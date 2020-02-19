package wcfb.model.pojo;

import lombok.Data;

import java.io.File;
import java.io.Serializable;

@Data
public class EmailPojo implements Serializable {

    //邮件接收人
    String recipient;
    //邮件的主题
    String subject;
    //邮件的内容
    String content;
    //发送文件
    File file;
}
