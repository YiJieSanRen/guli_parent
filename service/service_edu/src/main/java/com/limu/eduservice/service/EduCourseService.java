package com.limu.eduservice.service;

import com.limu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.eduservice.entity.vo.CourseInfoVo;
import com.limu.eduservice.entity.vo.CoursePublishVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author limu
 * @since 2022-02-07
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程基本信息的方法
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程查询课程基本信息
    CourseInfoVo getCourseInfo(String courseId);

    //修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程确认信息
    CoursePublishVo publishCourseInfo(String id);

    //删除课程
    void removeCourse(String courseId);

    //查询热门课程
    List<EduCourse> queryHotCourses();
}
