package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.FansPo;
import wcfb.model.vo.MessageFollowVo;

import java.util.List;

@Mapper
public interface FansMapper extends BaseMapper<FansPo> {

    List<MessageFollowVo> selectFollowsByAuthor(String author);

}
