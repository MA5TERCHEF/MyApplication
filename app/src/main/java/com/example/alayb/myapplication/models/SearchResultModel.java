package com.example.alayb.myapplication.models;

/**
 * Created by alayb on 19-Jul-19.
 */

public class SearchResultModel {

    String id,name,price,discount,category,image;

    public SearchResultModel(String id, String name, String price, String discount, String category, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.category = category;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getDiscount() {
        return discount;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }
}
