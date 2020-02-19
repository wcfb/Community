package wcfb.base.utils;

import org.springframework.stereotype.Component;

import static wcfb.model.constant.ArticleConstant.articleShowMaxWord;
import static wcfb.model.constant.ArticleConstant.image;

@Component
public class ArticleUtil {

    /**
     * 得到文章中的缩略文字
     * @param articleContent
     * @return
     */
    public String getContent(String articleContent){
        if (articleContent.length()<=articleShowMaxWord){
            return articleContent;
        }
        int start = articleContent.indexOf("![");
        if (start>articleShowMaxWord){
            return articleContent.substring(0, articleShowMaxWord);
        }
        if (start<0){
            return articleContent.substring(0, articleShowMaxWord);
        }
        int end = articleContent.substring(start).indexOf(")");
        String title = (articleContent.substring(0, start) + articleContent.substring(end+start+1));
        if (title.length()<=100){
            return title;
        } else {
            return title.substring(0, articleShowMaxWord) + "...";
        }
    }

    /**
     * 得到文章的封面
     * 如果文章中包含图片，则将第一张图片设置为封面
     * @param articleContent
     * @return
     */
    public String getCover(String articleContent){
        if (articleContent.contains(image)){
            int begin = articleContent.indexOf(image);
            String imageContent = articleContent.substring(begin);
            int end = imageContent.indexOf(")");
            String imageUrl = imageContent.substring(image.length(), end);
            return "img/" + imageUrl;
        }
        return null;
    }
}
