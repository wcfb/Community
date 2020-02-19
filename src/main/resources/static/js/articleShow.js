new Vue({
    el: '#app',
    data() {
        return {
            head: '',
            title: '',
            author: '',
            authorId: '',
            authorHead: '',
            time: '',
            word: 0,
            look: 0,
            liked: 0,
            wordSum: 0,
            likedSum: 0,
            fansSum: 0,
            comment: '',
        }
    },
    mounted() {
        this.load()
        this.loadComment()
    },
    methods: {
        load() {
            var vue = this
            console.log(this.getArticle())
            axios.post('http://localhost:8080/article/info/' + this.getArticle(), {}).then(function (responce) {
                if (responce.data.code == 200) {
                    console.log(responce.data.data)
                    vue.title = responce.data.data.title
                    vue.author = responce.data.data.author
                    vue.authorId = responce.data.data.authorId
                    vue.authorHead = responce.data.data.authorHead
                    vue.time = responce.data.data.time
                    vue.word = responce.data.data.word
                    vue.look = responce.data.data.look
                    vue.liked = responce.data.data.liked
                    vue.wordSum = responce.data.data.wordSum
                    vue.likedSum = responce.data.data.likedSum
                    vue.fansSum = responce.data.data.fansSum
                    mdSwitch(responce.data.data.content)
                }
            }).catch(function (error) {
                console.log(error);
            });
            vue.getHead();
            vue.getAside(vue);
        },
        //加载侧边数据
        getAside(vue){
            axios.post('http://localhost:8080/article/aside/' + vue.getArticle(), {}).then(function (responce) {
                if (responce.data.code == 200) {
                    aside(responce.data.data)
                }
            }).catch(function (error) {
                console.log(error);
            });
        },
        //根据url得到账号
        getArticle() {
            //获取url中"?"符后的字串
            var url = location.search;
            return url.substr(1)
        },
        //评论文章
        commentArticle() {
            var vue = this
            console.log(this.comment)
            //判断登录
            confirmLogin();
            axios.post('http://localhost:8080/comment/article', {
                'id': this.getArticle(),
                'type': 1,
                'comment': this.comment,
            }).then(function (responce) {
                if (responce.data.code == 200) {
                    vue.$message({showClose: true, message: '评论成功', type: 'success', offset: 50});
                    loadComment(responce.data.data)
                }
            })
        },
        //加载评论
        loadComment() {
            console.log('加载评论')
            var id = this.getArticle()
            axios.post('http://localhost:8080/article/comments/' + this.getArticle(), {})
                .then(function (responce) {
                    console.log(responce)
                    loadComment(responce.data.data)
                })
        },
        //喜欢文章
        likeArticle() {
            console.log('like this');
            confirmLogin();
            var vue = this
            axios.post('http://localhost:8080/article/like/' + this.getArticle(), {})
                .then(function (response) {
                    if (response.data.code == 200) {
                        vue.$message({showClose: true, message: '点赞成功', type: 'success', offset: 50});
                    }
                })
        },
        //获取头像
        getHead(){
            var vue = this
            axios.post('http://localhost:8080/user/getHeadUrl', {})
                .then(response=>{
                    if (response.data.code == 200){
                        vue.head = response.data.data
                    }
                })
        },
        //关注作者
        follow(){
            var vue = this
            confirmLogin()
            axios.post('http://localhost:8080/author/followAuthor', {
                'author': vue.authorId
            }).then(response=>{
                    if (response.data.code == 200){
                        vue.$message({showClose: true, message: '关注成功', type: 'success', offset: 50});
                    }
                })
        }
    }
})

/**
 * 加载文章
 */
function mdSwitch(mdValue) {
    var converter = new showdown.Converter();
    var html = converter.makeHtml(mdValue);
    document.getElementById("show-area").innerHTML = html;
}

function addConmment(id) {
    console.log('addConmment' + id)
    $("#commentView" + id).setAttribute("style", "display:block;");
}

function comment(id) {
    var data = $("#comment" + id).text()
    console.log(data)
}

function loadComment(data) {
    var innerHTML = "";
    for (var i = 0; i < data.length; i++) {
        innerHTML +=
            "<div id=\"commentList\" class=\"_2gPNSa\">\n" +
            "    <div class=\"_2IUqvs _3uuww8\">\n" +
            "        <a class=\"_1OhGeD\" rel=\"noopener noreferrer\">\n" +
            "            <img class=\"_1_jhXc\" src=\"" + data[i].head + "\" alt=\"\">\n" +
            "        </a>\n" +
            "        <div class=\"_1K9gkf\">\n" +
            "            <div class=\"_23G05g\">\n" +
            "                <a class=\"_1OhGeD\" rel=\"noopener noreferrer\">" + data[i].name + "</a>\n" +
            "            </div>\n" +
            "            <div class=\"_1xqkrI\">\n" +
            "                <span>" + data[i].time + "</span>\n" +
            "            </div>\n" +
            "            <div class=\"_2bDGm4\">" + data[i].comment + "</div>\n" +
            // "            <div class=\"_2ti5br\">\n" +
            // "                <div class=\"_3MyyYc\" onclick=\"addConmment("+data[i].commentId+")\">\n" +
            // "                    <span class=\"_1Jvkh4\" role=\"button\" tabindex=\"-1\" aria-label=\"添加评论\">\n" +
            // "                    <i aria-label=\"ic-reply\" class=\"anticon\">\n" +
            // "                        <svg width=\"1em\" height=\"1em\" fill=\"currentColor\" aria-hidden=\"true\"\n" +
            // "                             focusable=\"false\" class=\"\">\n" +
            // "                            <use xlink:href=\"#ic-reply\"></use>\n" +
            // "                        </svg>\n" +
            // "                    </i>\n" +
            // "                        回复\n" +
            // "                    </span>\n" +
            // "                </div>\n" +
            // "            </div>\n" +
            "            <div>\n" +
            // "                <div id=\"commentView\" class=\"_3GKFE3\" style=\"display: none\">\n" +
            // "                    <textarea id=\"commentView\" "+data[i].commentId+" class=\"_1u_H4i\" placeholder=\"写下你的评论...\">\n" +
            // "                    </textarea>\n" +
            // "                    <div style=\"align-content: center; align-items: center\">\n" +
            // "                        <button type=\"button\" onclick=\"comment("+data[i].commentId+")\"\n" +
            // "                                class=\"el-button el-button--info el-button--medium is-round\"\n" +
            // "                                style=\"float: right;margin-top: 5px;\">\n" +
            // "                            <span>评论</span>\n" +
            // "                        </button>\n" +
            // "                    </div>\n" +
            // "                </div>\n" +
            "            </div>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n";
    }
    document.getElementById("commentList").innerHTML = innerHTML
}

/**
 * 加载侧边数据
 * @param data
 */
function aside(data) {
    var innerHTML = "";
    for (var i = 0; i < data.length; i++) {
        innerHTML +=
            "<div class=\"_26Hhi2\" role=\"listitem\">\n" +
            "    <div class=\"_3TNGId\" title=\"\">\n" +
            "        <a class=\"_2ER8Tt _1OhGeD\" href=\"http://localhost:8080/articleShow.html?"
            +           data[i].id + "\" rel=\"noopener noreferrer\">" + data[i].title +
            "        </a>\n" +
            "    </div>\n" +
            "    <div class=\"DfvGP9\">阅读 "+data[i].look+"</div>\n" +
            "</div>";
    }
    document.getElementById("aside").innerHTML = innerHTML
}

/**
 * 从后台获取数据
 */
function getData(url, data, callback) {
    axios.post(url, {data}).then(callback);
}