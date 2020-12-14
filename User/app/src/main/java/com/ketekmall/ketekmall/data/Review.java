package com.ketekmall.ketekmall.data;

public class Review {
    String review;
    Float rating;
    String Customer_Name;

    public Review(String Customer_Name, String review, Float rating) {
        this.Customer_Name = Customer_Name;
        this.review = review;
        this.rating = rating;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
