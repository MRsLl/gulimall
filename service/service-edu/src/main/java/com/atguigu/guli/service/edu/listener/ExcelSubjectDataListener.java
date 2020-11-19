package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.CollectionUtils;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.ArrayList;
import java.util.List;

public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private List<ExcelSubjectData> data = new ArrayList<>();
    private SubjectMapper subjectMapper;

    public ExcelSubjectDataListener(SubjectMapper subjectMapper) {
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        data.add(excelSubjectData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        if (!CollectionUtils.isEmpty(data)) {
            for (ExcelSubjectData excelSubjectData : data) {
                //遍历数据集合，获取excel 中每行数据的一级分类和二级分类
                String levelOneTitle = excelSubjectData.getLevelOneTitle();
                String levelTwoTitle = excelSubjectData.getLevelTwoTitle();

                //按一级分类的标题从数据库中查询，若数据库中没有相应记录，则插入
                QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("title", levelOneTitle);
                queryWrapper.eq("parent_id", "0");

                Subject levelOneSubject = subjectMapper.selectOne(queryWrapper);

                if (levelOneSubject == null) {
                    levelOneSubject = new Subject();
                    levelOneSubject.setParentId("0");
                    levelOneSubject.setTitle(levelOneTitle);
                    levelOneSubject.setSort(1);

                    subjectMapper.insert(levelOneSubject);
                }

                //按二级分类的标题和父分类的id 从数据库中查询，若数据库中没有相应记录，则插入
                queryWrapper.clear();
                queryWrapper.eq("title", levelTwoTitle);
                queryWrapper.eq("parent_id", levelOneSubject.getTitle());

                Subject levelTwoSubject = subjectMapper.selectOne(queryWrapper);

                if (levelTwoSubject == null) {
                    levelTwoSubject = new Subject();
                    levelTwoSubject.setTitle(levelTwoTitle);
                    levelTwoSubject.setParentId(levelOneSubject.getId());
                    levelTwoSubject.setSort(2);

                    subjectMapper.insert(levelTwoSubject);
                }
            }
        }


    }
}
