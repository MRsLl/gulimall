package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
public interface TeacherService extends IService<Teacher> {

    IPage<Teacher> selectPage(Long page, Long limit, TeacherQuery teacherQuery);
}
