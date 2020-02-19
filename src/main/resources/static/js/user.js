new Vue({
    el: '#app',
    data() {
        return{
            followVisible: true,
            settingVisible: true,
            user: '',
            head: '',
            article: '',
            follow: '',
            word: '',
            fans: '',
            liked: ''
        }
    },
    mounted (){
        document.getElementById("articleUl").innerHTML = "<li></li>\n";
        this.load()
    },
    methods: {
        load() {
            var vue = this
            axios.post('http://localhost:8080/author/info/' + this.getUser(), {}).then(function (responce) {
                if (responce.data.code == 200) {
                    vue.followVisible = responce.data.data.followVisible
                    vue.settingVisible = responce.data.data.settingVisible
                    vue.user = responce.data.data.user
                    vue.head = responce.data.data.head
                    vue.article = responce.data.data.article
                    vue.follow = responce.data.data.follow
                    vue.word = responce.data.data.word
                    vue.fans = responce.data.data.fans
                    vue.liked = responce.data.data.liked
                    vue.addArticle(responce.data.data.articleList)
                }
            })
        },
        followAuthor(){
            follow(this.getUser());
        },
        setting(){
            setUrl('setting.html')
        },
        //根据url得到账号
        getUser(){
            //获取url中"?"符后的字串
            var url = location.search;
            if (url.substr(1) == null || url.substr(1) == ''){
                return '0';
            }
            return url.substr(1);
        },
        addArticle(articleList){
            var innerHTML = "";
            for(var i=0;i<articleList.length;i++){
                innerHTML +=
                    "<li data-note-id=\"59789499\" class=\"\">\n" +
                    "    <div class=\"content \">\n" +
                    "        <a class=\"title\"  href=\"http://localhost:8080/articleShow.html?" + articleList[i].id + "\">" + articleList[i].title + "</a>\n" +
                    "        <p class=\"abstract\">\n" + articleList[i].content +
                    "        </p>\n" +
                    "        <div class=\"meta\">\n" +
                    "            <a>\n" +
                    "                <i class=\"iconfont ic-list-read\"></i>" + articleList[i].look + "\n" +
                    "            </a> <a>\n" +
                    "            <i class=\"iconfont ic-list-comments\"></i>\n" + articleList[i].comment +
                    "        </a> <span><i class=\"iconfont ic-list-like\"></i>" + articleList[i].liked + "</span>\n" +
                    "            <span class=\"time\" data-shared-at=\"2020-01-31T16:31:52+08:00\">" + articleList[i].createTime + "</span>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</li>";
            }
            document.getElementById("articleUl").innerHTML+=innerHTML
        }
    }
})

/**
 * 修改iframe的src
 * @param contentUrl
 */
function setUrl(contentUrl) {
    window.location.href=contentUrl
}

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