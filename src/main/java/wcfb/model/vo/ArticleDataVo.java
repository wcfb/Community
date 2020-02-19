package wcfb.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: wcfb
 * @date: 2020/2/12
 * @version: 1.0.0
 */
@Data
public class ArticleDataVo {
    private String title;
    private String content;
    private String author;
    private String authorId;
    private String authorHead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;
    private int word;
    private int look;
    private int liked;
    private float wordSum;
    private int likedSum;
    private int fansSum;
}
