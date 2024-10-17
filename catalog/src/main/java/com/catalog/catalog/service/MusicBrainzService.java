package com.catalog.catalog.service;

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
            throw new RuntimeException("Failed to parse Json");
        }
    }

}
