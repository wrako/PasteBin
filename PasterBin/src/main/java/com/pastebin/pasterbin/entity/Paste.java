package com.pastebin.pasterbin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Paste {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String blobUrl;

    private LocalDateTime expirationTime;

    public Paste(String title, String blobUrl, LocalDateTime expirationTime) {
        this.title = title;
        this.blobUrl = blobUrl;
        this.expirationTime = expirationTime;
    }


}


