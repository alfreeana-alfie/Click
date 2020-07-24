package com.example.click;

public class Item_All_Details {

    private String id, seller_id;
    private String ad_detail, price, item_location;
    private String photo;
    private String main_category, sub_category;

    public Item_All_Details(String id, String seller_id, String main_category, String sub_category, String ad_detail, String price, String item_location, String photo) {
        this.id = id;
        this.seller_id = seller_id;
        this.main_category = main_category;
        this.sub_category = sub_category;
        this.ad_detail = ad_detail;
        this.price = price;
        this.item_location = item_location;
        this.photo = photo;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getMain_category() {
        return main_category;
    }

    public void setMain_category(String main_category) {
        this.main_category = main_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAd_detail() {
        return ad_detail;
    }

    public void setAd_detail(String ad_detail) {
        this.ad_detail = ad_detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getItem_location() {
        return item_location;
    }

    public void setItem_location(String item_location) {
        this.item_location = item_location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
