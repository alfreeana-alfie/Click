package com.example.click.helper;

public class Item {

    private String id;
    private String ad_detail, price, item_location;
    private String photo;

    public Item(String id, String ad_detail, String price, String item_location, String photo) {
        this.id = id;
        this.ad_detail = ad_detail;
        this.price = price;
        this.item_location = item_location;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getAd_detail() {
        return ad_detail;
    }

    public String getPrice() {
        return price;
    }

    public String getItem_location() {
        return item_location;
    }

    public String getPhoto() {
        return photo;
    }
}
