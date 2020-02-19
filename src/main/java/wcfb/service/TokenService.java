package wcfb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.base.utils.GenerateUtil;
import wcfb.mapper.TokenMapper;
import wcfb.model.po.TokenPo;

import java.util.Date;

/**
 * @Author wcfb
 * @Time 2020/1/17
 * 用户token服务
 */
@Service
public class TokenService {

    //12小时后过期
    private final static Long EXPIRE = Long.valueOf(3600 * 12 * 1000);

    @Autowired
    private TokenMapper tokenMapper;

    /**
     * 为用户添加token
     * @param account
     * @return
     */
    public TokenPo createToken(String account) {

        //生成一个token
        String token = GenerateUtil.generateUuid();

        //过期时间
        Long expireTime = new Date().getTime() + EXPIRE;

        //判断是否生成过token
        TokenPo userTokenPo = tokenMapper.selectByAccount(account);
        if (userTokenPo == null) {
            userTokenPo = new TokenPo();
            userTokenPo.setAccount(account);
            userTokenPo.setToken(token);
            userTokenPo.setExpire(expireTime);

            //保存token
            tokenMapper.insert(userTokenPo);
        } else {
            userTokenPo.setToken(token);
            userTokenPo.setExpire(expireTime);

            //更新token
            tokenMapper.updateByAccount(userTokenPo);
        }

        TokenPo tokenPo = new TokenPo();
        tokenPo.setToken(token);
        tokenPo.setExpire(EXPIRE);

        return tokenPo;
    }

    /**
     * 用户退出登录，重新生成token
     * @param account
     */
    public void logout(String account) {
        //生成一个token
        String token = GenerateUtil.generateUuid();
        //修改token
        TokenPo tokenEntity = new TokenPo();
        tokenEntity.setAccount(account);
        tokenEntity.setToken(token);
        tokenMapper.updateByAccount(tokenEntity);
    }
}
