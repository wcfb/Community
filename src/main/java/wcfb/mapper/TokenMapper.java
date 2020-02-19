package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.TokenPo;

/**
 * @Author wcfb
 * @Time 2020/1/17
 */
@Mapper
public interface TokenMapper extends BaseMapper<TokenPo> {

    TokenPo selectByAccount(String account);

    TokenPo selectByToken(String token);

    void updateByAccount(TokenPo tokenPo);

    void clean(Long expire);
}
