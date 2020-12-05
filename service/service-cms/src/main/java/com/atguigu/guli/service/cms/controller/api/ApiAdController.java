package com.atguigu.guli.service.cms.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-27
 */
@Api(tags = "用户广告管理")
@RestController

@RequestMapping("/api/cms/ad")
public class ApiAdController {

    @Autowired
    private AdService adService;


    @ApiOperation("由广告类型id 获取广告集合")
    @GetMapping("listByAdTypeId/{adTypeId}")
    public R listByAdTypeId(@PathVariable String adTypeId){
        List<Ad> adList = adService.listByAdTypeId(adTypeId);

        return R.ok().data("items",adList);
    }

}

