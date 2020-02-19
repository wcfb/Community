package wcfb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wcfb.base.support.RespondResult;
import wcfb.model.bo.ArticleBo;
import wcfb.service.ArticleService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleService articleService;

    /**
     * 推荐文章
     * 每次推荐10篇文章
     * @return
     */
    @RequestMapping("/find")
    public RespondResult find(){
        return articleService.find();
    }


    /**
     * 推荐文章
     * 每次推荐10篇文章
     * @return
     */
    @RequestMapping("/follow")
    public RespondResult follow(HttpServletRequest request){
        return articleService.follow(request);
    }

    /**
     * 发表文章
     * @param articleBo
     * @return
     */
    @RequestMapping("/publishArticle")
    public RespondResult publishArticle(@RequestBody ArticleBo articleBo, HttpServletRequest request){
        return articleService.publishArticle(articleBo, request);
    }

    /**
     * 查看文章
     * @param id
     * @return
     */
    @RequestMapping("/info/{id}")
    public RespondResult info(@PathVariable("id") String id){
        return articleService.info(id);
    }

    /**
     * 查看文章的评论
     * @param id
     * @return
     */
    @RequestMapping("/comments/{id}")
    public RespondResult comment(@PathVariable("id") String id){
        return articleService.comment(id);
    }

    /**
     * 喜欢文章
     * @param id
     * @return
     */
    @RequestMapping("/like/{id}")
    public RespondResult like(@PathVariable("id") String id, HttpServletRequest request){
        return articleService.like(id, request);
    }

    /**
     * 喜欢文章
     * @param id
     * @return
     */
    @RequestMapping("/aside/{id}")
    public RespondResult aside(@PathVariable("id") String id){
        return articleService.aside(id);
    }

    /**
     * 搜索文章
     * @param title
     * @return
     */
    @RequestMapping("/search/{title}")
    public RespondResult search(@PathVariable("title") String title){
        return articleService.search(title);
    }
}
