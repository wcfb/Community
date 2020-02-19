package wcfb.model.bo;

import lombok.Data;

/**
 * @author: wcfb
 * @date: 2020/2/11
 * @version: 1.0.0
 */
@Data
public class ChangePasswordBo {
    private String email;
    private String password;
}
