new Vue({
    el: "#app",
    data() {
        return {}
    },
    created() {
        this.loadArticle()
        this.loadAuthor()
    },
    methods: {
        loadArticle() {
            var vue = this
            //推荐文章
            axios.post('http://localhost:8080/article/find', {}).then(function (responce) {
                if (responce.data.code == 200) {
                    vue.addArticle(responce.data.data)
                } else {
                    vue.$message.error(responce.data.value)
                }
            }).catch(function (error) {
                console.log(error);
            });

        },
        loadAuthor() {
            var vue = this
            //推荐作者
            axios.post('http://localhost:8080/author/find', {}).then(function (responce) {
                if (responce.data.code == 200) {
                    vue.addAuthor(responce.data.data)
                } else {
                    vue.$message.error(responce.data.value)
                }
            }).catch(function (error) {
                console.log(error);
            });
        },
        addArticle(articleList) {
            var innerHTML = "";
            for (var i = 0; i < articleList.length; i++) {
                if (articleList[i].cover == null || articleList[i].cover == '') {
                    innerHTML +=
                        "<li id=\"\" data-note-id=\"\">\n";
                } else {
                    innerHTML +=
                        "<li id=\"\" data-note-id=\"\" class=\"have-img\">\n" +
                        "   <a class=\"wrap-img\">\n" +
                        "       <img class=\"  img-blur-done\" src=\"" + articleList[i].cover + "\" alt=\"120\">\n" +
                        "   </a>\n";
                }
                innerHTML +=
                    "   <div class=\"content\">\n" +
                    "       <a class=\"title\" target=\"\" href=\"http://localhost:8080/articleShow.html?" + articleList[i].id + "\">" + articleList[i].title + "</a>\n" +
                    "       <p class=\"abstract\">\n" + articleList[i].content +
                    "       </p>\n" +
                    "   <div class=\"meta\">\n" +
                    "       <span class=\"jsd-meta\">" +
                    "           <i class=\"iconfont ic-list-read\"></i> " + articleList[i].look + "</span>\n" +
                    "       <a class=\"nickname\">" + articleList[i].authorName + "</a>\n" +
                    "       <a>\n" +
                    "           <i class=\"iconfont ic-list-comments\"></i> " + articleList[i].comment + "\n" +
                    "       </a>     \n" +
                    "       <span><i class=\"iconfont ic-list-like\"></i> " + articleList[i].liked + "</span>\n" +
                    "       </div>\n" +
                    "   </div>\n" +
                    "</li>";
            }
            document.getElementById("articleList").innerHTML += innerHTML
        },
        //添加推荐的作者
        addAuthor(author) {
            var innerHTML = "";
            for (var i = 0; i < author.length; i++) {
                innerHTML +=
                    "<li>\n" +
                    "   <a href=\"http://localhost:8080/user.html?" + author[i].account + "\" class=\"avatar\">" +
                    "       <img src=\"" + author[i].head + "\">" +
                    "   </a>\n" +
                    "   <a onclick='follow(" + author[i].account + ")' class=\"follow\" state=\"0\">" +
                    "       <i class=\"iconfont ic-follow\"></i>关注</a>\n" +
                    "   <a href=\"http://localhost:8080/user.html?" + author[i].account + "\" class=\"name\">" + author[i].name + "</a>\n" +
                    "   <p>写了" + author[i].word + "字 · " + author[i].liked + "喜欢</p>\n" +
                    "</li>";
            }
            document.getElementById("authorList").innerHTML = innerHTML
        }
    }
})

/**
 * 关注博主
 * @param account
 */
function follow(account) {
    confirmLogin()
    axios.post('http://localhost:8080/author/followAuthor', {
        'author': account
    }).then(response => {
        if (response.data.code == 200) {
            var vue = new Vue()
            vue.$message.closeAll()
            vue.$message({showClose: true, message: '关注成功', type: 'success', offset: 50});
        }
    })
}