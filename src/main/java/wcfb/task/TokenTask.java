package wcfb.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import wcfb.mapper.TokenMapper;

import java.util.Date;

@Configuration
@EnableScheduling
public class TokenTask {
    private Logger logger = LoggerFactory.getLogger("TokenTask");

    @Autowired
    private TokenMapper tokenMapper;

    @Scheduled(cron = "0 0 0 * * ?")
    private void cleanToken() {
        logger.info("clean");
        tokenMapper.clean(new Date().getTime() - 3600 * 12 * 1000);
    }
}
