package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.service.cms.entity.Ad;
import com.atguigu.guli.service.cms.mapper.AdMapper;
import com.atguigu.guli.service.cms.service.AdService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告推荐 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-27
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    /**
     * 由广告类型id 获取广告集合
     * @param adTypeId
     * @return
     */
    @Override
    @Cacheable(value = "index",key="'listByAdTypeId'")
    public List<Ad> listByAdTypeId(String adTypeId) {

        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_id",adTypeId);
        queryWrapper.orderByAsc("sort", "id");

        List<Ad> adList = baseMapper.selectList(queryWrapper);
        return adList;
    }
}
