package com.catalog.catalog.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RecordingsResponse {
    public List<Recording> recordings;

    public static class Recording {
        public String id;
        public int score;
        public String title;
        public long length;
        @JsonProperty("video")
        public Object video;

        @JsonProperty("artist-credit")
        public List<ArtistCredit> artistCredit;

        @JsonProperty("releases")
        public List<Release> releases;

        @JsonProperty("isrcs")
        public List<String> isrcs;




        public static class ArtistCredit {
            public String name;
            public Artist artist;

            public static class Artist {
                public String id;
                public String name;
                @JsonProperty("sort-name")
                public String sortName;
                public String disambiguation;
            }
        }
            public static class Release {
                public String id;
                public String title;
                public String date;
                public String country;
                @JsonProperty("track-count")
                public int trackCount;
                public List<Media> media;

                public static class Media {
                    public int position;
                    public String format;
                    @JsonProperty("track")
                    public List<Track> track;

                    public static class Track {
                        public String id;
                        public String number;
                        public String title;
                        public long length;
                    }
                }


        }


    }
}
