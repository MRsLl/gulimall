package com.atguigu.guli.service.cms.controller.admin;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.service.AdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 广告推荐 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2020-11-27
 */
@Api(tags = "管理员广告管理")
@RestController

@RequestMapping("/admin/cms/ad")
public class AdminAdController {

    @Autowired
    private AdService adService;

    @ApiOperation("新增广告")
    @PostMapping("save")
    public R save(@RequestBody Ad ad){
        boolean b = adService.save(ad);

        if (b) {
            return R.ok().message("新增广告成功");
        } else {
            return R.error().message("新增广告失败");
        }
    }
}

