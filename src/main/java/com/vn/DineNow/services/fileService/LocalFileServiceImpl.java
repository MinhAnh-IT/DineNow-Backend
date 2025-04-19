package com.vn.DineNow.services.fileService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class LocalFileServiceImpl implements IFileService {

    @Value("${upload.images}")
    String uploadDirectory;

    @Value("${server.address}")
    String serverAddress;

    @Value("${server.port}")
    String serverPort;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                : "";

        if (!isValidImageExtension(extension)) {
            throw new IOException("Invalid image type: " + extension);
        }

        String filename = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDirectory, filename);
        Files.write(filePath, file.getBytes());

        return filename;
    }

    @Override
    public void deleteFile(String filename) throws IOException {
        Files.deleteIfExists(Path.of(uploadDirectory, filename));
    }

    @Override
    public String getPublicFileUrl(String filename) {
        return "http://" + serverAddress + ":" + serverPort + "/uploads/" + filename;
    }

    @Override
    public boolean isValidImage(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                : "";
        return isValidImageExtension(extension);
    }

    public boolean isValidImageExtension(String ext) {
        return ext.matches("png|jpg|jpeg|gif");
    }
}
