package com.limu.msmservice.controller;

import com.limu.commonutils.R;
import com.limu.msmservice.service.Msmservice;
import com.limu.msmservice.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/msm")
@CrossOrigin
public class MsmController {

    @Autowired
    private Msmservice msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    //发送短信的方法
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        //1.从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //2.如果redis获取不到,进行阿里云发送

        //生成随机值，传递阿里云进行发送
        code = RandomUtil.getSixBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        //调用service发送短信的方法
        boolean isSend = msmService.send(param,phone);
        if (isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }

    //发送邮件的方法
    /**
     * 发送包含简单文本的邮件
     */
    @GetMapping("sendTxtMail/{email}")
    public R sendTxtMail(@PathVariable String email) {
        //1.从redis获取验证码，如果获取到直接返回
        String code = redisTemplate.opsForValue().get(email);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }

        //2.如果redis获取不到,进行邮件发送

        //生成随机值，进行发送
        code = RandomUtil.getSixBitRandom();

        boolean isSend = msmService.sendTxtMail(code,email);
        if (isSend) {
            //发送成功，把发送成功验证码放到redis里面
            //设置有效时间
            redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }

}
