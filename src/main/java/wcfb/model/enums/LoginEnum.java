package wcfb.model.enums;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */

public enum LoginEnum {
    account(1),
    phone(2),
    email(3);

    private int key;

    LoginEnum(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
