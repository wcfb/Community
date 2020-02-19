package wcfb.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import wcfb.mapper.CaptchaMapper;

import java.util.Date;

@Configuration
@EnableScheduling
public class CaptchaTask {

    private Logger logger = LoggerFactory.getLogger("CaptchaTask");

    @Autowired
    private CaptchaMapper captchaMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    private void cleanCaptcha() {
        logger.info("clean");
        captchaMapper.clean(new Date().getTime() - 3600 * 12 * 1000);
    }
}
