package com.limu.eduorder.client;


import com.limu.commonutils.ordervo.CourseWebVoOrder;
import com.limu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class EduFileDegradeFeignClient implements EduClient{

    @Override
    public CourseWebVoOrder getCourseInfoOrder(String id) {
        throw new GuliException(20001,"查询课程信息失败");
    }
}
