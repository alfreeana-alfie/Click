package com.ketekmall.ketekmall.data;

public class Receipt {

    String id;
    String customer_id;
    String seller_id;
    String item_id;
    String order_id;
    String quantity;
    String grand_total;
    String status;
    String date;

    public Receipt(String id, String customer_id, String seller_id, String item_id, String order_id, String quantity, String grand_total, String status, String date) {
        this.id = id;
        this.customer_id = customer_id;
        this.seller_id = seller_id;
        this.item_id = item_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.grand_total = grand_total;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
