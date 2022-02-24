package com.limu.eduservice.mapper;

import com.limu.eduservice.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limu.eduservice.entity.frontvo.CourseWebVo;
import com.limu.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author limu
 * @since 2022-02-07
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    //根据课程id查询课程确认信息
    CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id查询课程基本信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
