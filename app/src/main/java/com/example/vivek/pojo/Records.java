package com.example.vivek.pojo;

/**
 * Created by vIvEk on 05-01-2016.
 */

public class Records {

    private String service_date;
    private String name;
    private String category;

    public Records() {
    }

    public Records(String service_date, String name, String category) {
        super();
        this.service_date = service_date;
        this.name = name;
        this.category = category;
    }

    public String getServiceDate() {
        return service_date;
    }

    public void setServiceDate(String service_date) {
        this.service_date = service_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book [id=" + service_date + ", name=" + name + ", category=" + category
                + "]";
    }
}