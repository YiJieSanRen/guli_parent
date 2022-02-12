package com.limu.eduservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.limu.eduservice.entity.EduChapter;
import com.limu.eduservice.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author limu
 * @since 2022-02-07
 */
public interface EduVideoService extends IService<EduVideo> {
    //根据课程id删除小节
    void removeVideoByCourseId(String courseId);
}
