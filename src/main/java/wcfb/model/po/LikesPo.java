package wcfb.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: wcfb
 * @date: 2020/2/17
 * @version: 1.0.0
 */
@Data
@TableName("likes")
public class LikesPo {
    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private Integer articleId;
    private String authorId;
    private String likesId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
}
