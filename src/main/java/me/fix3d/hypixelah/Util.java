package me.fix3d.hypixelah;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;
import net.hypixel.api.reply.skyblock.SkyBlockAuctionsReply;

public class Util {

    public static final HypixelAPI API;

    static {
        API = new HypixelAPI(new ApacheHttpClient(ConfigManager.readAPI()));
    }

    /**
     * Keep the program alive till we explicitly exit.
     */
    public static void await() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static int totalPages() {
        int totalPages = 0;
        try {
            totalPages = API.getSkyBlockAuctions(0).get().getTotalPages(); // Get the total pages
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("totalPages() exception!");
            e.printStackTrace();
        } return totalPages;
    }

    public static JsonArray allAuctions() {
        JsonArray allAuctions = new JsonArray(); // All auctions will be stored here
        int totalPages = totalPages();
        List<CompletableFuture<SkyBlockAuctionsReply>> futureList = new ArrayList<>(); // List of all the completable futures
        for(int i= 0; i < totalPages; i++){ // Loop through all auctions
            futureList.add(
                    API.getSkyBlockAuctions(i).whenComplete((page, throwable) -> allAuctions.add(page.getAuctions())) // Add the auctions of this page to the array
            );
        }

        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        return allAuctions;
    }
    
    public static String NBTconvert(String base64) {
        String resultStr = null;
        try {
            NBTCompound result = NBTReader.readBase64(base64);
            resultStr = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Eşya bulunamadı!");
        }
        return resultStr;
    }
    
    public static Data scraping(String text) {
        System.out.println("Text: "+text);
        text = text.replace("[","\\[");
        Pattern searchNamePattern;
        String tier;
        if (text.contains(",")) {
            String [] textArray = text.split(",");
            tier = textArray[1].trim().toUpperCase(Locale.ENGLISH);
            searchNamePattern = Pattern.compile(textArray[0].trim(), Pattern.CASE_INSENSITIVE);
        } else {
            searchNamePattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
            tier = null;
        }
        System.out.println("Pattern: "+searchNamePattern);
        System.out.println("Tier: "+tier);
        
        JsonArray allAuctions = Util.allAuctions();
        Integer totalPages = allAuctions.size();
        
        int lowbin = 0;
        int totalPrice = 0;
        int totalItem = 0;
        String base64 = "";
        String uuid = "";
        for (int i = 0; i < totalPages; i++) {
            JsonArray pagex = allAuctions.get(i).getAsJsonArray();
            int aucx = 0;
            while (aucx < pagex.size()) {
                try {
                    JsonObject objectx = pagex.get(aucx).getAsJsonObject();
                    if (objectx.get("bin").getAsBoolean()) {
                        Matcher searchName = searchNamePattern.matcher(objectx.get("item_name").getAsString());
                        if (searchName.find()) {
                            if (text.contains(",")) {
                                if (objectx.get("tier").getAsString().equals(tier)) {
                                    uuid = objectx.get("auctioneer").getAsString();
                                    int itemPrice = objectx.get("starting_bid").getAsInt();
                                    totalItem++;
                                    totalPrice += objectx.get("starting_bid").getAsInt();
                                    if (lowbin == 0) {
                                        lowbin = itemPrice;
                                        base64 = objectx.get("item_bytes").getAsString();
                                    } else if (lowbin != 0 && itemPrice < lowbin) {
                                        lowbin = itemPrice;
                                        base64 = objectx.get("item_bytes").getAsString();
                                    }
                                }
                            } else {
                                uuid = objectx.get("auctioneer").getAsString();
                                int itemPrice = objectx.get("starting_bid").getAsInt();
                                totalItem++;
                                totalPrice += itemPrice;
                                if (lowbin == 0) {
                                    lowbin = itemPrice;
                                    base64 = objectx.get("item_bytes").getAsString();
                                } else if (lowbin != 0 && itemPrice < lowbin) {
                                    lowbin = itemPrice;
                                    base64 = objectx.get("item_bytes").getAsString();
                                }
                            }
                        }
                    }
                } catch (NullPointerException  e) {
                    //for catch null BIN
                    pagex.remove(aucx);
                    aucx--;
                }
                aucx++;
            }
        } return new Data(lowbin, totalPrice, totalItem, base64, uuid);
    }
}