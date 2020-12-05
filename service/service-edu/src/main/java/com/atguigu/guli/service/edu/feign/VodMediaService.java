package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.fallback.VodMediaServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-vod", fallback = VodMediaServiceFallBack.class)
public interface VodMediaService {

    //vod 按id 删除视频
    @DeleteMapping("/admin/vod/media/delete/{videoSourceId}")
    R delete(@PathVariable String videoSourceId);

    //根据视频id 集合 从 vod 删除视频
    @DeleteMapping("/admin/vod/media/removeVideoByList")
    R removeVideoByList(@RequestBody List<String> videoSourceIdList);
}
