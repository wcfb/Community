package wcfb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wcfb.base.utils.FileUtil;

@Controller
@RequestMapping("/img")
public class ImgController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileUtil fileUtil;

    /**
     *获取图片地址
     * @param img
     * @return
     */
    @ResponseBody
    @RequestMapping("/getImg")
    public String getImg(@RequestParam("img") String img){
        return "http://localhost:8080/img/" + img;
    }


    /**
     * 上传图片
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/imgUpload")
    public String fileUpload(@RequestParam(value = "file") MultipartFile file) {
        String fileName = fileUtil.uploadImg(file);
        return fileName;
    }

}
