package com.atguigu.guli.service.edu.feign.fallback;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OssFileServiceFallBack implements OssFileService {
    @Override
    public R removeFile(String url) {
        log.warn("在删除URL:" + url + "时发生降级保护");
        return R.error().message("删除文件失败");
    }

    @Override
    public R batchRemoveFile(List<String> urls) {
        return R.error();
    }
}
