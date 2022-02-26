package com.limu.eduservice.controller;


import com.limu.commonutils.R;
import com.limu.eduservice.entity.EduChapter;
import com.limu.eduservice.entity.chapter.ChapterVo;
import com.limu.eduservice.service.EduChapterService;
import com.limu.servicebase.exceptionhandler.GuliException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author limu
 * @since 2022-02-07
 */
@Api(description = "章节管理")
@RestController
@RequestMapping("/eduservice/chapter")
//@CrossOrigin
public class EduChapterController {

    @Autowired
    private EduChapterService chapterService;

    //课程大纲列表,根据课程id进行查询
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCoureId(courseId);
        return R.ok().data("allChapterVideo",list);
    }

    //添加章节
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        if (null != eduChapter.getTitle() && eduChapter.getTitle()!="") {
            chapterService.save(eduChapter);
            return R.ok();
        }else {
            throw new GuliException(20001,"添加章节不能为空!");
        }
    }

    //根据章节id查询
    @GetMapping("getChapterInfo/{chapterId}")
    public R getChapterInfo(@PathVariable String chapterId){
        EduChapter eduChapter = chapterService.getById(chapterId);
        return R.ok().data("chapter",eduChapter);
    }

    //修改章节
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除的方法
    @DeleteMapping("{chapterId}")
    public R deleteChapter(@PathVariable String chapterId){
        boolean flag = chapterService.deleteChapter(chapterId);
        if (flag) return R.ok();
        return R.error();
    }
}

