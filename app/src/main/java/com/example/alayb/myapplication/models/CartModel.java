package com.example.alayb.myapplication.models;

/**
 * Created by alayb on 19-Jul-19.
 */

public class CartModel {

    String name,price,qty,img,cartId,productId;

    public CartModel(String name, String price, String qty, String img, String cartId, String productId) {
        this.name = name;
        this.price = price;
        this.qty = qty;
        this.img = img;
        this.cartId = cartId;
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getImg() {
        return img;
    }

    public String getCartId() {
        return cartId;
    }

    public String getProductId() {
        return productId;
    }
}
