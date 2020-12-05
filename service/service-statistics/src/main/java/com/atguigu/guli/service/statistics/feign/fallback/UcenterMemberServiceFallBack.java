package com.atguigu.guli.service.statistics.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public R countRegisterNum(String day) {
        //错误日志
        log.warn("熔断器被执行");
        return R.ok().data("registerNum", 0);
    }
}