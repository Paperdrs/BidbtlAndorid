package com.android.bidbatl.Model;

public class TopSellingDataprovider {

    String titel,weight,rate,lowest,delivery;
    int image;

    public TopSellingDataprovider(String titel, String weight, String rate, String lowest, String delivery, int image) {
        this.titel = titel;
        this.weight = weight;
        this.rate = rate;
        this.lowest = lowest;
        this.delivery = delivery;
        this.image = image;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
