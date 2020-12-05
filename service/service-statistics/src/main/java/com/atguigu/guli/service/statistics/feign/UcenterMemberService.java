package com.atguigu.guli.service.statistics.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.statistics.feign.fallback.UcenterMemberServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter" ,fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @GetMapping("/api/ucenter/member/countRegisterNum/{day}")
    R countRegisterNum(@PathVariable String day);
}
