package com.randomrainbow.springboot.demosecurity.dto;

import java.util.List;

import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistProfileDTO {
    private String username;
    private List<ArtistVideoDTO> listVideos;
    private ArtistDataUserProfileDTO dataUserProfile;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistVideoDTO {
        private int id;
        private String title;
        private VideoStatus videoStatus;
        private String endpoint;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistDataUserProfileDTO {
        private String artistDescription;
        private String socialMedia;
        private String username;
    }
}