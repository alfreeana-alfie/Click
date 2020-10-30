package com.ketekmall.ketekmall.data;

import android.text.Spanned;

public class Checkout_Data {
    String delivery_id;
    String delivery_user_id;
    String delivery_division;
    String delivery_price;
    String delivery_division1;
    String delivery_price1;
    Spanned delivery_price2;
    String delivery_days;
    String delivery_item_id;
    private String id, seller_id;
    private String ad_detail, price, division, district;
    private String photo;
    private String main_category;
    private String sub_category;
    private String quantity;

    public Spanned getDelivery_price2() {
        return delivery_price2;
    }

    public void setDelivery_price2(Spanned delivery_price2) {
        this.delivery_price2 = delivery_price2;
    }

    public String getDelivery_division1() {
        return delivery_division1;
    }

    public void setDelivery_division1(String delivery_division1) {
        this.delivery_division1 = delivery_division1;
    }

    public String getDelivery_price1() {
        return delivery_price1;
    }

    public void setDelivery_price1(String delivery_price1) {
        this.delivery_price1 = delivery_price1;
    }



    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getDelivery_user_id() {
        return delivery_user_id;
    }

    public void setDelivery_user_id(String delivery_user_id) {
        this.delivery_user_id = delivery_user_id;
    }

    public String getDelivery_division() {
        return delivery_division;
    }

    public void setDelivery_division(String delivery_division) {
        this.delivery_division = delivery_division;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getDelivery_days() {
        return delivery_days;
    }

    public void setDelivery_days(String delivery_days) {
        this.delivery_days = delivery_days;
    }

    public String getDelivery_item_id() {
        return delivery_item_id;
    }

    public void setDelivery_item_id(String delivery_item_id) {
        this.delivery_item_id = delivery_item_id;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Checkout_Data() {
    }
}
