package wcfb.base.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wcfb.mapper.TokenMapper;
import wcfb.mapper.UserMapper;
import wcfb.model.po.TokenPo;
import wcfb.model.po.UserPo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户工具类
 */
@Component
public class UserUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据请求头查询用户信息
     * @param request
     * @return
     */
    public TokenPo getToken(HttpServletRequest request){
        //判断token
        HttpSession session = request.getSession();
        TokenPo token = (TokenPo) session.getAttribute("token");
        if (token == null) {
            return null;
        }

        // 检查token缓存
        TokenPo tokenPo = tokenMapper.selectByToken(token.getToken());
        if (tokenPo == null) {
            return null;
        }
        logger.info("用户:" + tokenPo);
        return tokenPo;
    }

    public UserPo getUser(HttpServletRequest request){
        TokenPo tokenPo = getToken(request);
        if (tokenPo == null){
            return null;
        }
        UserPo userPo = userMapper.selectOne(new QueryWrapper<UserPo>()
                .lambda()
                .eq(UserPo::getAccount, tokenPo.getAccount()));
        return userPo;
    }
}
