package com.randomrainbow.springboot.demosecurity.entity;

import jakarta.persistence.*;
// if the id will be initialized at the data base how do i define the contructor
@Entity
@Table(name = "videos", schema = "random_rainbow")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;
    @Column(name = "title")
    private String title;
    @Column(name = "video_description")
    private String videoDescription;
    @Column(name = "video_link")
    private String videoLink;
    private int duration;
    private boolean checked;
    private boolean approved;
    private String endpoint;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Video() {
    }

    @Override
    public String toString() {
        return "Video [id=" + id + ", idUser=" + idUser + ", title=" + title + ", videoDescription=" + videoDescription
                + ", videoLink=" + videoLink + ", duration=" + duration + ", checked=" + checked + ", approved="
                + approved + ", endpoint=" + endpoint + "]";
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return idUser;
    }

    public void setUser(User user) {
        this.idUser = user;
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
}
