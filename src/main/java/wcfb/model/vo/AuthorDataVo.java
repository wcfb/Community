package wcfb.model.vo;

import lombok.Data;
import wcfb.model.po.ArticlePo;

import java.util.List;

/**
 * @author: wcfb
 * @date: 2020/2/13
 * @version: 1.0.0
 */
@Data
public class AuthorDataVo {
    private Boolean followVisible;
    private Boolean settingVisible;
    private String user;
    private String head;
    private Integer article;
    private Integer follow;
    private Float word;
    private Integer fans;
    private Integer liked;
    private List<ArticlePo> articleList;
}
