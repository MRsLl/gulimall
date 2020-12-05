package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.cms.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-27
 */
public interface AdService extends IService<Ad> {

    List<Ad> listByAdTypeId(String adTypeId);
}
