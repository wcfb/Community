package wcfb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wcfb.base.support.RespondResult;
import wcfb.model.bo.ArticleCommentBo;
import wcfb.service.CommentService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CommentService commentService;

    /**
     * 评论文章或者评论
     *
     * @return
     */
    @RequestMapping("/article")
    public RespondResult article(HttpServletRequest request, @RequestBody ArticleCommentBo articleCommentBo) {
        return commentService.article(request, articleCommentBo);
    }
}
