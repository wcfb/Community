package wcfb.model.enums;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */
public enum RespondCodeEnum {

    success(200,"成功"),
    notFound(404,"没有找到"),
    error(500,"错误"),
    logOutDate(501,"登录过期"),
    captchaEmpty(1001,"图片为空"),
    captchaError(1002,"图片错误"),
    codeEmpty(1003,"验证码为空"),
    codeError(1004,"验证码错误"),
    accountAndPasswordEmpty(1005,"用户名密码为空"),
    accountAndPasswordError(1006,"用户名密码错误"),
    dataIncomplete(1007,"数据不完整"),
    phoneUsed(1008,"手机号已经注册过"),
    phoneIllegally(1009,"手机号不合法"),
    emailIllegally(1010,"邮箱不合法"),
    emailUsed(1011,"邮箱注册过");

    private int code;
    private String value;

    RespondCodeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RespondCodeEnum{" +
                "code=" + code +
                ", value='" + value + '\'' +
                '}';
    }
}
