package wcfb.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wcfb.base.support.RespondResult;
import wcfb.base.utils.*;
import wcfb.common.utils.Base64Util;
import wcfb.common.utils.Md5Util;
import wcfb.mapper.UserMapper;
import wcfb.mapper.UserdataMapper;
import wcfb.model.bo.*;
import wcfb.model.constant.CommonConstant;
import wcfb.model.enums.LoginEnum;
import wcfb.model.enums.RespondCodeEnum;
import wcfb.model.po.TokenPo;
import wcfb.model.po.UserPo;
import wcfb.model.po.UserdataPo;
import wcfb.model.pojo.EmailPojo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static wcfb.model.constant.AuthorConstant.imgUrl;
import static wcfb.model.constant.AuthorConstant.imgUrlDefault;
import static wcfb.model.constant.EmailConstant.emailContent;
import static wcfb.model.constant.EmailConstant.emailSubject;
import static wcfb.model.enums.RespondCodeEnum.emailIllegally;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */

@Service
public class UserServer {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginUtil loginUtil;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private UserdataMapper userdataMapper;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private Base64Util base64Util;

    @Autowired
    private Md5Util md5Util;

    /**
     * 登录
     * @param loginBo
     * @return 1.登录成功 其他登录失败
     */
    public RespondResult login(LoginBo loginBo, HttpServletRequest request){
        if (loginBo == null) {
            return RespondResult.error();
        }

        //验证码图片为空
        if (StringUtils.isBlank(loginBo.getUuid())){
            return RespondResult.error(RespondCodeEnum.captchaEmpty);
        }
        //图片验证码为空
        if (StringUtils.isBlank(loginBo.getCaptcha())){
            return RespondResult.error(RespondCodeEnum.codeEmpty);
        }

        //验证图片码
        if (!captchaService.validate(loginBo.getUuid(), loginBo.getCaptcha())) {
            return RespondResult.error(RespondCodeEnum.codeError);
        }

        return login(loginBo.getAccount(), loginBo.getPassword(), request);
    }

    /**
     * 登录
     * @param code
     * @param password
     * @return 1.登录成功 其他登录失败
     */
    public RespondResult login(String code, String password, HttpServletRequest request){
        String account = null;
        //用户名密码为空
        if (StringUtils.isBlank(code) || StringUtils.isBlank(password)){
            return RespondResult.error(RespondCodeEnum.accountAndPasswordEmpty);
        }

        String md5Password = md5Util.commonMD5(password);
        int type = loginUtil.analysis(code);
        if (LoginEnum.account.getKey() == type){
            account = userMapper.loginByAccount(code, md5Password);
        } else if (LoginEnum.phone.getKey() == type){
            account = userMapper.loginByPhone(code, md5Password);
        } else {
            account = userMapper.loginByEmail(code, md5Password);
        }

        if (account != null){

            //登录成功，添加token
            TokenPo tokenPo = tokenService.createToken(account);
            HttpSession session=request.getSession();
            session.setAttribute("token", tokenPo);
            return RespondResult.success(tokenPo.getToken());
        } else {
            return RespondResult.error(RespondCodeEnum.accountAndPasswordError);
        }
    }

