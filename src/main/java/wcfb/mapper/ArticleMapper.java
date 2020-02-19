package wcfb.mapper;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wcfb.model.po.ArticlePo;

/**
 * @Author wcfb
 * @Time 2020/1/16
 */
@Mapper
@TableName("article")
public interface ArticleMapper extends BaseMapper<ArticlePo> {

    int insert(ArticlePo articlePo);

    ArticlePo selectById(Integer id);

    Integer selectMaxId();
}
