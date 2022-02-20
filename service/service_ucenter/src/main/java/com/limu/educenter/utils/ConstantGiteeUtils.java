package com.limu.educenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantGiteeUtils implements InitializingBean {

    @Value("${gitee.client_id}")
    private String clientId;

    @Value("${gitee.client_secret}")
    private String clientSecret;

    @Value("${gitee.redirect_url}")
    private String redirectUrl;

    public static String GITEE_CLIENT_ID;
    public static String GITEE_CLIENT_SECRET;
    public static String GITEE_REDIRECT_URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        GITEE_CLIENT_ID = clientId;
        GITEE_CLIENT_SECRET = clientSecret;
        GITEE_REDIRECT_URL = redirectUrl;
    }
}
