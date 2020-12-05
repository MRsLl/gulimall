package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {

    @Override
    public R delete(String videoSourceId) {
        log.warn("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByList(List<String> videoIdList) {
        log.warn("熔断保护");
        return R.error();
    }
}