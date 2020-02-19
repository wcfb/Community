package wcfb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wcfb.base.support.RespondResult;
import wcfb.model.bo.AuthorBo;
import wcfb.service.AuthorService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: wcfb
 * @date: 2020/2/13
 * @version: 1.0.0
 */
@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    /**
     * 推荐作者
     * @return
     */
    @RequestMapping("/find")
    public RespondResult find(){
        return authorService.find();
    }

    /**
     * 关注作者
     * @return
     */
    @RequestMapping("/followAuthor")
    public RespondResult followAuthor(@RequestBody AuthorBo authorBo, HttpServletRequest request){
        return authorService.followAuthor(authorBo, request);
    }

    /**
     * 查看作者
     * @param id
     * @return
     */
    @RequestMapping("/info/{id}")
    public RespondResult info(@PathVariable("id") String id, HttpServletRequest request){
        return authorService.info(id, request);
    }

    /**
     * 查看作者收到的评论
     * @param request
     * @return
     */
    @RequestMapping("comment")
    public RespondResult comment(HttpServletRequest request){
        return authorService.comment(request);
    }

    /**
     * 查看作者收到的评论
     * @param request
     * @return
     */
    @RequestMapping("likes")
    public RespondResult likes(HttpServletRequest request){
        return authorService.likes(request);
    }

    /**
     * 查看作者收到的评论
     * @param request
     * @return
     */
    @RequestMapping("follows")
    public RespondResult follows(HttpServletRequest request){
        return authorService.follows(request);
    }
}
