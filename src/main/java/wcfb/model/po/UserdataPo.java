package wcfb.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("userdata")
public class UserdataPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String account;
    private String name;
    /** 性别(0未知，1男，2女) */
    private Integer sex;
    private String head;
    private Integer followNum;
    private Integer fans;
    private Integer articleNum;
    private Float word;
    private Integer liked;
    private Integer comment;
}
