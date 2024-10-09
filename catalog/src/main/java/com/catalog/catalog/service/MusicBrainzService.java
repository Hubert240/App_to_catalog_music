package com.catalog.catalog.service;

import com.catalog.catalog.rest.dto.RecordingsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MusicBrainzService {

    private final RestTemplate restTemplate;

    @Autowired
    public MusicBrainzService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RecordingsResponse getRecordingInfo(String title, String artist) {
        String query = String.format("recording:\"%s\" AND artist:\"%s\"", title, artist);
        String url = "https://musicbrainz.org/ws/2/recording/?query=" + query + "&fmt=json&limit=1";

        return restTemplate.getForObject(url, RecordingsResponse.class);
    }
}
