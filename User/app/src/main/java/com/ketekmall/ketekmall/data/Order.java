package com.ketekmall.ketekmall.data;

import android.text.Spanned;

public class Order {

    private String id, seller_id, customer_id, item_id;
    private String ad_detail, price, division, district, seller_division, seller_district;
    private String photo;
    private String main_category;
    private String sub_category;
    private String date;
    private String quantity;
    private String status;
    private String tracking_no;
    private String weight;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Spanned getStatus1() {
        return status1;
    }

    public void setStatus1(Spanned status1) {
        this.status1 = status1;
    }

    private Spanned status1;

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    private String delivery_price;
    private String delivery_date;

    public String getSeller_division() {
        return seller_division;
    }

    public void setSeller_division(String seller_division) {
        this.seller_division = seller_division;
    }

    public String getSeller_district() {
        return seller_district;
    }

    public void setSeller_district(String seller_district) {
        this.seller_district = seller_district;
    }

    public String getTracking_no() {
        return tracking_no;
    }

    public void setTracking_no(String tracking_no) {
        this.tracking_no = tracking_no;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getOrder_date() {
        return order_date;
    }

    private String order_date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                 String order_date,
                 String date,
                 String quantity,
                 String status) {
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
        this.order_date = order_date;
        this.date = date;
        this.quantity = quantity;
        this.status = status;
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
