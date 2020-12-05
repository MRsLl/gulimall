package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.feign.UcenterMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterMemberServiceFallBack implements UcenterMemberService {
    @Override
    public R getMemberDtoById(String memberId) {
        log.warn("熔断保护");
        return R.error();
    }
}