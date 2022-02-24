package com.limu.eduorder.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WxPayUtil implements InitializingBean {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.partner}")
    private String partner;

    @Value("${wx.partnerkey}")
    private String partnerkey;

    @Value("${wx.notifyurl}")
    private String notifyurl;

    public static String WX_PAY_APPID;
    public static String WX_PAY_PARTNER;
    public static String WX_PAY_PARTNERKEY;
    public static String WX_PAY_NOTIFYURL;

    @Override
    public void afterPropertiesSet() throws Exception {
        WX_PAY_APPID = appid;
        WX_PAY_PARTNER = partner;
        WX_PAY_PARTNERKEY = partnerkey;
        WX_PAY_NOTIFYURL = notifyurl;
    }
}
