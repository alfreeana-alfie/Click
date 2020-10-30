package com.ketekmall.ketekmall.data;

public class Product_Add_Data {

    private String seller_id;
    private String ad_detail, price, division, district;
    private String photo;
    private String main_category;
    private String sub_category;
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Product_Add_Data(String seller_id, String main_category, String sub_category, String ad_detail, String price, String division, String district, String photo) {
        this.seller_id = seller_id;
        this.main_category = main_category;
        this.sub_category = sub_category;
        this.ad_detail = ad_detail;
        this.price = price;
        this.division = division;
        this.district = district;
        this.photo = photo;
        this.quantity = quantity;
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
}
