package com.randonrainbow.springboot.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

// if the id will be initialized at the data base how do i define the contructor
@Entity
@Getter
@Setter
@Table(name="video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="id_artist")
    private int idArtist;
    @Column(name="title")
    private String title;
    @Column(name="video_description")
    private String videoDescription;
    @Column(name="video_link")
    private String videoLink;
   private int duration;
   private boolean checked;
   private boolean approved;

    public Video(){
    }

    public Video(int idArtist, String title, String videoDescription, String videoLink) {
        this.idArtist = idArtist;
        this.title = title;
        this.videoDescription = videoDescription;
        this.videoLink = videoLink;
        checked = false;
        approved = false;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", idArtist=" + idArtist +
                ", title='" + title + '\'' +
                ", videoDescription='" + videoDescription + '\'' +
                ", videoLink='" + videoLink + '\'' +
                '}';
    }
}
