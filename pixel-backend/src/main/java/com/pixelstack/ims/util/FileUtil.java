package com.pixelstack.ims.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileUtil {

    public static String saveFile(MultipartFile file, String uploadPath) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);

        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString() + "." + extension;
        String relativePath = dateStr + "/" + fileName;

        File targetDir = new File(uploadPath + dateStr);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        File targetFile = new File(uploadPath + relativePath);
        file.transferTo(targetFile);

        return relativePath;
    }

    public static String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
}