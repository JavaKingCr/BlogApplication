package com.peng.controller;



import com.peng.aspect.MyLog;
import com.peng.entity.Blog;
import com.peng.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class IndexController {

    @Autowired
    private IBlogService iBlogService;
    @Autowired
    private ICacheService iCacheService;

    @MyLog
    @GetMapping("/")
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer pageNum, @RequestParam(required = false, value = "title") String title, Model model) {
        long l1 = System.currentTimeMillis();
        model.addAttribute("page", iCacheService.getIndexPage(title, pageNum));
        System.out.println("查询数据操作1耗时"+"\t"+(System.currentTimeMillis()-l1));
        long l2 = System.currentTimeMillis();
        model.addAttribute("types", iCacheService.getIndexTypes());
        System.out.println("查询数据操作2耗时"+"\t"+(System.currentTimeMillis()-l2));
        model.addAttribute("tags", iCacheService.getIndexTags());

        long l3 = System.currentTimeMillis();
        model.addAttribute("blogsCount", iCacheService.getPushedBlogNum());
        System.out.println("查询数据操作3耗时"+"\t"+(System.currentTimeMillis()-l3));

        long l4 = System.currentTimeMillis();
        model.addAttribute("typesCount", iCacheService.getTypeNum());
        System.out.println("查询数据操作4耗时"+"\t"+(System.currentTimeMillis()-l4));

        long l5 = System.currentTimeMillis();
        model.addAttribute("tagsCount", iCacheService.getTagNum());
        System.out.println("查询数据操作5耗时"+"\t"+(System.currentTimeMillis()-l5));

        long l6 = System.currentTimeMillis();
        model.addAttribute("commentsCount", iCacheService.getCommentNum());
        System.out.println("查询数据操作6耗时"+"\t"+(System.currentTimeMillis()-l6));

        long l7 = System.currentTimeMillis();
        model.addAttribute("user", iCacheService.getAdminInfo());
        System.out.println("查询数据操作7耗时"+"\t"+(System.currentTimeMillis()-l7));
        return "index";
    }


    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @MyLog
    @GetMapping("/blog/{blId}")
    public String blog(@PathVariable Long blId, Model model) {
        Blog blog = iBlogService.findFullById(blId);
        iBlogService.addViews(blId);
        if (!blog.getPublished()) {
            throw new RuntimeException("无效资源！");
        }
        model.addAttribute("blog", blog);
        model.addAttribute("user", iCacheService.getAdminInfo());
        return "blog";
    }


}
