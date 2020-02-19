package wcfb.model.constant;

import lombok.Data;

@Data
public class EmailConstant {

    public static final String emailSubject = "密码找回(粉笔社区)";
    public static final String emailContent = "如果非本人操作请忽略！http://localhost:8080/changePassword.html?";
}
