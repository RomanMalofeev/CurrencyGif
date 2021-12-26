package com.alfa.currencygif.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;


@JsonAutoDetect
public class DataGiphy {

    @JsonProperty("type")
    private String type;

    @JsonProperty("id")
    private String id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("images")
    private Map<String, ImageGiphy> images;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setImages(Map<String, ImageGiphy> images) {
        this.images = images;
    }

    public Map<String, ImageGiphy> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "DataGiphy{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", rating='" + rating + '\'' +
                ", images=" + images.toString() +
                '}';
    }
}
