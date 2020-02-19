package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.LikesPo;
import wcfb.model.vo.MessageLikesVo;

import java.util.List;

@Mapper
public interface LikesMapper extends BaseMapper<LikesPo> {

    List<MessageLikesVo> selectLikesByAuthor(String author);

}
