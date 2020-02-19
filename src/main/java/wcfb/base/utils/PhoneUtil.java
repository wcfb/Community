package wcfb.base.utils;

import java.util.regex.Pattern;

/**
 * 手机号工具类
 */
public class PhoneUtil {

    /**
     * 检查手机号是否合法
     * @param phone
     * @return
     */
    public static boolean checkPhoneLegal(String phone){
        String pattern = "0?(13|14|15|18|17)[0-9]{9}";
        if(Pattern.compile(pattern).matcher(phone).matches()) {
            return true;
        }
        return false;
    }
}
