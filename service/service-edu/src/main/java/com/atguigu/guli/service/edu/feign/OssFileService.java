package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.OssFileServiceFallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-oss",fallback = OssFileServiceFallBack.class)
public interface OssFileService {

    //oss 删除单个图片远程方法
    @DeleteMapping("/admin/oss/file/remove")
    R removeFile(@RequestBody String url);

    //oss 批量删除图片远程方法
    @DeleteMapping("/admin/oss/file/batchRemove")
    R batchRemoveFile(@RequestBody List<String> urls);
}
