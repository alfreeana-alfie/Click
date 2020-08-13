package com.example.click;

public class Order {

    private String id, seller_id, customer_id, item_id;
    private String ad_detail, price, division, district;
    private String photo;
    private String main_category, sub_category, date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Order(String id,
                 String seller_id,
                 String ad_detail,
                 String main_category,
                 String sub_category,
                 String price,
                 String division,
                 String district,
                 String photo,
                 String item_id,
                 String customer_id,
                 String date) {
        this.id = id;
        this.seller_id = seller_id;
        this.customer_id = customer_id;
        this.item_id = item_id;
        this.ad_detail = ad_detail;
        this.price = price;
        this.division = division;
        this.district = district;
        this.photo = photo;
        this.main_category = main_category;
        this.sub_category = sub_category;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
}
