package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private OssFileService ossFileService;

    @Resource
    private ChapterMapper chapterMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private CourseCollectMapper courseCollectMapper;

    @Resource
    private CourseDescriptionMapper courseDescriptionMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        //保存课程
        System.out.println("courseInfoForm id: " + courseInfoForm.getId());
        Course course = new Course();
        System.out.println("course.getId() = " + course.getId());
        BeanUtils.copyProperties(courseInfoForm,course);
        System.out.println("course.getId() = " + course.getId());
        baseMapper.insert(course);

        //保存课程详情,id 与课程id 相等
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());

        courseDescriptionMapper.insert(courseDescription);

        return course.getId();
    }

    //按条件分页查询课程集合
    @Override
    public IPage<CourseVo> listCourseByPage(Integer page, Integer limit, CourseQuery courseQuery) {
        //根据当前页码和每页最大记录数创建分页对象
        QueryWrapper<CourseVo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("t1.publish_time");

        String title = courseQuery.getTitle();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();
        String teacherId = courseQuery.getTeacherId();

        if (StringUtils.isNotEmpty(title)) {
            queryWrapper.like("t1.title",title);
        }
        if (StringUtils.isNotEmpty(subjectParentId)) {
            queryWrapper.eq("t1.subject_parent_id",subjectParentId);
        }
        if (StringUtils.isNotEmpty(subjectId)) {
            queryWrapper.eq("t1.subject_id",subjectId);
        }
        if (StringUtils.isNotEmpty(teacherId)) {
            queryWrapper.eq("t1.teacher_id",teacherId);
        }

        Page<CourseVo> pageParam = new Page<>(page,limit);
        //放入分页参数和查询条件参数，mp会自动组装
        List<CourseVo> records = baseMapper.listCourseByPage(pageParam, queryWrapper);
        pageParam.setRecords(records);
        return pageParam;
    }

    //根据id 删除课程封面
    @Override
    public boolean removeCoverById(String id) {
        Course course = baseMapper.selectById(id);
        if (course != null) {
            String cover = course.getCover();
            if (StringUtils.isNotEmpty(cover)) {
                //调用远程oss 服务删除封面
                R r = ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    //根据id 删除课程视频
    @Override
    public boolean removeVideoById(String id) {
        return false;
    }

    @Override
    @Transactional
    public boolean removeCourseById(String id) {

        //收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper = new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id", id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //删除章节：chapter
        QueryWrapper<Chapter> chapterUpdateWrapper = new QueryWrapper<>();
        chapterUpdateWrapper.eq("course_id",id);
        chapterMapper.delete(chapterUpdateWrapper);

        //删除课程描述:course_description
        courseDescriptionMapper.deleteById(id);

        //返回删除课程信息结果：course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        return baseMapper.getCoursePublishById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course = this.getById(id);
        course.setPublishTime(new Date());
        course.setStatus(Course.COURSE_NORMAL);

        return this.updateById(course);
    }
}
