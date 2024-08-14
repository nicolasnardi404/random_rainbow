package com.randomrainbow.springboot.demosecurity.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User idUser;

    @Column(name = "title")
    @NotNull(message = "Title is required")
    private String title;

    @Column(name = "video_description")
    @NotNull(message = "Video Description is required")
    private String videoDescription;

    @Column(name = "video_link")
    @NotNull(message = "Video Link is required")
    private String videoLink;

    private int duration;

    private boolean active;

    @Column(name = "video_status")
    private VideoStatus videoStatus;

    private String endpoint;

    @Column(name = "submission_date")
    private Date submissionDate;

    @Column(name = "approved_date")
    private Date approvedDate;

    @Column(name = "message_error")
    private String messageError;

  

    @Override
    public String toString() {
        return "Video [id=" + id + ", idUser=" + idUser + ", title=" + title + ", videoDescription=" + videoDescription
                + ", videoLink=" + videoLink + ", duration=" + duration + ", active=" + active + ", videoStatus="
                + videoStatus + ", endpoint=" + endpoint + ", submissionDate=" + submissionDate + ", approvedDate="
                + approvedDate + ", messageError=" + messageError + "]";
    }


    public String getMessageError() {
        return messageError;
    }


    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }


    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public VideoStatus getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(VideoStatus videoStatus) {
        this.videoStatus = videoStatus;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
}
