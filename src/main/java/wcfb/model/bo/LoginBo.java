package wcfb.model.bo;

import lombok.Data;

/**
 * @Author wcfb
 * @Time 2020/1/16
 * 登录所用请求实体类
 */
@Data
public class LoginBo {
    private String account;
    private String password;
    private String captcha;
    private String uuid;
}
