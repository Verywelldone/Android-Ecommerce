package com.example.ecommerce.model;

public class Products {
    private String pname;
    private String description;
    private String price;
    private String category;
    private String image;
    private String pid;
    private String date;
    private String time;

    public Products() {
    }

    public Products(String pname, String description, String price, String category, String image, String pid, String date, String time) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}