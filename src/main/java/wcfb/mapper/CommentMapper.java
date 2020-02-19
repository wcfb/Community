package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.CommentPo;
import wcfb.model.vo.MessageCommentVo;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<CommentPo> {

    List<MessageCommentVo> selectCommentByAuthor(String author);

}
