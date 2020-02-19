package wcfb.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: wcfb
 * @date: 2020/2/17
 * @version: 1.0.0
 */
@Data
public class MessageLikesVo {
    private Integer id;
    private String head;
    private String name;
    private String article;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date time;
}
