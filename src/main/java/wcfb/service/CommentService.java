package wcfb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.base.support.RespondResult;
import wcfb.base.utils.UserUtil;
import wcfb.mapper.ArticleMapper;
import wcfb.mapper.CommentMapper;
import wcfb.mapper.UserdataMapper;
import wcfb.model.bo.ArticleCommentBo;
import wcfb.model.po.ArticlePo;
import wcfb.model.po.CommentPo;
import wcfb.model.po.TokenPo;
import wcfb.model.po.UserdataPo;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static wcfb.model.constant.CommonConstant.article;
import static wcfb.model.constant.CommonConstant.yes;

/**
 * @author: wcfb
 * @date: 2020/2/17
 * @version: 1.0.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserdataMapper userdataMapper;

    @Autowired
    private UserUtil userUtil;

    /**
     * 评论文章或者评论
     * @param articleCommentBo
     * @return
     */
    public RespondResult article(HttpServletRequest request, ArticleCommentBo articleCommentBo) {
        CommentPo commentPo = new CommentPo();
        if (articleCommentBo.getType() == article) {
            ArticlePo articlePo = articleMapper.selectById(articleCommentBo.getId());
            commentPo.setAuthorId(articlePo.getAuthorId());
            commentPo.setArticleId(articleCommentBo.getId());
        } else {
            CommentPo selectOneComment = commentMapper.selectOne(
                    new QueryWrapper<CommentPo>()
                            .lambda()
                            .eq(CommentPo::getId, articleCommentBo.getId()));
            commentPo.setArticleId(selectOneComment.getArticleId());
            commentPo.setAuthorId(selectOneComment.getAuthorId());
            commentPo.setCommentId(articleCommentBo.getId());
        }
        commentPo.setArticleId(articleCommentBo.getId());
        commentPo.setType(articleCommentBo.getType());
        commentPo.setNormal(yes);

        TokenPo tokenPo = userUtil.getToken(request);
        commentPo.setCommentAuthorId(tokenPo.getAccount());
        commentPo.setComment(articleCommentBo.getComment());
        Date now = new Date();
        commentPo.setCreateTime(now);
        commentPo.setUpdateTime(now);
        commentMapper.insert(commentPo);

        //添加文章的评论数
        ArticlePo articlePo = articleMapper.selectById(articleCommentBo.getId());
        articlePo.setComment(articlePo.getComment()+1);
        articleMapper.updateById(articlePo);

        //添加作者的评论数
        UserdataPo userdataPo = userdataMapper.selectByAccount(commentPo.getAuthorId());
        userdataPo.setComment(userdataPo.getComment()+1);
        userdataMapper.updateById(userdataPo);

        return RespondResult.success();
    }
}
