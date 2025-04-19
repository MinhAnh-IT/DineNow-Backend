package com.vn.DineNow.services.fileService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    String uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String filename) throws IOException;

    String getPublicFileUrl(String filename);
    boolean isValidImage(MultipartFile image);
}
