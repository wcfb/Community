package wcfb.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wcfb.base.support.RespondResult;
import wcfb.model.bo.*;
import wcfb.service.CaptchaService;
import wcfb.service.UserServer;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @Author wcfb
 * @Time 2020/1/4
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServer userServer;

    @Autowired
    private CaptchaService captchaService;

    /**
     * 登录
     * @param loginBo 请求体
     * @param request
     * @return
     */
    @RequestMapping("/login")
    public RespondResult login(@RequestBody LoginBo loginBo, HttpServletRequest request){
        return userServer.login(loginBo, request);
    }

    /**
     * 检查是否登录
     * @param request
     * @return
     */
    @RequestMapping("/confirmLogin")
    public RespondResult confirmLogin(HttpServletRequest request){
        return userServer.confirmLogin(request);
    }

    /**
     * 注册
     * @param registerBo
     * @return
     */
    @RequestMapping("/register")
    public RespondResult register(@RequestBody RegisterBo registerBo){
        return userServer.register(registerBo);
    }

    /**
     * 找回密码
     * @param retrieveBo
     * @return
     */
    @RequestMapping("/retrieve")
    public RespondResult retrieve(@RequestBody RetrieveBo retrieveBo){
        return userServer.retrieve(retrieveBo);
    }

    /**
     * 更改密码
     * @param changePasswordBo
     * @return
     */
    @RequestMapping("/changePassword")
    public RespondResult changePassword(@RequestBody ChangePasswordBo changePasswordBo){
        return userServer.changePassword(changePasswordBo);
    }

    /**
     * 得到用户头像
     * @return
     */
    @RequestMapping("/getHeadUrl")
    public RespondResult getHeadUrl(HttpServletRequest request){
        return userServer.getHeadUrl(request);
    }

    /**
     * 获取生成的图片验证码
     * @param response
     */
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response, @RequestParam("uuid") String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = captchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 设置个人资料
     * @param userDataBo
     * @param request
     * @return
     */
    @PostMapping("/set")
    public RespondResult set(@RequestBody UserDataBo userDataBo, HttpServletRequest request){
        return userServer.set(userDataBo, request);
    }
}
