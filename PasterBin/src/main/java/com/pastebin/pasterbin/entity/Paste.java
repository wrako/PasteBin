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

//    @Field(targetType = FieldType.DATE)
    private Instant expirationTime;

///  For future update
//    private int views;
//    private int likes;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private boolean isEdited;

//    private Author author;
//    private List<Attachment> attachments;
//    private List<Comment> comments;
//    private List<String> tags;


    public Paste(String title, String blobUrl, LocalDateTime expirationTime) {
        this.title = title;
        this.blobUrl = blobUrl;
        this.expirationTime = expirationTime.toInstant(ZoneOffset.UTC);
    }
}


