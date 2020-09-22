package com.ketekmall.ketekmall.data;

public class OrderDone {
    String ItemImage;
    String ItemName;
    String ItemPrice;
    String DeliveryAddress;
    String DeliveryTime;
    String DeliveryPrice;
    String Grandtotal;
    String Status;
    String Quantity;

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }



    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    public String getDeliveryPrice() {
        return DeliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        DeliveryPrice = deliveryPrice;
    }

    public String getGrandtotal() {
        return Grandtotal;
    }

    public void setGrandtotal(String grandtotal) {
        Grandtotal = grandtotal;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public OrderDone() {
    }
}
