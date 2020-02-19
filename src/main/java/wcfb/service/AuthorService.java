package wcfb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.base.support.RespondResult;
import wcfb.base.utils.UserUtil;
import wcfb.mapper.*;
import wcfb.model.bo.AuthorBo;
import wcfb.model.po.*;
import wcfb.model.vo.AuthorDataVo;
import wcfb.model.vo.MessageCommentVo;
import wcfb.model.vo.MessageFollowVo;
import wcfb.model.vo.MessageLikesVo;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static wcfb.model.constant.AuthorConstant.authorNum;
import static wcfb.model.enums.RespondCodeEnum.logOutDate;

/**
 * @author: wcfb
 * @date: 2020/2/13
 * @version: 1.0.0
 */
@Service
public class AuthorService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserdataMapper userdataMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikesMapper likesMapper;

    /**
     * 推荐作者
     * 每次推荐没有关注的6个粉丝最多的作者
     * @return
     */
    public RespondResult find() {
        int count = userdataMapper.selectCount(null);
        int page = (count-1)/authorNum;
        if (count%authorNum != 0){
            page++;
        }
        int current = new Random().nextInt(page);
        IPage<UserdataPo> articlePoIPage = new Page<>(current+1, authorNum);
        Wrapper<UserdataPo> queryWrapper = new QueryWrapper<UserdataPo>()
                .lambda()
                .orderByDesc(UserdataPo::getFans);
        userdataMapper.selectPage(articlePoIPage, queryWrapper);
        List<UserdataPo> list = articlePoIPage.getRecords();
        return RespondResult.success(list);
    }

    /**
     * 查看作者
     * @param id
     * @return
     */
    public RespondResult info(String id, HttpServletRequest request) {
        String account = id;
        AuthorDataVo authorDataVo = new AuthorDataVo();
        //如果id为空，则认为查看自己的
        if (id.equals("0")){
            TokenPo tokenPo = userUtil.getToken(request);
            if (tokenPo == null){
                return RespondResult.error(logOutDate);
            }
            account = tokenPo.getAccount();
            authorDataVo.setSettingVisible(true);
            authorDataVo.setFollowVisible(false);
        }
        //根据账号查找用户信息
        UserdataPo userdataPo = userdataMapper.selectOne(
                new QueryWrapper<UserdataPo>()
                        .lambda()
                        .eq(UserdataPo::getAccount, account));
        authorDataVo.setUser(userdataPo.getName());
        authorDataVo.setHead(userdataPo.getHead());
        authorDataVo.setArticle(userdataPo.getArticleNum());
        authorDataVo.setFollow(userdataPo.getFollowNum());
        authorDataVo.setWord(userdataPo.getWord());
        authorDataVo.setFans(userdataPo.getFans());
        authorDataVo.setLiked(userdataPo.getLiked());

        List<ArticlePo> articlePoList =  articleMapper.selectList(
                new QueryWrapper<ArticlePo>()
                        .lambda()
                        .eq(ArticlePo::getAuthorId, account));
        authorDataVo.setArticleList(articlePoList);
        return RespondResult.success(authorDataVo);
    }

    /**
     * 关注作者
     * @param request
     * @return
     */
    public RespondResult followAuthor(AuthorBo authorBo, HttpServletRequest request) {
        TokenPo token = userUtil.getToken(request);
        if (token == null || StringUtils.isBlank(token.getAccount())){
            return RespondResult.error(logOutDate);
        }
        Integer followNum = fansMapper.selectCount(
                new QueryWrapper<FansPo>().lambda()
                        .eq(FansPo::getAuthorId, authorBo.getAuthor())
                        .eq(FansPo::getFansId, token.getAccount()));
        if (followNum == 0) {
            FansPo fansPo = new FansPo();
            fansPo.setAuthorId(authorBo.getAuthor());
            fansPo.setFansId(token.getAccount());
            fansPo.setCreateTime(new Date());
            fansMapper.insert(fansPo);
        }

        //添加粉丝数
        UserdataPo userdataPo = userdataMapper.selectOne(
                new QueryWrapper<UserdataPo>()
                        .lambda()
                        .eq(UserdataPo::getAccount, authorBo.getAuthor()));
        userdataPo.setFans(userdataPo.getFans()+1);
        userdataMapper.updateById(userdataPo);

        //添加关注数
        UserdataPo userdata = userdataMapper.selectOne(
                new QueryWrapper<UserdataPo>()
                        .lambda()
                        .eq(UserdataPo::getAccount, token.getAccount()));
        userdata.setFollowNum(userdata.getFollowNum()+1);
        userdataMapper.updateById(userdata);
        return RespondResult.success();
    }

    /**
     * 查看作者收到的评论
     * @param request
     * @return
     */
    public RespondResult comment(HttpServletRequest request) {
        TokenPo tokenPo = userUtil.getToken(request);
        String account = tokenPo.getAccount();
        if (account == null){
            return RespondResult.error(logOutDate);
        }
        List<MessageCommentVo> messageCommentVoList = commentMapper.selectCommentByAuthor(account);
        return RespondResult.success(messageCommentVoList);
    }

    /**
     * 收到的喜欢
     * @param request
     * @return
     */
    public RespondResult likes(HttpServletRequest request) {
        TokenPo tokenPo = userUtil.getToken(request);
        String account = tokenPo.getAccount();
        if (account == null){
            return RespondResult.error(logOutDate);
        }
        List<MessageLikesVo> messageLikesVoList = likesMapper.selectLikesByAuthor(account);
        return RespondResult.success(messageLikesVoList);
    }

    /**
     * 关注的人
     * @param request
     * @return
     */
    public RespondResult follows(HttpServletRequest request) {
        TokenPo tokenPo = userUtil.getToken(request);
        String account = tokenPo.getAccount();
        if (account == null){
            return RespondResult.error(logOutDate);
        }
        List<MessageFollowVo> messageFollowVoList = fansMapper.selectFollowsByAuthor(account);
        return RespondResult.success(messageFollowVoList);
    }
}
