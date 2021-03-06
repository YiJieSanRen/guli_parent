package com.limu.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.eduservice.entity.EduCourse;
import com.limu.eduservice.entity.EduCourseDescription;
import com.limu.eduservice.entity.EduTeacher;
import com.limu.eduservice.entity.frontvo.CourseFrontVo;
import com.limu.eduservice.entity.frontvo.CourseWebVo;
import com.limu.eduservice.entity.vo.CourseInfoVo;
import com.limu.eduservice.entity.vo.CoursePublishVo;
import com.limu.eduservice.mapper.EduCourseMapper;
import com.limu.eduservice.service.EduChapterService;
import com.limu.eduservice.service.EduCourseDescriptionService;
import com.limu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limu.eduservice.service.EduVideoService;
import com.limu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author limu
 * @since 2022-02-07
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //课程描述的注入
    @Autowired
    private EduCourseDescriptionService courseDescriptionService;

    //注入小节和章节service
    @Autowired
    private EduChapterService eduChapterService;

    @Autowired
    private EduVideoService eduVideoService;

    //添加课程基本信息的方法
    @Override
    @Transactional
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);

        if (insert < 0) {
            //添加失败
            throw new GuliException(20001, "添加课程信息失败");
        }

        //获取添加之后课程id
        String cid = eduCourse.getId();

        //2.向课程简介表添加课程简介
        //edu_course_description
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfoVo.getDescription());
        //设置描述id就是课程id
        courseDescription.setId(cid);
        try {
            courseDescriptionService.save(courseDescription);
        } catch (Exception e) {
            throw new GuliException(20001, "添加课程简介失败");
        }

        return cid;
    }

    //根据课程查询课程基本信息
    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse, courseInfoVo);

        //2.查询描述表
        EduCourseDescription courseDescription = courseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(courseDescription.getDescription());

        return courseInfoVo;
    }

    //修改课程信息
    @Override
    @Transactional
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.修改课程表
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int update = baseMapper.updateById(eduCourse);
        if (update == 0) {
            throw new GuliException(20001, "修改课程信息失败");
        }

        //2.修改描述表
        EduCourseDescription description = new EduCourseDescription();
        description.setId(courseInfoVo.getId());
        description.setDescription(courseInfoVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo publishCourseInfo = baseMapper.getPublishCourseInfo(id);
        return publishCourseInfo;
    }

    //删除课程
    @Override
    @Transactional
    public void removeCourse(String courseId) {
        //1.根据课程id删除小节
        eduVideoService.removeVideoByCourseId(courseId);

        //2.根据课程id删除章节
        eduChapterService.removeChapterByCourseId(courseId);

        //3.根据课程id删除描述
        courseDescriptionService.removeById(courseId);

        //4.根据课程id删除课程本身
        int result = baseMapper.deleteById(courseId);
        if (result == 0) {
            throw new GuliException(20001, "删除失败!");
        }

    }

    //查询热门课程
    @Cacheable(value = "course", key = "'selectIndexList'")
    @Override
    public List<EduCourse> queryHotCourses() {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> courseList = baseMapper.selectList(wrapper);
        return courseList;
    }

    //条件查询带分页查询课程
    @Override
    public Map<String, Object> getCourseFrontList(Page<EduCourse> pageParam, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (courseFrontVo != null) {
            //判断条件值是否为空，不为空拼接
            if (!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) { //一级分类
                wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
                if (!StringUtils.isEmpty(courseFrontVo.getSubjectId())) { //一级分类
                    wrapper.eq("subject_id", courseFrontVo.getSubjectId());
                }
            }
            if (!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())) { //关注度
                System.out.println(courseFrontVo.getBuyCountSort());
                if ("1".equals(courseFrontVo.getBuyCountSort())) {
                    wrapper.orderByDesc("buy_count");
                } else {
                    wrapper.orderByAsc("buy_count");
                }
            }
            if (!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())) { //最新
                if ("1".equals(courseFrontVo.getGmtCreateSort())) {
                    wrapper.orderByDesc("gmt_create");
                }else {
                    wrapper.orderByAsc("gmt_create");
                }
            }
            if (!StringUtils.isEmpty(courseFrontVo.getPriceSort())) { //价格
                if ("1".equals(courseFrontVo.getPriceSort())) {
                    wrapper.orderByDesc("price");
                }else {
                    wrapper.orderByAsc("price");
                }
            }
        }
        baseMapper.selectPage(pageParam, wrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();//下一页
        boolean hasPrevious = pageParam.hasPrevious();//上一页


        //把分页数据获取出来，放到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        //map返回
        return map;
    }

    //根据课程id，编写sql语句查询课程信息
    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }
}
