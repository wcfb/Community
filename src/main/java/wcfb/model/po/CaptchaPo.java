package wcfb.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author wcfb
 * @Time 2020/1/16
 */

@Data
@TableName("captcha")
public class CaptchaPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private int id;
    private String uuid;
    //验证码
    private String code;
    //过期时间
    private Long expireTime;
}
