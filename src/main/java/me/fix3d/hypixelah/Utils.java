package me.fix3d.hypixelah;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.unirest.UnirestHttpClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.hypixel.api.reply.skyblock.SkyBlockAuctionsReply;

public class Utils {

    public static final HypixelAPI API = new HypixelAPI(new UnirestHttpClient(ConfigManager.readAPI()));

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
        }
        return totalPages;
    }

    public static JsonArray allAuctions() {
        JsonArray allAuctions = new JsonArray(); // All auctions will be stored here
        int totalPages = totalPages();
        List<CompletableFuture<SkyBlockAuctionsReply>> futureList = new ArrayList<>(); // List of all the completable futures
        for (int i = 0; i < totalPages; i++) { // Loop through all auctions
            futureList.add(
                API.getSkyBlockAuctions(i).whenComplete((page, throwable) -> {
                        allAuctions.add(page.getAuctions());
                }) // Add the auctions of this page to the array
            );
        }

        try {
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return allAuctions;
    }

    public static Data scraping(String text, String filter) {
        text = text.replace("[", "\\[");
        Pattern searchNamePattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);

        //Log
        System.out.println("Text: " + text);
        System.out.println("Pattern: " + searchNamePattern);
        System.out.println("Filter: " + filter);

        JsonArray allAuctions = Utils.allAuctions();
        Integer totalPages = allAuctions.size();

        int lowbin = 0;
        int totalItem = 0;
        String base64 = "";
        String uuid = "";
        for (int i = 0; i < totalPages; i++) {
            JsonArray pageIndex = allAuctions.get(i).getAsJsonArray();
            for (int aucIndex = 0; aucIndex < pageIndex.size(); aucIndex++) {
                try {
                    JsonObject aucOptions = pageIndex.get(aucIndex).getAsJsonObject();
                    if (aucOptions.get("bin").getAsBoolean()) {
                        Matcher searchName = searchNamePattern.matcher(aucOptions.get("item_name").getAsString());
                        if (searchName.find()) {
                            if (!filter.equals("NO FILTER")) {
                                if (aucOptions.get("tier").getAsString().equals(filter)) {
                                    uuid = aucOptions.get("auctioneer").getAsString();
                                    int itemPrice = aucOptions.get("starting_bid").getAsInt();
                                    totalItem++;
                                    if (lowbin == 0) {
                                        lowbin = itemPrice;
                                        base64 = aucOptions.get("item_bytes").getAsString();
                                    } else if (lowbin != 0 && itemPrice < lowbin) {
                                        lowbin = itemPrice;
                                        base64 = aucOptions.get("item_bytes").getAsString();
                                    }
                                }
                            } else {
                                uuid = aucOptions.get("auctioneer").getAsString();
                                int itemPrice = aucOptions.get("starting_bid").getAsInt();
                                totalItem++;
                                if (lowbin == 0) {
                                    lowbin = itemPrice;
                                    base64 = aucOptions.get("item_bytes").getAsString();
                                } else if (lowbin != 0 && itemPrice < lowbin) {
                                    lowbin = itemPrice;
                                    base64 = aucOptions.get("item_bytes").getAsString();
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    //for catch null BIN
                    pageIndex.remove(aucIndex);
                    aucIndex--;
                }
            }
        }
        return new Data(lowbin, totalItem, base64, uuid);
    }
}
