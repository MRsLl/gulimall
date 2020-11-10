package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.query.TeacherQuery;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Override
    public IPage<Teacher> selectPage(Long page, Long limit, TeacherQuery teacherQuery) {

        //根据当前页码和每页记录数创建分页对象1
        Page<Teacher> pageParam = new Page<>(page,limit);
        //若查询条件为空则返回根据分页对象1和空条件查出的分页对象
        if (teacherQuery == null) {
            return baseMapper.selectPage(pageParam,null);
        }

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();

        //获取查询条件对象中的各个条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String joinDateBegin = teacherQuery.getJoinDateBegin();
        String joinDateEnd = teacherQuery.getJoinDateEnd();

        //把获取的条件添加到条件构造器 QueryWrapper 中
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.likeRight("name",name);
        }
        if (level != null) {
            queryWrapper.eq("level",level);
        }
        if (StringUtils.isNotEmpty(joinDateBegin)) {
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if (StringUtils.isNotEmpty(joinDateEnd)) {
            queryWrapper.le("join_date",joinDateEnd);
        }
        //根据分页对象和查询条件构造器分页查询并返回新分页对象
        return baseMapper.selectPage(pageParam,queryWrapper);
    }
}
