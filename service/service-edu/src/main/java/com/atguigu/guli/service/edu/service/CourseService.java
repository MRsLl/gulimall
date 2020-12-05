package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.query.CourseQuery;
import com.atguigu.guli.service.edu.entity.query.WebCourseQuery;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
public interface CourseService extends IService<Course> {

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    IPage<CourseVo> listCourseByPage(Integer page, Integer limit, CourseQuery courseQuery);

    boolean removeCoverById(String id);

    boolean removeCourseById(String id);

    CoursePublishVo getCoursePublishById(String id);

    boolean publishCourseById(String id);

    List<Course> selectWebCourse(WebCourseQuery webCourseQuery);

    WebCourseVo selectWebCourseVoById(String id);

    CourseDto getCourseDtoById(String courseId);
}
