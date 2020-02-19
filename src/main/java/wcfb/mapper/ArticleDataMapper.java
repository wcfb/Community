package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.ArticleDataPo;

@Mapper
public interface ArticleDataMapper extends BaseMapper<ArticleDataPo> {

//    int insert(ArticleDataPo articleDataPo);
}
