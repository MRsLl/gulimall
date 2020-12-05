package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    List<Map<String, Object>> selectNameListByKey(String key);

    boolean removeAvatarById(String id);

    boolean batchRemoveAvatarsByIds(List<String> ids);

    Map<String,Object> selectTeacherInfoById(String id);
}
