package com.limu.educenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class ConstantGitHubUtils implements InitializingBean {

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.redirect_url}")
    private String redirectUrl;

    public static String GITHUB_CLIENT_ID;
    public static String GITHUB_CLIENT_SECRET;
    public static String GITHUB_REDIRECT_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        GITHUB_CLIENT_ID = clientId;
        GITHUB_CLIENT_SECRET = clientSecret;
        GITHUB_REDIRECT_URL = redirectUrl;
    }

    public static String redirectUrlEncoderUtil(String redirectUrl){
        return urlEncode(redirectUrl);
    }

    private static String urlEncode(String url){
        try {
            url = URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
