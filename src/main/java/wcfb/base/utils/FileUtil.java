package wcfb.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import wcfb.model.constant.ConfigConstant;

import java.io.*;
import java.util.UUID;

@Component
public class FileUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件名
     */
    public String uploadImg(MultipartFile file) {
        if (file.isEmpty()) {
            logger.info("file isEmpty");
            return null;
        }
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 上传后的路径
        String filePath = ConfigConstant.imgUrl;
        // 新文件名
        fileName = "i" + UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 创建文件
     *
     * @param content
     * @return 文件名（包括路径）
     */
    public String creatFile(String content) {
        String fileName = "t" + UUID.randomUUID() + ".txt";
        try {
            File file = new File(ConfigConstant.textUrl + fileName);
            if(!file.exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();

            // write
            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.flush();
            bw.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 读取文件内容
     * @param filePath
     * @return 文件内容
     */
    public String readFile(String filePath){
        File file = new File(filePath);
        if(file.isFile() && file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer stringBuffer = new StringBuffer();
                String text = null;
                while((text = bufferedReader.readLine()) != null){
                    stringBuffer.append(text);
                }
                return stringBuffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
