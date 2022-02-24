package com.limu.eduorder.client;

import com.limu.commonutils.ordervo.UcenterMemberOrder;
import com.limu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Component;

@Component
public class UcenterFileDegradeFeignClient implements UcenterClient{

    @Override
    public UcenterMemberOrder getUserInfoOrder(String id) {

        throw new GuliException(20001,"获取用户信息失败");
    }
}
