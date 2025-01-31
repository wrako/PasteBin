package com.pastebin.pasterbin.controller;

import com.pastebin.pasterbin.dto.PasteRequest;
import com.pastebin.pasterbin.service.BlobStorageService;
import com.pastebin.pasterbin.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class PasteBinController {

    private final BlobStorageService blobStorageService;
    private final RedisService redisService;

    @Autowired
    public PasteBinController(BlobStorageService blobStorageService, RedisService redisService) {
        this.blobStorageService = blobStorageService;
        this.redisService = redisService;
    }

    @PostMapping("/save")
    public String saveText(@RequestBody PasteRequest request) {

        RestTemplate restTemplate = new RestTemplate();
        String externalServiceUrl = "http://localhost:8081/gen";

        ResponseEntity<String> response = restTemplate.getForEntity(externalServiceUrl, String.class);

        String fileName;
        if (response.getStatusCode().is2xxSuccessful()) {
            fileName =  response.getBody(); // Возвращаем ответ от внешнего сервиса
        } else {
            throw new RuntimeException("Failed to send data to external service");
        }
        System.out.println("\n\n" +fileName+"\n\n");
        return blobStorageService.saveTextAndGenerateLink(fileName, request.getText(), request.getLifeTime());
    }

    @GetMapping("/delete/{fileName}")
    public void deleteBlob(@PathVariable String fileName) {
        blobStorageService.deleteBlob(fileName);
        redisService.delete(fileName);
    }

    @GetMapping("/{fileName}")
    public String getBlobContent(@PathVariable String fileName) {
        String answer = redisService.get(fileName);

        if (answer == null) { // if cant find text in cache take it in blob storage
            answer = blobStorageService.getBlobContentAsText(fileName);
            redisService.save(fileName, answer);
            return answer;
        }
        return answer;
    }
}
