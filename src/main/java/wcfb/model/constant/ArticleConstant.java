package wcfb.model.constant;

import lombok.Data;

@Data
public final class ArticleConstant {

    //推荐文章的数量
    public final static int articleNum = 10;

    //文章封面最多显示的文字数量
    public final static int articleShowMaxWord = 100;

    public final static int normal = 1;
    public final static int abnormal = 2;
    public final static String image = "(http://localhost:8080/img/";
}
