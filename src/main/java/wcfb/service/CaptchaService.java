package wcfb.service;

import com.google.code.kaptcha.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.mapper.CaptchaMapper;
import wcfb.model.constant.LoginConstant;
import wcfb.model.po.CaptchaPo;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

/**
 * @Author wcfb
 * @Time 2020/1/16
 */
@Service
public class CaptchaService {

    private Logger logger = LoggerFactory.getLogger("CaptchaService");

    @Autowired
    private Producer producer;

    @Autowired
    private CaptchaMapper captchaMapper;

    public void getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            //设置页面不缓存
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");

            // 生成验证码编码
            String capText = producer.createText();
            // 将验证码编码生成图片
            BufferedImage kaptchaImage = producer.createImage(capText);

            //输出验证码PNG格式图片
            response.setHeader("content-type", "image/png");
            out = response.getOutputStream();
            ImageIO.write(kaptchaImage, "png", out);
            out.flush();

            //获取session，并将验证码编码存放到session中
            HttpSession session = request.getSession();
            session.setAttribute(LoginConstant.DEFAULT_KAPTCHA_SESSION_KEY, capText);
        } catch (IOException e) {
            logger.info("*****服务端获取验证码异常*******" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.info("*****流关闭异常*******" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成图片验证码
     * @return
     */
    public BufferedImage getCaptcha(String uuid) {
        logger.info(uuid);
        //生成文字验证码
        String code = producer.createText();

        CaptchaPo captchaEntity = new CaptchaPo();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(new Date().getTime()+5*60*1000);

        captchaMapper.insert(captchaEntity);

        BufferedImage image = producer.createImage(code);
        return image;
    }

    /**
     * 验证图片验证码
     * @param code
     * @return
     */
    public boolean validate(String uuid, String code) {
        CaptchaPo captchaEntity = captchaMapper.selectOne(uuid);
        if (captchaEntity == null) {
            return false;
        }

        //删除验证码
        captchaMapper.delete(uuid);

        if (captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime() >= System.currentTimeMillis()) {
            return true;
        }

        return false;
    }
}
