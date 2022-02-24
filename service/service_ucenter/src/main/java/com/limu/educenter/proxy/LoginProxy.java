package com.limu.educenter.proxy;


import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class LoginProxy {

    @Autowired
    private RedisTemplate redisTemplate;

    @AfterReturning(returning = "login", pointcut = "execution( * com.limu.educenter.service.impl.UcenterMemberServiceImpl.login(..))")
    public void doAfterReturning(Object login) {
        if (!StringUtils.isEmpty(login)) {
            Integer loginAmount = (Integer) redisTemplate.opsForValue().get("loginAmount");
            if (loginAmount == null) {
                redisTemplate.opsForValue().set("loginAmount", 1);
            } else {
                redisTemplate.opsForValue().increment("loginAmount", 1L);
            }
        }
    }
}
