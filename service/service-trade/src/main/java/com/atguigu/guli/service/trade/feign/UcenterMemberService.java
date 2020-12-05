package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.fallback.UcenterMemberServiceFallBack;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter",fallback = UcenterMemberServiceFallBack.class)
public interface UcenterMemberService {

    @ApiOperation("根据用户id 获取 memberDto")
    @GetMapping("/api/ucenter/member/getMemberDtoById/{memberId}")
    public R getMemberDtoById(@PathVariable String memberId);
}
