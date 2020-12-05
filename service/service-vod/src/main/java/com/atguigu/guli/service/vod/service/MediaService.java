package com.atguigu.guli.service.vod.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    String upload(MultipartFile file);

    String getPlayUrl(String id);

    String getPlayAuth(String id);

    void delete(String videoSourceId);

    void removeVideoByList(List<String> videoIdList) throws ClientException;
}
