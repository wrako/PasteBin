package com.pastebin.pasterbin.entity;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@Document(collection = "posts")  // MongoDB collection name
public class Paste {
    
    @Id
    private String id;
    private String title;
    private String blobUrl;
    private Instant expirationTime;

///  For future update
    private int views;
    private int likes;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isEdited;

//    private Author author;
//    private List<Attachment> attachments;
//    private List<Comment> comments;
//    private List<String> tags;


    public Paste(String title, String blobUrl, LocalDateTime expirationTime, int views, int likes, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isEdited) {
        this.title = title;
        this.blobUrl = blobUrl;
        this.expirationTime = expirationTime.toInstant(ZoneOffset.UTC);
        this.views = views;
        this.likes = likes;
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC);
        this.updatedAt = updatedAt.toInstant(ZoneOffset.UTC);
        this.isEdited = isEdited;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime.toInstant(ZoneOffset.UTC);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt.toInstant(ZoneOffset.UTC);
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt.toInstant(ZoneOffset.UTC);
    }
}


