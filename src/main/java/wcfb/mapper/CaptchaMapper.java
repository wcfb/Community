package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.CaptchaPo;

/**
 * @Author wcfb
 * @Time 2020/1/16
 */
@Mapper
public interface CaptchaMapper extends BaseMapper<CaptchaPo> {

    CaptchaPo selectOne(String uuid);

    void delete(String uuid);

    void clean(Long expire);
}
