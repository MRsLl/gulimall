package com.atguigu.guli.service.base.dto;

import lombok.Data;

import java.math.BigDecimal;

//订单中的会员信息，用于在远程调用中传输
@Data
public class MemberDto {

    private String id;//会员id
    private String mobile;//手机号
    private String nickname;//昵称
}
