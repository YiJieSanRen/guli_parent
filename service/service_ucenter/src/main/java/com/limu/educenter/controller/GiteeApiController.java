package com.limu.educenter.controller;

import com.google.gson.Gson;
import com.limu.commonutils.JwtUtils;
import com.limu.educenter.entity.UcenterMember;
import com.limu.educenter.service.UcenterMemberService;
import com.limu.educenter.utils.ConstantGitHubUtils;
import com.limu.educenter.utils.ConstantGiteeUtils;
import com.limu.educenter.utils.ConstantWxUtils;
import com.limu.educenter.utils.HttpClientUtils;
import com.limu.servicebase.exceptionhandler.GuliException;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/gitee")
@CrossOrigin
public class GiteeApiController {

    @Autowired
    private UcenterMemberService memberService;

    //请求用户的 GitHub 身份
    @GetMapping("login")
    public String getGitHubCode() {
        String baseUrl = "https://gitee.com/oauth/authorize" +
                "?client_id=%s" +
                "&redirect_uri=%s" +
                "&response_type=code";

        String redirectUrl = ConstantGiteeUtils.GITEE_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format(
                baseUrl,
                ConstantGiteeUtils.GITEE_CLIENT_ID,
                redirectUrl
        );

        //重定向到请求微信地址
        return "redirect:" + url;

    }


    //2.获取扫描人信息，添加数据
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            //1.获取code值，临时票据，类似于验证码

            //2.拿着code请求 微信固定的地址，得到两个值 access_token 和 openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://gitee.com/oauth/token";
            String requestBody ="grant_type=authorization_code" +
                    "&code=%s" +
                    "&client_id=%s" +
                    "&redirect_uri=%s" +
                    "&client_secret=%s";

            String redirectUrl = ConstantGiteeUtils.GITEE_REDIRECT_URL;
            try {
                redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            //拼接三个参数：id 密钥 和 code值
            String body = String.format(
                    requestBody,
                    code,
                    ConstantGiteeUtils.GITEE_CLIENT_ID,
                    redirectUrl,
                    ConstantGiteeUtils.GITEE_CLIENT_SECRET
            );

            //请求这个拼接好的地址，得到返回两个值 access_token 和 openid
            //使用httpclient发送请求，得到返回结果
            String accessTokenInfo = HttpClientUtils.postParameters(baseAccessTokenUrl,body);

            System.out.println("accessTokenInfo:" + accessTokenInfo);

            //从accessTokenInfo字符串获取出来两个值 access_token 和 openid
            //把accessTokenInfo字符串转换map集合，根据map里面key获取对应值
            //使用json转换工具 Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");

            //3.获取GitUser对象
            String getUserUrl = "https://gitee.com/api/v5/user" +
                    "?access_token=" +
                    access_token;

            String userInfo = HttpClientUtils.get(getUserUrl);
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            System.out.println(userInfoMap);

            String giteeId = String.valueOf(userInfoMap.get("id"));//昵称

            UcenterMember member = memberService.getGiteeidMember(giteeId);

            if (member == null) {//member是空，表中没有相同微信数据，进行添加

                String name = (String) userInfoMap.get("name");//昵称
                String avatar_url = (String) userInfoMap.get("avatar_url");//头像


                member = new UcenterMember();
                member.setGiteeid(giteeId);
                member.setNickname(name);
                member.setAvatar(avatar_url);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            //最后返回首页面，通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+jwtToken;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GuliException(20001,"登录失败");
        }

    }

}
