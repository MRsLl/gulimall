package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Resource
    private VideoMapper videoMapper;
    @Autowired
    private VodMediaService vodMediaService;

    //查询嵌套章节集合
    @Override
    public List<ChapterVo> getChapterList(String courseId) {
        return baseMapper.getChapterList(courseId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {

        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",id);

        //从数据库中删除id 对应章节的所有课时
        videoMapper.delete(queryWrapper);

        return this.removeById(id);
    }


}
