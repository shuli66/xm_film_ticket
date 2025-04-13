package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.common.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    // 修改为配置项，不再硬编码
    @Value("${file.upload-dir:/usr/share/nginx/files/}")
    private String uploadPath;

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        try {
            if (!FileUtil.isDirectory(uploadPath)) {
                FileUtil.mkdir(uploadPath);
            }
            fileName = System.currentTimeMillis() + "-" + fileName;
            String realFilePath = uploadPath + fileName;
            FileUtil.writeBytes(file.getBytes(), realFilePath);
        } catch (Exception e) {
            log.error(fileName + "--文件上传失败", e);
        }
        // 始终返回相对路径，让前端自动拼接环境URL
        String url = "/files/download/" + fileName;
        return Result.success(url);
    }

    /**
     * 获取文件 - 仅在开发环境使用，生产环境由Nginx直接提供文件
     */
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        OutputStream os;
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(uploadPath + fileName);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            log.warn("文件下载失败：" + fileName);
        }
    }
}