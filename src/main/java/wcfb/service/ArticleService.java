package wcfb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.base.support.RespondResult;
import wcfb.base.utils.ArticleUtil;
import wcfb.base.utils.FileUtil;
import wcfb.base.utils.UserUtil;
import wcfb.mapper.*;
import wcfb.model.bo.ArticleBo;
import wcfb.model.bo.ArticleCommentBo;
import wcfb.model.bo.ArticleFindBo;
import wcfb.model.bo.FindBo;
import wcfb.model.constant.ArticleConstant;
import wcfb.model.po.*;
import wcfb.model.vo.ArticleAsideVo;
import wcfb.model.vo.ArticleCommentVo;
import wcfb.model.vo.ArticleDataVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static wcfb.model.constant.ArticleConstant.*;
import static wcfb.model.constant.ConfigConstant.textUrl;

@Service
public class ArticleService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleDataMapper articleDataMapper;

    @Autowired
    private UserdataMapper userdataMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ArticleUtil articleUtil;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikesMapper likesMapper;

    /**
     * 根据id查询文章封面
     *
     * @param articleFindBo
     * @return
     */
    public RespondResult find(ArticleFindBo articleFindBo) {
        ArticlePo articlePo = articleMapper.selectById(articleFindBo.getId());
        return RespondResult.success(articlePo);
    }

    /**
     * 文章推荐每次推荐10篇文章
     *
     * @return
     */
    public RespondResult find() {
        int current = new Random().nextInt(getPage());
        IPage<ArticlePo> articlePoIPage = new Page<>(current + 1, articleNum);
        Wrapper<ArticlePo> queryWrapper = new QueryWrapper<ArticlePo>()
                .lambda()
                .orderByDesc(ArticlePo::getId);
        articleMapper.selectPage(articlePoIPage, queryWrapper);
        List<ArticlePo> list = articlePoIPage.getRecords();
        return RespondResult.success(list);
    }

    /**
     * 获取关注的博主10篇文章
     *
     * @return
     */
    public RespondResult follow(HttpServletRequest request) {
        TokenPo tokenPo = userUtil.getToken(request);
        List<FansPo> fansPoList = fansMapper.selectList(
                new QueryWrapper<FansPo>()
                        .lambda()
                        .eq(FansPo::getFansId, tokenPo.getAccount()));

        if (fansPoList.isEmpty()) {
            return RespondResult.success();
        }

        //得到关注的所有用户
        List<String> authorAccountList = fansPoList
                .stream().map(item -> item.getAuthorId()).collect(Collectors.toList());
        int current = new Random().nextInt(getPage());
        IPage<ArticlePo> articlePoIPage = new Page<>(current + 1, articleNum);
        Wrapper<ArticlePo> queryWrapper = new QueryWrapper<ArticlePo>()
                .lambda()
                .in(ArticlePo::getAuthorId, authorAccountList)
                .orderByDesc(ArticlePo::getId);
        articleMapper.selectPage(articlePoIPage, queryWrapper);
        List<ArticlePo> list = articlePoIPage.getRecords();
        return RespondResult.success(list);
    }

    /**
     * 发表文章(如果文章中包含图片，则默认第一张图片为封面图)
     *
     * @param articleBo
     * @param request
     * @return
     */
    public RespondResult publishArticle(ArticleBo articleBo, HttpServletRequest request) {

        TokenPo account = userUtil.getToken(request);
        UserdataPo userdataPo = userdataMapper.selectByAccount(account.getAccount());

        //保存文章到本地
        String filePath = fileUtil.creatFile(articleBo.getContent());

        //将文章保存到数据库
        ArticleDataPo articleDataPo = new ArticleDataPo();
        articleDataPo.setTitle(articleBo.getTitle());
        String articleContent = articleBo.getContent();
        articleDataPo.setContent(filePath);
        articleDataPo.setAuthorId(account.getAccount());
        articleDataPo.setAuthorName(userdataPo.getName());
        articleDataPo.setNormal(normal);
        Date now = new Date();
        articleDataPo.setCreateTime(now);
        articleDataPo.setUpdateTime(now);
        articleDataMapper.insert(articleDataPo);

        ArticlePo articlePo = new ArticlePo();
        articlePo.setArticleDataId(articleDataPo.getId());
        articlePo.setTitle(articleBo.getTitle());
        articlePo.setContent(articleUtil.getContent(articleContent));
        articlePo.setLook(0);
        articlePo.setAuthorId(account.getAccount());
        articlePo.setAuthorName(userdataPo.getName());
        articlePo.setLiked(0);
        articlePo.setComment(0);
        articlePo.setCover(articleUtil.getCover(articleContent));
        articlePo.setCreateTime(now);
        articlePo.setUpdateTime(now);
        articleMapper.insert(articlePo);

        //更新作者文章数和字数
        userdataPo.setWord(userdataPo.getWord() + articleBo.getContent().length());
        userdataPo.setArticleNum(userdataPo.getArticleNum()+1);
        userdataMapper.updateById(userdataPo);

        return RespondResult.success();
    }

    /**
     * 查看文章
     *
     * @param id
     * @return
     */
    public RespondResult info(String id) {

        //文章查看数量加一
        ArticlePo articlePo = articleMapper.selectById(id);
        articlePo.setLook(articlePo.getLook()+1);
        articleMapper.updateById(articlePo);

        ArticleDataPo articleDataPo = articleDataMapper.selectById(articlePo.getArticleDataId());
        UserdataPo userdataPo = userdataMapper.selectOne(
                new QueryWrapper<UserdataPo>()
                        .lambda()
                        .eq(UserdataPo::getAccount, articlePo.getAuthorId()));

        String content = fileUtil.readFile(textUrl + articleDataPo.getContent());
        ArticleDataVo articleDataVo = new ArticleDataVo();
        articleDataVo.setAuthor(articlePo.getAuthorName());
        articleDataVo.setAuthorId(articlePo.getAuthorId());
        articleDataVo.setAuthorHead(userdataPo.getHead());
        articleDataVo.setFansSum(userdataPo.getFans());
        articleDataVo.setLiked(articlePo.getLiked());
        articleDataVo.setLikedSum(userdataPo.getLiked());
        articleDataVo.setLook(articlePo.getLook());
        articleDataVo.setTime(articlePo.getCreateTime());
        articleDataVo.setTitle(articlePo.getTitle());
        articleDataVo.setContent(content);
        articleDataVo.setWord(content.length());
        articleDataVo.setWordSum(userdataPo.getWord());
        return RespondResult.success(articleDataVo);
    }

    /**
     * 查询页面
     *
     * @return
     */
    public int getPage() {
        int count = articleMapper.selectCount(null);
        int page = count / articleNum;
        if (count % articleNum != 0) {
            page++;
        }
        return page;
    }

    /**
     * 查看文章评论
     *
     * @param id
     * @return
     */
    public RespondResult comment(String id) {
        logger.info("加载文章id：" + id+"的所有评论");
        List<CommentPo> commentPoList = commentMapper.selectList(
                new QueryWrapper<CommentPo>().lambda().eq(CommentPo::getArticleId, id)
        );
        List<ArticleCommentVo> articleCommentVoArrayList = new ArrayList<>();
        commentPoList.forEach(commentPo -> {
            ArticleCommentVo articleCommentVo = new ArticleCommentVo();
            articleCommentVo.setArticleId(commentPo.getArticleId());
            articleCommentVo.setComment(commentPo.getComment());
            articleCommentVo.setCommentId(commentPo.getId());
            UserdataPo userdataPo = userdataMapper.selectOne(
                    new QueryWrapper<UserdataPo>()
                            .lambda()
                            .eq(UserdataPo::getAccount, commentPo.getCommentAuthorId()));
            articleCommentVo.setHead(userdataPo.getHead());
            articleCommentVo.setName(userdataPo.getName());
            articleCommentVo.setTime(commentPo.getCreateTime());
            articleCommentVoArrayList.add(articleCommentVo);
        });
        return RespondResult.success(articleCommentVoArrayList);
    }

    /**
     * 喜欢文章
     * 文章点赞加一 作者点赞加一
     * @param id
     * @return
     */
    public RespondResult like(String id, HttpServletRequest request) {
        TokenPo token = userUtil.getToken(request);
        String account = token.getAccount();

        ArticlePo articlePo = articleMapper.selectById(id);
        articlePo.setLiked(articlePo.getLiked()+1);
        articleMapper.updateById(articlePo);

        String authorId = articlePo.getAuthorId();
        UserdataPo userdataPo = userdataMapper.selectOne(
                new QueryWrapper<UserdataPo>()
                        .lambda().eq(UserdataPo::getAccount, authorId));
        userdataPo.setLiked(userdataPo.getLiked()+1);
        userdataMapper.updateById(userdataPo);

        LikesPo likesPo = new LikesPo();
        likesPo.setArticleId(Integer.valueOf(id));
        likesPo.setAuthorId(articlePo.getAuthorId());
        likesPo.setLikesId(account);
        likesPo.setCreateTime(new Date());
        likesMapper.insert(likesPo);
        return RespondResult.success();
    }

    /**
     * 加载侧边文章
     * @param id
     * @return
     */
    public RespondResult aside(String id) {
        List<ArticleAsideVo> articleAsideVoList = new ArrayList<>();
        ArticlePo articlePo = articleMapper.selectById(id);
        List<ArticlePo> articlePoList = articleMapper.selectList(
                new QueryWrapper<ArticlePo>()
                        .lambda()
                        .eq(ArticlePo::getAuthorId, articlePo.getAuthorId()));
        articlePoList.forEach(article -> {
            ArticleAsideVo articleAsideVo = new ArticleAsideVo();
            articleAsideVo.setId(article.getId());
            articleAsideVo.setLook(article.getLook());
            articleAsideVo.setTitle(article.getTitle());
            articleAsideVoList.add(articleAsideVo);
        });
        return RespondResult.success(articleAsideVoList);
    }

    /**
     * 搜索文章
     * @param title
     * @return
     */
    public RespondResult search(String title) {
        int current = new Random().nextInt(getPage());
        IPage<ArticlePo> articlePoIPage = new Page<>(current + 1, articleNum);
        Wrapper<ArticlePo> queryWrapper = new QueryWrapper<ArticlePo>()
                .lambda()
                .like(ArticlePo::getTitle, title)
                .or().like(ArticlePo::getContent, title)
                .orderByDesc(ArticlePo::getId);
        articleMapper.selectPage(articlePoIPage, queryWrapper);
        List<ArticlePo> list = articlePoIPage.getRecords();
        return RespondResult.success(list);
    }
}
