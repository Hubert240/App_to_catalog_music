package com.catalog.catalog.service;

import com.catalog.catalog.rest.dto.CoverArtResponse;
import com.catalog.catalog.rest.dto.RecordingsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MusicBrainzService {


    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Autowired
    public MusicBrainzService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public RecordingsResponse getRecordingInfo(String title, String artist) {
        String query = String.format("recording:\"%s\" AND artist:\"%s\"", title, artist);
        String url = "https://musicbrainz.org/ws/2/recording/?query=" + query + "&fmt=json&limit=1";

        return restTemplate.getForObject(url, RecordingsResponse.class);
    }


    public RecordingsResponse getRecordingInfoJson(String title, String artist) {
        String query = String.format("recording:\"%s\" AND artist:\"%s\"", title, artist);
        String url = "https://musicbrainz.org/ws/2/recording/?query=" + query + "&fmt=json&limit=1";

        String json = restTemplate.getForObject(url, String.class);
        try {
            return objectMapper.readValue(json, RecordingsResponse.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    public String getCoverArtUrl(String releaseId) {
        String url = "https://coverartarchive.org/release/" + releaseId + "/front";
        try {
            String coverArtUrl = restTemplate.getForObject(url, String.class);
            return coverArtUrl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch cover art for releaseId: " + releaseId);
        }
    }

    public String getCoverArtForRecording(String title, String artist) {
        RecordingsResponse response = getRecordingInfoJson(title, artist);
        if (response != null && !response.getRecordings().isEmpty()) {
            String releaseId = response.getRecordings().get(0).getReleases().get(0).getId();
            return getCoverArtUrl(releaseId);
        }
        throw new RuntimeException("No release found for the recording");
    }


    public String getCoverArtForReleaseId(String releaseId) {
        String url = "https://coverartarchive.org/release/" + releaseId;

        String coverArtData = restTemplate.getForObject(url, String.class);
        return coverArtData;
    }

    public String getThumbnail250(String releaseId) {
        String url = "http://coverartarchive.org/release/" + releaseId;
        CoverArtResponse response = restTemplate.getForObject(url, CoverArtResponse.class);

        if (response != null && response.getImages() != null && !response.getImages().isEmpty()) {
            return response.getImages().get(0).getThumbnails().get("250");
        }

        throw new RuntimeException("Thumbnail not found for release ID: " + releaseId);
    }


}
