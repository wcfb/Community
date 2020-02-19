package wcfb.base.support;

import wcfb.model.enums.RespondCodeEnum;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */


public class RespondResult<T> {

    private int code;
    private String value;
    private T data;

    public RespondResult() {
    }

    public RespondResult(RespondCodeEnum respondCodeEnum, T data) {
        this.code = respondCodeEnum.getCode();
        this.value = respondCodeEnum.getValue();
        this.data = data;
    }

    public static RespondResult success(){
        RespondResult respondResult = new RespondResult();
        respondResult.setCode(RespondCodeEnum.success.getCode());
        respondResult.setValue(RespondCodeEnum.success.getValue());
        return respondResult;
    }

    public static <T> RespondResult success(T data){
        RespondResult respondResult = new RespondResult();
        respondResult.setCode(RespondCodeEnum.success.getCode());
        respondResult.setValue(RespondCodeEnum.success.getValue());
        respondResult.setData(data);
        return respondResult;
    }

    public static RespondResult error(){
        RespondResult respondResult = new RespondResult();
        respondResult.setCode(RespondCodeEnum.error.getCode());
        respondResult.setValue(RespondCodeEnum.error.getValue());
        return respondResult;
    }

    public static RespondResult error(RespondCodeEnum respondCodeEnum){
        RespondResult respondResult = new RespondResult();
        respondResult.setCode(respondCodeEnum.getCode());
        respondResult.setValue(respondCodeEnum.getValue());
        return respondResult;
    }

    public static <T> RespondResult error(RespondCodeEnum respondCodeEnum, T data){
        RespondResult respondResult = new RespondResult();
        respondResult.setCode(respondCodeEnum.getCode());
        respondResult.setValue(respondCodeEnum.getValue());
        respondResult.setData(data);
        return respondResult;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
