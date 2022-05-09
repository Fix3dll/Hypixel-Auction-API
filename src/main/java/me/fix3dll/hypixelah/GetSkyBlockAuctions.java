package me.fix3dll.hypixelah;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static java.lang.Math.pow;
import java.text.DecimalFormat;
import javax.swing.text.BadLocationException;
import kong.unirest.Unirest;
import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;

public class GetSkyBlockAuctions {

    public static void main(String[] args) throws BadLocationException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }

        GUI gui = new GUI();

        while (gui.text == null) {
            if (gui.text != null) {
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        gui.print("\"" + gui.text + "\" için tarama başladı...\n", 85, 255, 85);

        long startTime = System.nanoTime();
        Data data = Utils.scraping(gui.text, gui.filter);

        NBTCompound result = null;
        try {
            result = NBTReader.readBase64(data.base64).getList("i").getCompound(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (result == null) {
            gui.print("\"" + gui.text + "\" bulunamadı! Arama tamamalandı.\n", 255, 85, 85);
            Utils.await();
        }

        //Doesn't need API key.
        Unirest.get("https://sessionserver.mojang.com/session/minecraft/profile/" + data.uuid)
                .asJsonAsync().whenComplete((profile, throwable) -> {
                    String auctioneer = profile.getBody().getObject().getString("name");
                    try {
                        gui.print("Auctioneer: &(255,255,255)" + auctioneer, 255, 170, 0);
                    } catch (BadLocationException | IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                });

        int count = result.getInt("Count", 0);
        int recomb = result.getCompound("tag").getInt("ExtraAttributes.rarity_upgrades", 0);
        int potato = result.getCompound("tag").getInt("ExtraAttributes.hot_potato_count", 0);
        String enchs = result.getCompound("tag").getString("ExtraAttributes.enchantments", "[]");
        String gems = result.getCompound("tag").getString("ExtraAttributes.gems", "[]");
        String name = result.getCompound("tag").getString("display.Name", "");

        String coloredName = Replace.modifiedColor(name);
        gui.print(coloredName, 255, 85, 255);

        DecimalFormat formatter = new DecimalFormat("#,###");

        if (!result.getCompound("tag").getString("ExtraAttributes.petInfo", "").equals("")) {
            String petInfo = result.getCompound("tag").getString("ExtraAttributes.petInfo", "").toString();
            JsonObject json = new JsonParser().parse(petInfo).getAsJsonObject();
            String petItem = json.get("heldItem").getAsString().replace("PET_ITEM_", "");
            int petEXP = json.get("exp").getAsBigDecimal().intValue();
            int petCandy = json.get("candyUsed").getAsInt();

            gui.print("Pet EXP: &(255,255,255)" + formatter.format(petEXP), 255, 170, 0);
            gui.print("Pet Item: &(255,255,255)" + petItem, 255, 170, 0);
            gui.print("Pet Candy: &(255,255,255)" + petCandy + "/10", 255, 170, 0);
        } else {
            if (count > 1) {
                gui.print("Miktar: " + count, 255, 85, 255);
            }

            if (recomb == 1) {
                gui.print("Recombulated: &(85,255,85)✓", 255, 85, 255);
            } else {
                gui.print("Recombulated: &(255,85,85)╳", 255, 85, 255);
            }

            if (potato > 10) {
                gui.print("Hot Potato Count: &(255,170,0)10 + " + (potato - 10), 255, 255, 85);
            } else {
                gui.print("Hot Potato Count: &(255,170,0)" + potato, 255, 255, 85);
            }

            gui.print("Enchantments: &(85,255,255)" + Replace.modifiedEnchs(enchs), 85, 85, 255);
            gui.print("Gems: &(255,255,255)" + Replace.modifiedGems(gems), 200, 0, 200);

            long stopTime = System.nanoTime();

            gui.print("Low BIN: &(85,255,85)" + formatter.format(data.lowbin), 0, 170, 0);
            //gui.print("Ortalama fiyat: &(85,255,85)"+formatter.format(data.totalPrice/data.totalItem), 0, 204, 0);
            gui.print("Piyasada eşleşen eşya miktarı: &(255,255,255)" + data.totalItem, 255, 170, 0);
            gui.print("Hesaplama süresi [saniye]: &(255,255,255)" + (stopTime - startTime) / pow(10, 9), 255, 170, 0);

            System.gc();
            Utils.await();
        }
    }
}