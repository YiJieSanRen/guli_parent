package com.limu.msmservice.service;

import java.util.Map;

public interface Msmservice {
    //发送短信的方法
    boolean send(Map<String, Object> param, String phone);


    boolean sendTxtMail(String code, String email);
}
