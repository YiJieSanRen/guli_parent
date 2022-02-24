package com.limu.staservice.schedule;

import com.limu.staservice.service.StatisticsDailyService;
import com.limu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    //0/5 * * * * ? 表示每隔5秒执行一次这个方法
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void task1(){
//        System.out.println("*********++++++++++++*****执行了");
//    }

    //在每天凌晨1点，查询前一天数据并进行添加
    @Scheduled(cron = "0 0 1 * * ? ")
    public void task() {
        staService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
