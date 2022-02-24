package com.limu.eduorder.service;

import com.limu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author limu
 * @since 2022-02-22
 */
public interface OrderService extends IService<Order> {

    //生成订单的方法
    String createOrder(String courseId, String memberIdByJwtToken);
}
