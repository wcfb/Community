package wcfb.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wcfb.mapper.TokenMapper;
import wcfb.model.po.TokenPo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger("TokenInterceptor");

    private static final String AUTH_PATH1 = "/community";
    private static final String AUTH_PATH2 = "/user/login";
    private static final String AUTH_PATH21 = "/user/register";
    private static final String AUTH_PATH22 = "/user/confirmLogin";
    private static final String AUTH_PATH23 = "/user/retrieve";
    private static final String AUTH_PATH24 = "/user/changePassword";
    private static final String AUTH_PATH3 = "/article/find";
    private static final String AUTH_PATH31 = "/article/info";
    private static final String AUTH_PATH32 = "/article/comments";
    private static final String AUTH_PATH33 = "/article/aside";
    private static final String AUTH_PATH34 = "/article/search";
    private static final String AUTH_PATH4 = "/author/find";
    private static final String AUTH_PATH41 = "/author/info";
    private static final String AUTH_PATH5 = "/css";
    private static final String AUTH_PATH6 = "jpg";
    private static final String AUTH_PATH7 = ".html";
    private static final String AUTH_PATH8 = "favicon.ico";
    private static final String AUTH_PATH9 = "/error";
    private static final String AUTH_PATH10 = "/js";

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception ex)
            throws Exception {
        if (ex != null) {
            logger.error("<== afterCompletion - 解析token失败. ex={}", ex.getMessage(), ex);
            this.handleException(response);
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView mv) {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // OPTIONS请求类型直接返回不处理
        // 防止axios请求两次
        if ("OPTIONS".equals(request.getMethod())){
            logger.info("OPTIONS请求");
            return false;
        }

        String uri = request.getRequestURI();
        if (uri.contains(AUTH_PATH1) || uri.contains(AUTH_PATH2) || uri.contains(AUTH_PATH21) ||
                uri.contains(AUTH_PATH22) || uri.contains(AUTH_PATH23) || uri.contains(AUTH_PATH24) ||
                uri.contains(AUTH_PATH3) || uri.contains(AUTH_PATH31) || uri.contains(AUTH_PATH32) ||
                uri.contains(AUTH_PATH33) ||uri.contains(AUTH_PATH34) ||
                uri.contains(AUTH_PATH4) || uri.contains(AUTH_PATH41) ||
                uri.contains(AUTH_PATH5) ||
                uri.contains(AUTH_PATH6) || uri.contains(AUTH_PATH7) || uri.contains(AUTH_PATH8) ||
                uri.contains(AUTH_PATH9) || uri.contains(AUTH_PATH10)) {
            return true;
        }

        //判断token
        HttpSession session = request.getSession();
        TokenPo token = (TokenPo) session.getAttribute("token");
        if (token == null) {
            logger.info("非法token");
            return false;
        }

        // 检查token缓存
        TokenPo tokenPo = tokenMapper.selectByToken(token.getToken());
        if (tokenPo == null) {
            logger.info("非法token");
            return false;
        }
        return true;
    }

    private void handleException(HttpServletResponse res) throws IOException {
        res.resetBuffer();
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("{\"success\":false , \"errorCode\":\"-1\" ,\"errorMessage\" :\"解析token失败\"}");
        res.flushBuffer();
    }

}