package wcfb.base.utils;

import org.springframework.stereotype.Component;
import wcfb.model.enums.LoginEnum;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */
@Component
public class LoginUtil {

    /**
     * 判断登录是什么方式
     * @param code
     * @return
     */
    public int analysis(String code){

        if (code.contains("@")){
            return LoginEnum.email.getKey();
        }

        if (PhoneUtil.checkPhoneLegal(code)) {
            return LoginEnum.phone.getKey();
        }

        return LoginEnum.account.getKey();
    }
}
