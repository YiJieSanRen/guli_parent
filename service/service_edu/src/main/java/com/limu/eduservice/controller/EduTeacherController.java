package com.limu.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.limu.commonutils.R;
import com.limu.eduservice.entity.EduTeacher;
import com.limu.eduservice.entity.vo.TeacherQuery;
import com.limu.eduservice.service.EduTeacherService;
import com.limu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 老师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-01-19
 */
@Api(description = "老师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin
public class EduTeacherController {

    //访问地址：http://localhost:8001/eduservice/teacher/findAll
    //把service注入
    @Autowired
    private EduTeacherService teacherService;

    //1.查询老师表所有数据
    //rest风格
    @ApiOperation(value = "所有老师列表")
    @GetMapping("findAll")
    public R findAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("items", list);
    }

    //2.逻辑删除老师的方法
    @ApiOperation(value = "逻辑删除老师")
    @DeleteMapping("{id}")
    public R removeTeacher(@ApiParam(name = "id", value = "老师ID", required = true) @PathVariable String id) {
        boolean flag = teacherService.removeById(id);

        if (flag) {
            return R.ok();
        } else {
            return R.error();
        }
    }

    /**
     * 3.分页查询老师的方法
     * @param current 当前页
     * @param limit 每页记录数
     * @return
     */
    @ApiOperation(value = "分页查询老师")
    @GetMapping("pageTeacher/{current}/{limit}")
    public R pageListTeacher(@PathVariable long current,@PathVariable long limit) {

        try {
            int i = 10 / 0;
        } catch (Exception e) {
            throw new GuliException(20001,"执行自定义异常处理...");
        }

        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有的数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher,null);

        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //每页数据list集合

        return R.ok().data("total",total).data("rows",records);
    }

    //4.条件查询带分页的方法
    @ApiOperation(value = "多条件组合查询老师带分页")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public R pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false) TeacherQuery teacherQuery) {
        //创建page对象
        Page<EduTeacher> pageTeacher = new Page<>(current, limit);

        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        //多条件组合查询
        //判断条件值是否为空，如果不为空拼接条件
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //判断条件值是否为空，如果不为空拼接条件
        if(!StringUtils.isEmpty(name)){
            //构建条件
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)){
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)){
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)){
            wrapper.le("gmt_create",end);
        }

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现条件查询分页
        teacherService.page(pageTeacher,wrapper);
        long total = pageTeacher.getTotal();//总记录数
        List<EduTeacher> records = pageTeacher.getRecords(); //每页数据list集合
        return R.ok().data("total",total).data("rows",records);
    }

    //添加老师接口的方法
    @ApiOperation(value = "添加老师")
    @PostMapping("addTeacher")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        boolean save = teacherService.save(eduTeacher);
        if(save) {
            return R.ok();
        }else {
            return R.error();
        }
    }

    //根据老师id进行查询
    @ApiOperation(value = "根据ID查询老师")
    @GetMapping("getTeacher/{id}")
    public R getTeacher(@PathVariable String id) {
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("teacher",eduTeacher);
    }

    //老师修改功能
    @ApiOperation(value = "根据ID修改老师")
    @PostMapping("updateTeacher")
    public R updateTeacher(@ApiParam(name = "teacher", value = "讲师对象", required = true) @RequestBody EduTeacher eduTeacher) {
        boolean flag = teacherService.updateById(eduTeacher);
        if (flag) {
            return R.ok();
        }else {
            return R.error();
        }
    }
}

