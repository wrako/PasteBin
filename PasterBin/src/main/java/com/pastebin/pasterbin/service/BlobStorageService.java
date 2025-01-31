package com.pastebin.pasterbin.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.pastebin.pasterbin.entity.Paste;
import com.pastebin.pasterbin.repo.PasteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;


@Service
public class BlobStorageService {

    private final BlobServiceClient blobServiceClient;
    private final PasteRepository pasteRepository;

    @Value("${AZURE_STORAGE_CONTAINER_NAME}") String containerName;

    public BlobStorageService(BlobServiceClient blobServiceClient, PasteRepository pasteRepository) {
        this.blobServiceClient = blobServiceClient;
        this.pasteRepository = pasteRepository;
    }

    public String saveTextAndGenerateLink(String blobName, String text, String lifeTimeOfPaster) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            try (InputStream dataStream = new ByteArrayInputStream(text.getBytes())) {
                blobClient.upload(dataStream, text.length(), true);
            }

            long lifeTimeMinutes = Long.parseLong(lifeTimeOfPaster);
            LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(lifeTimeMinutes);

            Paste paste = new Paste(blobName, blobClient.getBlobUrl(), expirationTime);
            pasteRepository.save(paste);

            return "http://localhost:8080/api/" + blobName;
        } catch (Exception e) {
            throw new RuntimeException("Error saving text to Azure Blob Storage", e);
        }
    }

    public String getBlobContentAsText(String blobName) {
        try {
            String blobUrl = pasteRepository.findByTitle(blobName).getFirst().getBlobUrl();
            BlobClient blobClient = new BlobClientBuilder().endpoint(blobUrl).buildClient();

            if (!blobClient.exists()) {
//                throw new RuntimeException("Blob with name " + blobName + " dose not exist.");
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            blobClient.downloadStream(outputStream);

            return outputStream.toString("UTF-8"); // Укажите нужную кодировку, например, UTF-8
        } catch (Exception e) {
            throw new RuntimeException("Failed to get blob: " + blobName, e);
        }

    }

    public void deleteBlob(String blobName) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = blobContainerClient.getBlobClient(blobName);
            blobClient.delete();
            Paste paste = pasteRepository.findByTitle(blobName).getFirst();
            pasteRepository.delete(paste);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete blob: " + blobName, e);
        }
    }


}
