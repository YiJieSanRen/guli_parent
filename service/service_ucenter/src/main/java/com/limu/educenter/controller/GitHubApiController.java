package com.limu.educenter.controller;

import com.google.gson.Gson;
import com.limu.educenter.utils.ConstantGitHubUtils;
import com.limu.educenter.utils.HttpClientUtils;
import com.limu.servicebase.exceptionhandler.GuliException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/github")
@CrossOrigin
public class GitHubApiController {

    //请求用户的 GitHub 身份
    @GetMapping("login")
    public String getGitHubCode() {
        String baseUrl = "https://github.com/login/oauth/authorize" +
                "?client_id=%s" +
                "&redirect_uri=%s" +
                "&state=%s";

        String redirectUrl = ConstantGitHubUtils.redirectUrlEncoderUtil(ConstantGitHubUtils.GITHUB_REDIRECT_URL);

        String url = String.format(
                baseUrl,
                ConstantGitHubUtils.GITHUB_CLIENT_ID,
                redirectUrl,
                "limu"
        );

        //重定向到请求微信地址
        return "redirect:" + url;

    }

    @GetMapping("callback")
    public String callback(String code, String state ) {
        try {
            //1.获取code值，临时票据，类似于验证码

            //2.拿着code请求 微信固定的地址，得到两个值 access_token 和 openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://github.com/login/oauth/access_token";
                    String requestBody = "client_id=%s" +
                    "&client_secret=%s" +
                    "&code=%s" +
                    "&redirect_uri=%s" +
                    "&state=%s";

            String redirectUrl = ConstantGitHubUtils.redirectUrlEncoderUtil(ConstantGitHubUtils.GITHUB_REDIRECT_URL);


            //拼接三个参数：id 密钥 和 code值
            String body = String.format(
                    requestBody,
                    ConstantGitHubUtils.GITHUB_CLIENT_ID,
                    ConstantGitHubUtils.GITHUB_CLIENT_SECRET,
                    code,
                    redirectUrl,
                    "limu"
            );

            //请求这个拼接好的地址，得到返回两个值 access_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.postParameters(baseAccessTokenUrl,body);

            System.out.println("accessTokenInfo:" + accessTokenInfo);

            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            //String access_token = (String) mapAccessToken.get("access_token");
            System.out.println(mapAccessToken.values());


//            String baseUserInfoUrl = "https://api.github.com/user" +
//                    "?access_token=%s";
//            //拼接两个参数
//            String userInfoUrl = String.format(
//                    baseUserInfoUrl,
//                    access_token
//            );
//            //发送请求
//            String userInfo = HttpClientUtils.get(userInfoUrl);
//            //获取返回userinfo字符串扫描人信息
//            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
//
//            System.out.println(userInfoMap);








        }catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GuliException(20001,"登录失败");
        }
        return null;
    }
}