package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.UserdataPo;

@Mapper
public interface UserdataMapper extends BaseMapper<UserdataPo> {

    int insert(UserdataPo userdataPo);

    UserdataPo selectByAccount(String account);
}
