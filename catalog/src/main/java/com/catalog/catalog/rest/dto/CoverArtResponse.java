package com.catalog.catalog.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverArtResponse {

    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        private Map<String, String> thumbnails;

        public Map<String, String> getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Map<String, String> thumbnails) {
            this.thumbnails = thumbnails;
        }
    }
}

