package com.randomrainbow.springboot.demosecurity.entity;

import jakarta.persistence.*;
// if the id will be initialized at the data base how do i define the contructor
@Entity
@Table(name = "videos", schema = "random_rainbow")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="id_user")
    private int idUser;
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

    public Video(int idUser, String title, String videoDescription, String videoLink) {
        this.idUser = idUser;
        this.title = title;
        this.videoDescription = videoDescription;
        this.videoLink = videoLink;
        checked = false;
        approved = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", idUser" +
                "=" + idUser
                +
                ", title='" + title + '\'' +
                ", videoDescription='" + videoDescription + '\'' +
                ", videoLink='" + videoLink + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
