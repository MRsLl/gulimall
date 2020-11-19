package com.atguigu.guli.service.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.CollectionUtils;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.listener.ExcelSubjectDataListener;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2020-11-10
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {


    @Override
    public void batchImport(InputStream inputStream) {
        ExcelSubjectDataListener listener = new ExcelSubjectDataListener(baseMapper);
        EasyExcel.read(inputStream, ExcelSubjectData.class,listener).excelType(ExcelTypeEnum.XLS).sheet().doRead();
    }

    @Override
    public List<SubjectVo> nestedList() {
        //mybatis/mp 逆向生成的 mapper只能对单表进行CRUD的操作
        //如果需要查询多表的数据
        //方式1：将多表的数据查询到后在java代码中通过业务封装在一起
        //如果是自关联表的数据需要业务封装，可以在代码中通过逻辑来实现
        List<Subject> subjects = baseMapper.selectList(null);
        Map<String, SubjectVo> levelOneSubjectVos = new HashMap<>();

        if (!CollectionUtils.isEmpty(subjects)) {
            //遍历所有分类集合，将所有一级分类存入map 集合中，key 为分类的 id
            for (Subject subject : subjects) {
                if ("0".equals(subject.getParentId())) {
                    SubjectVo subjectVo = new SubjectVo();
                    //将subject 中相应属性对拷给 subjectVo
                    BeanUtils.copyProperties(subject,subjectVo);
                    levelOneSubjectVos.put(subjectVo.getId(),subjectVo);
                }
            }

            //遍历所有分类集合，将所有二级分类存入一级分类的 children 中
            for (Subject subject : subjects) {
                SubjectVo levelOneSubjectVo = levelOneSubjectVos.get(subject.getParentId());

                if (levelOneSubjectVo != null) {
                    SubjectVo subjectVo = new SubjectVo();
                    BeanUtils.copyProperties(subject,subjectVo);
//                    List<SubjectVo> children = levelOneSubjectVo.getChildren();
//                    children.add(subjectVo);
//                    levelOneSubjectVo.setChildren(children);
                    levelOneSubjectVo.getChildren().add(subjectVo);
//                    System.out.println("vo 中" + levelOneSubjectVo.getChildren());
                }
            }
        }

        List<SubjectVo> levelOneSubjectList = new ArrayList<>(levelOneSubjectVos.values());

        return levelOneSubjectList;
}
}
