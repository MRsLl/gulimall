package com.atguigu.guli.service.oss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface FileService {
    String fileUpload(String module, MultipartFile file) throws IOException;

    void removeFile(String url);

    void batchRemove(List<String> urls);
}
