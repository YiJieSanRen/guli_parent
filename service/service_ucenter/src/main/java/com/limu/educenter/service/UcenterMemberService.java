package com.limu.educenter.service;

import com.limu.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.limu.educenter.entity.vo.LoginVo;
import com.limu.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author limu
 * @since 2022-02-17
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    //登录的方法
    String login(LoginVo loginVo);

    //注册的方法
    void register(RegisterVo registerVo);

    //根据openid判断
    UcenterMember getOpenidMember(String openid);

    //根据giteeid判断
    UcenterMember getGiteeidMember(String giteeId);

    //查询某天的注册人数
    Integer countRegisterDay(String day);
}
