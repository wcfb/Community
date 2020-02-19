package wcfb.base.utils;

import java.util.Date;
import java.util.UUID;

/**
 * @Author wcfb
 * @Time 2020/1/17
 */
public class GenerateUtil {

    public static String generateUuid() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateUpperUuid() {

        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static String generateAccount(){
        String account = String.valueOf(new Date().getTime()).substring(2,12);
        return account;
    }
}