    /**
     * 注册
     * 一个手机号只能注册一个用户
     * @param registerBo
     * @return
     */
    public RespondResult register(RegisterBo registerBo) {
        if (StringUtils.isBlank(registerBo.getName()) || StringUtils.isBlank(registerBo.getPassword()) ||
                StringUtils.isBlank(registerBo.getPhone())) {
            return RespondResult.error(RespondCodeEnum.dataIncomplete);
        }

        //检验手机号是否合法
        if(!PhoneUtil.checkPhoneLegal(registerBo.getPhone())) {
            return RespondResult.error(RespondCodeEnum.phoneIllegally);
        }

        //判断手机号是否注册过
        UserPo userPo = userMapper.selectByPhone(registerBo.getPhone());
        if (userPo != null) {
            return RespondResult.error(RespondCodeEnum.phoneUsed);
        }

        //判断邮箱是否合法
        String email = registerBo.getEmail();
        if (StringUtils.isBlank(email) || !email.contains("@")){
            return RespondResult.error(emailIllegally);
        }
        //判断邮箱是否注册过
        UserPo emailUser = userMapper.selectByEmail(email);
        if (emailUser != null) {
            return RespondResult.error(RespondCodeEnum.emailUsed);
        }

        String generateAccount = GenerateUtil.generateAccount();
        String md5Password = md5Util.commonMD5(registerBo.getPassword());
        userPo = new UserPo();
        userPo.setAccount(generateAccount);
        userPo.setPassword(md5Password);
        userPo.setPhone(registerBo.getPhone());
        userPo.setEmail(email);
        userMapper.insert(userPo);

        UserdataPo userdataPo = new UserdataPo();
        userdataPo.setAccount(generateAccount);
        userdataPo.setName(registerBo.getName());
        userdataPo.setSex(CommonConstant.unknownSex);
        userdataPo.setHead(null);
        userdataPo.setFollowNum(CommonConstant.no);
        userdataPo.setFans(CommonConstant.no);
        userdataPo.setArticleNum(CommonConstant.no);
        userdataPo.setWord(CommonConstant.noWord);
        userdataPo.setLiked(CommonConstant.no);
        userdataPo.setComment(CommonConstant.no);
        userdataPo.setHead(imgUrlDefault);
        userdataMapper.insert(userdataPo);
        return RespondResult.success();
    }

    /**
     * 确认登录
     * 拦截器没有拦截就代表登录了
     * @param request
     * @return
     */
    public RespondResult confirmLogin(HttpServletRequest request) {
        TokenPo token = userUtil.getToken(request);
        if (token == null) {
            return RespondResult.error();
        }
        return RespondResult.success();
    }

    /**
     * 得到用户头像
     * @param request
     * @return
     */
    public RespondResult getHeadUrl(HttpServletRequest request){
        TokenPo token = userUtil.getToken(request);
        if (token == null) {
            return RespondResult.success();
        }
        String account = token.getAccount();
        UserdataPo userdataPo = userdataMapper.selectByAccount(account);
        if (userdataPo == null) {
            return RespondResult.success();
        }
        return RespondResult.success(userdataPo.getHead());
    }

    /**
     * 找回密码
     * 给用户发送短息包含更改密码的网址
     * @param retrieveBo
     * @return
     */
    public RespondResult retrieve(RetrieveBo retrieveBo) {
        String email = retrieveBo.getEmail();
        if (!email.contains("@")){
            return RespondResult.error(emailIllegally);
        }

        //网址为邮箱base64加密
        String encode = base64Util.encode(email);
        EmailPojo emailPojo = new EmailPojo();
        emailPojo.setRecipient(email);
        emailPojo.setSubject(emailSubject);
        emailPojo.setContent(emailContent + encode);
        emailUtil.sendSimpleMail(emailPojo);
        return RespondResult.success();
    }

    /**
     * 修改密码
     * @param changePasswordBo
     * @return
     */
    public RespondResult changePassword(ChangePasswordBo changePasswordBo) {
        if (changePasswordBo == null){
            return RespondResult.error();
        }
        String email = base64Util.decode(changePasswordBo.getEmail());
        if (StringUtils.isBlank(email)) {
            return RespondResult.error();
        }
        UserPo userPo = new UserPo();
        userPo.setEmail(email);
        String password = md5Util.commonMD5(changePasswordBo.getPassword());
        userPo.setPassword(password);
        userMapper.updatePassword(userPo);
        return RespondResult.success();
    }

    /**
     * 设置个人资料
     * @param userDataBo
     * @param request
     * @return
     */
    public RespondResult set(UserDataBo userDataBo, HttpServletRequest request) {
        TokenPo tokenPo = userUtil.getToken(request);
        UserdataPo userdataPo = userdataMapper.selectByAccount(tokenPo.getAccount());
        userdataPo.setHead(imgUrl + userDataBo.getHead());
        userdataPo.setName(userDataBo.getName());
        userdataPo.setSex(userDataBo.getSex());
        userdataMapper.updateById(userdataPo);
        return RespondResult.success();
    }
}
