package com.vn.DineNow.services.common.fileService;

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

/**
 * Implementation of FileService that stores files locally on the server.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class LocalFileServiceImpl implements FileService {

    @Value("${upload.images}")
    String uploadDirectory;

    @Value("${server.address}")
    String serverAddress;

    @Value("${server.port}")
    String serverPort;

    /**
     * Uploads an image file to the server's local filesystem.
     *
     * @param file the multipart image file
     * @return the unique filename stored
     * @throws IOException if file type is invalid or writing fails
     */
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Uploaded file is empty or null.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IOException("Invalid file name.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!isValidImageExtension(extension)) {
            throw new IOException("Invalid image type: " + extension);
        }

        Files.createDirectories(Paths.get(uploadDirectory)); 
        String filename = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDirectory, filename);
        Files.write(filePath, file.getBytes());

        return filename;
    }


    /**
     * Deletes a file from the server if it exists.
     *
     * @param filename the file to delete
     * @throws IOException if deletion fails
     */
    @Override
    public void deleteFile(String filename) throws IOException {
        Files.deleteIfExists(Path.of(uploadDirectory, filename));
    }

    /**
     * Returns a public URL to access the uploaded file.
     *
     * @param filename the stored file name
     * @return the full public URL to access the image
     */
    @Override
    public String getPublicFileUrl(String filename) {
        // Build public URL assuming a static file server maps /uploads
        return "http://" + serverAddress + ":" + serverPort + "/uploads/" + filename;
    }

    /**
     * Checks whether a file is a valid image based on its extension.
     *
     * @param image the uploaded file
     * @return true if valid image, false otherwise
     */
    @Override
    public boolean isValidImage(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();

        // Extract extension from original filename
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                : "";

        return isValidImageExtension(extension);
    }

    /**
     * Validates if the given file extension is an accepted image format.
     *
     * @param ext the file extension
     * @return true if the extension is one of the accepted image formats
     */
    public boolean isValidImageExtension(String ext) {
        return ext.matches("png|jpg|jpeg|gif");
    }
}
