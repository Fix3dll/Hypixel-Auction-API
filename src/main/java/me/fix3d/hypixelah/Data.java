package me.fix3d.hypixelah;

public class Data {
    double lowbin = 0;
    double  totalPrice = 0.0;
    int totalItem = 0;
    String base64 = "";
    String uuid = "";
    public Data(double lowbin, double totalPrice, int totalItem, String base64, String uuid) {
        this.lowbin = lowbin;
        this.totalPrice = totalPrice;
        this.totalItem = totalItem;
        this.base64 = base64;
        this.uuid = uuid;
    }
}
