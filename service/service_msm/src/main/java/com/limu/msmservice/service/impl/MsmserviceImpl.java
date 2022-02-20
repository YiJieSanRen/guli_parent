package com.limu.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.limu.msmservice.service.Msmservice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmserviceImpl implements Msmservice {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    //发送短信的方法
    @Override
    public boolean send(Map<String, Object> param, String phone) {
        if (StringUtils.isEmpty(phone)) return false;

        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI5tSTF89SKi9TFW1mftLx", "ISt0yDovW87JWGfhy8r6Nn9WaHPOmG");
        IAcsClient client = new DefaultAcsClient(profile);

        //设置相关参数
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        //设置发送相关的参数
        request.putQueryParameter("phoneNumbers", phone); //手机号
        request.putQueryParameter("SignName", "发送短信"); //申请阿里云 签名名称
        request.putQueryParameter("TemplateCode", "3d60a058896f4ca180c33541be19f9a4"); //申请阿里云 模板code
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param)); //验证码数据，转换json数据传递

        //最终的发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        } catch (ClientException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean sendTxtMail(String code, String email) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setSubject("散人学苑账户激活邮箱验证");//主题

            mailMessage.setText("<html>" +
                            "<body>" +
                            "您好，感谢您在散人学苑注册账户！" +
                            "<BR>" +
                            "<BR>" +
                            "您的账户激活邮箱验证码为：" + code + "，5分钟内有效！" +
                            "<BR>" +
                            "<BR>" +
                            "如果不是本人操作，请忽略。" +
                            "<BR>" +
                            "<BR>" +
                            "<BR>" +
                            "--" +
                            "<BR>" +
                            "散人学苑(www.yijiesanren.icu) - 最好的学习平台" +
                            "<BR>" +
                            "<BR>" +
                            "只要学不死，就往死里学！" +
                            "</body>" +
                            "</html>");//内容

            mailMessage.setTo(email);//发给谁

            mailMessage.setFrom(from);//你自己的邮箱

            mailSender.send(mailMessage);//发送
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
