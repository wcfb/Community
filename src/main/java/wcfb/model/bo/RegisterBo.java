package wcfb.model.bo;

import lombok.Data;

@Data
public class RegisterBo {
    private String name;
    private String phone;
    private String password;
    private String email;
}
