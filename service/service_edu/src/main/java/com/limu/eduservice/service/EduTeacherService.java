package com.limu.eduservice.service;

import com.limu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-01-19
 */
public interface EduTeacherService extends IService<EduTeacher> {

    //查询名师
    List<EduTeacher> queryHotTeacher();
}
