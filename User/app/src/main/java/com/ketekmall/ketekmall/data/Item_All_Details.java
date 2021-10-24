package com.ketekmall.ketekmall.data;

public class Item_All_Details {

    private String id;
    private String seller_id;
    private String item_id;
    private String ad_detail, price, division, district, brand, inner, stock, description;
    private String photo;
    private String photo02;
    private String photo03;
    private String photo04;
    private String photo05;
    private String main_category;
    private String sub_category;
    private String quantity;
    private String postcode;
    private String weight;
    private String deliveryPrice;

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }


    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    private String isChecked;

    private String max_order, shocking, delivery_status;

    public String getPhoto02() {
        return photo02;
    }

    public void setPhoto02(String photo02) {
        this.photo02 = photo02;
    }

    public String getPhoto03() {
        return photo03;
    }

    public void setPhoto03(String photo03) {
        this.photo03 = photo03;
    }

    public String getPhoto04() {
        return photo04;
    }

    public void setPhoto04(String photo04) {
        this.photo04 = photo04;
    }

    public String getPhoto05() {
        return photo05;
    }

    public void setPhoto05(String photo05) {
        this.photo05 = photo05;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getShocking() {
        return shocking;
    }

    public void setShocking(String shocking) {
        this.shocking = shocking;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    private String sold;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String rating;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getInner() {
        return inner;
    }

    public void setInner(String inner) {
        this.inner = inner;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getMax_order() {
        return max_order;
    }

    public void setMax_order(String max_order) {
        this.max_order = max_order;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Item_All_Details(String id,
                            String seller_id,
                            String main_category,
                            String sub_category,
                            String ad_detail,
                            String price,
                            String division,
                            String district,
                            String photo) {
        this.id = id;
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
