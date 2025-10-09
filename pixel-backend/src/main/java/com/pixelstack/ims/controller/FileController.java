package com.pixelstack.ims.controller;

import com.pixelstack.ims.util.FileUtil;
import com.pixelstack.ims.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @PostMapping("/upload")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String fileName = FileUtil.saveFile(file, uploadPath);
            String fileUrl = urlPrefix + fileName;

            Map<String, String> result = new HashMap<>();
            result.put("fileName", fileName);
            result.put("fileUrl", fileUrl);
            result.put("originalName", file.getOriginalFilename());

            return Result.success(result);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }
    }

    @GetMapping("/**")
    public void downloadFile(HttpServletResponse response, String fileName) {
        try {
            String requestPath = fileName.substring("/files/".length());
            File file = new File(uploadPath + requestPath);

            if (!file.exists()) {
                response.setStatus(404);
                return;
            }

            String contentType = getContentType(file.getName());
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));

            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            }
        } catch (IOException e) {
            log.error("文件下载失败", e);
            response.setStatus(500);
        }
    }

    private String getContentType(String fileName) {
        String extension = FileUtil.getFileExtension(fileName).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "webp":
                return "image/webp";
            case "pdf":
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }
}