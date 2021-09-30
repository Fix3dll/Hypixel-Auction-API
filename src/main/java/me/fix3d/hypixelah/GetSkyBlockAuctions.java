package me.fix3d.hypixelah;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import static java.lang.Integer.parseInt;
import static java.lang.Math.pow;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;

public class GetSkyBlockAuctions {
    public static void main(String[] args) throws BadLocationException {
        try {
            System.setOut(new PrintStream(System.out, true, "UTF8"));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        
        /*NBT test
        long testStart = System.nanoTime();
        String test = Util.NBTconvert("H4sIAAAAAAAAAHVUW3PSQBQ+FKmAlzrOqG+6Ona001YJhFvfKtQ2VWkt4OWJWZIlrE1242Zj5b/4zow/g5/iD3E8S4itD+Zhk3PynS/n8p2UAUqQ42UAyK3BGvdyhRwUOjIROleGvKZ+Ca4x4U7BXDm4MRRjxeg5HQcsl4fSEffY64D6Mb79XYbrHo+jgM4w6K1UrIjee/BoMW8eMqpI30XfHlnMvUbTxlvrudVqtLbgGQK6NKT+8qW7XbNbeGfPt6uVrSVs29qp2PYWVBDY14oJX09TqFVpXIW28aG1iqlZSL2NER3FNelMqXBX/NXqZoq1qpsZuLa5BbsZ+GoyVqO+QtfrGdpu4yPWZqM/JSaO46CxY5CdZMzjkHzI7K6ivhRkoKjLFPkALfRhz0IMyiBOGNGAC/8qjSMmXHDNyPuEf8NA5xPsoPuUM+UuoRnwVF4Y3r9mguNCIniMRl/w6J/kBixg51ywmMfEwRIeLub1/lQqPZYXe8QRsaZCBzMST6XU8ePl8I64jk0vang0QzmOCZbDMWVXv4AHpmlUEG/ZM8KEx1TIxAvkvmsSiDh2NKEBeSVFEsOmEYPCb8SEIh21Kqa7TTftopYkjuiFgCfooyQNJl3myhm5mDJBZjIh5zwICBVwHzFMsHBGuEAyLxE+k8sP7y3mjcU8WMzP6WKuSGaZ8+3B4UGvu3/2mXSHvcODkx55dfKRXOKLcK2Ho4E2ui5zP06+JCRrk0H/+vHzfyeUYePgu1Z0X2vFx4lmcR42FEWq2SiJfEU9ZtYF1+fOVOpRJDXVcuSanUN3uQjFUHp8gnOGUpylkIe7qwJHqIlwFLBvLEB4oQglqbjPxYD6cPv90Om8GXXO9l8PnN5h0Ww03DoeHg9H/aOTswHWWoabZp9xAjgljakV+Up8yJbPw3o6itQoRiu1pflu8JUmR1+XmkRvKQ+FyAjQ5JKHG/pSX2lMITYSTOkKEyP71L/uLtckDVunPEy/UjB8SfrDWQOcRpJgCU8bzYbdps36bsWq0F3bmlR225PGBA+36bVct261m9gJzUOGCg4juN1+2X5ZtUhtr2qT03eGbD1da/Mj+wNoLbtl9wQAAA\u003d\u003d");
        System.out.println(test);
        long testFin = System.nanoTime();
        System.out.println("Hesaplama süresi (saniye): "+ (testFin - testStart)/pow(10,9));
        */
        
        /* time benchmark
        long testStart = System.nanoTime();
        JsonArray test = Util.allAuctions();
        long testFin = System.nanoTime();
        System.out.println("Hesaplama süresi (saniye): "+ (testFin - testStart)/pow(10,9));
        /*/

        GUI gui = new GUI();
        
        while (GUI.stext == null) {
            if (GUI.stext != null) {
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } gui.print("\""+GUI.stext+"\" için tarama başladı...\n", 85, 255, 85);
        
        long startTime = System.nanoTime();
        Data data = Util.scraping(GUI.stext);
        
        String resultStr = Util.NBTconvert(data.base64);
        if (resultStr == null || resultStr == "") {
            gui.print("\""+GUI.stext+"\" bulunamadı! Arama tamamalandı.\n", 255, 85, 85);
            Util.await();
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        Matcher matchCount = Pattern.compile("(?<=Count:).*?(?=b,)").matcher(resultStr);
        Matcher matchRecomb = Pattern.compile("(?<=rarity_upgrades:).*?(?=,)").matcher(resultStr);
        Matcher matchPotato = Pattern.compile("(?<=hot_potato_count:).*?(?=,)").matcher(resultStr);
        Matcher matchEnchs = Pattern.compile("(?<=enchantments:).*?(?=,uuid)").matcher(resultStr);
        Matcher matchGems = Pattern.compile("(?<=gems:\\{).*?(?=},)").matcher(resultStr);
        Matcher matchName = Pattern.compile("(?<=Name:\").*?(?=\")").matcher(resultStr);
        
        if (matchName.find()) {
            String coloredName = Replace.modifiedColor(matchName.group());
            gui.print(coloredName, 255, 85, 255);
            
            Util.API.getPlayerByUuid(data.uuid).whenComplete((profile,throwable) -> {
                try {
                    gui.print("Auctioneer: &(255,255,255)"+profile.getPlayer().getName(), 255, 170, 0);
                } catch (BadLocationException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            });
            
            Matcher matchPet = Pattern.compile("\\[Lvl").matcher(coloredName);
            if (matchPet.find()) {
                Matcher matchEXP = Pattern.compile("(?<=\"exp\":).*?(?=,\")").matcher(resultStr);
                Matcher matchItem = Pattern.compile("(?<=\"heldItem\":\").*?(?=\",\")").matcher(resultStr);
                Matcher matchCandy = Pattern.compile("(?<=\"candyUsed\":)\\d+").matcher(resultStr);
                if (matchEXP.find()) {
                    if (matchEXP.group().contains("E")) {
                        int exp = new BigDecimal(matchEXP.group()).intValue();
                        gui.print("Pet EXP: &(255,255,255)"+formatter.format(exp), 255, 170, 0);
                    } else {
                        String[] intEXP = matchEXP.group().split("\\.");
                        gui.print("Pet EXP: &(255,255,255)"+formatter.format(parseInt(intEXP[0])), 255, 170, 0);
                    }
                }
                if (matchItem.find()) {
                    gui.print("Pet Item: &(255,255,255)"+matchItem.group().replace("PET_ITEM_",""), 255, 170, 0);
                } else {
                    gui.print("Pet Item: &(255,255,255)[]", 255, 170, 0);
                }
                if (matchCandy.find()) {
                    gui.print("Pet Candy: &(255,255,255)"+matchCandy.group()+"/10", 255, 170, 0);
                }
            } else {
                String counter;
                if(matchCount.find()) {
                    counter = matchCount.group();
                    if (parseInt(counter) > 1) {
                        gui.print("Miktar: "+counter, 255, 85, 255);
                    }
                }
                if(matchRecomb.find()) {
                    gui.print("Recombulated: &(85,255,85)✓", 255, 85, 255);
                } else {
                    gui.print("Recombulated: &(255,85,85)╳", 255, 85, 255);
                }
                if (matchPotato.find()) {
                    int potCount = parseInt(matchPotato.group());
                    if (potCount > 10) {
                        gui.print("Hot Potato Count: &(255,170,0)10 + "+String.valueOf(potCount-10), 255, 255, 85);
                    } else {
                        gui.print("Hot Potato Count: &(255,170,0)"+matchPotato.group(), 255, 255, 85);
                    }
                } else {
                    gui.print("Hot Potato Count: &(255,170,0)0", 255, 255, 85);
                }
                if (matchEnchs.find()) {
                    gui.print("Enchantments: &(85,255,255)"
                            +Replace.modifiedEnchs(matchEnchs.group()), 85, 85, 255);
                } else {
                    gui.print("Enchantments: &(85,255,255)[]", 85, 85, 255);
                }
                if (matchGems.find()) {
                    gui.print("Gems: &(255,255,255)"
                            +Replace.modifiedGems(matchGems.group()), 170, 0, 170);
                } else {
                    gui.print("Gems: &(255,255,255)[]", 200, 0, 200);
                }
            }
        }
        long stopTime = System.nanoTime();
        
        gui.print("Low BIN: &(85,255,85)"+formatter.format(data.lowbin), 0, 170, 0);
        //gui.print("Ortalama fiyat: &(85,255,85)"+formatter.format(data.totalPrice/data.totalItem), 0, 204, 0);
        gui.print("Piyasada eşleşen eşya miktarı: &(255,255,255)"+data.totalItem, 255, 170, 0);
        gui.print("Hesaplama süresi [saniye]: &(255,255,255)"+(stopTime - startTime)/pow(10,9), 255, 170, 0);

        System.gc();
        Util.await();
    }
}
