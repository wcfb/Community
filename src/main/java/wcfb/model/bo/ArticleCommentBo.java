package wcfb.model.bo;

import lombok.Data;

/**
 * @author: wcfb
 * @date: 2020/2/17
 * @version: 1.0.0
 */
@Data
public class ArticleCommentBo {
    //1.文章id  2.评论id
    private Integer id;
    //1.文章 2.评论
    private Integer type;
    private String comment;
}
