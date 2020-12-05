package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VodMediaService vodMediaService;

    //根据课时id 删除vod 上的视频,并删除库中课时信息
    @Override
    public boolean removeVideoById(String id) {
        Video video = baseMapper.selectById(id);
        vodMediaService.delete(video.getVideoSourceId());

        return this.removeById(id);
    }

    /**
     * 获取阿里云 vod 视频 id 列表
     * @param maps
     * @return
     */
    private List<String> getVideoSourceIdList(List<Map<String, Object>> maps){
        ArrayList<String> videoSourceIdList = new ArrayList<>();

        for (Map<String, Object> map : maps) {
            videoSourceIdList.add((String)map.get("video_source_id"));
        }
        return videoSourceIdList;
    }

    /**
     * 根据课程 id 从阿里云 vod 删除视频
     * @param courseId
     */
    @Override
    public void removeMediaVideoByCourseId(String courseId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id",courseId);
        queryWrapper.select("video_source_id");

        List<Map<String, Object>> maps = baseMapper.selectMaps(queryWrapper);

        List<String> videoSourceIdList = this.getVideoSourceIdList(maps);
        vodMediaService.removeVideoByList(videoSourceIdList);
    }

    //根据chapterId 从 vod 中删除视频
    @Override
    public void removeMediaVideoByChapterId(String chapterId) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);

        //从vod 上删除课时对应视频
        List<Video> videos = baseMapper.selectList(queryWrapper);
        ArrayList<String> videoSourceIdList = new ArrayList<>();

        for (Video video : videos) {
            if (StringUtils.isNotEmpty(video.getVideoSourceId())){
                videoSourceIdList.add(video.getVideoSourceId());
            }
        }

        vodMediaService.removeVideoByList(videoSourceIdList);
    }
}
