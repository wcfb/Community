new Vue({
    el: "#app",
    data () {
        return {
            messageType: ''
        }
    },
    mounted(){
        //确认登录
        confirm()
        this.comments()
    },
    methods: {
        follow(id){
            follow(id)
        },
        comments (){
            this.messageType = '收到的评论'
            getData("http://localhost:8080/author/comment",null, function (response) {
                if (response.data.code == 200){
                    setCommentsLi(response.data.data)
                }
            })
        },
        likes(){
            this.messageType = '收到的喜欢'
            getData("http://localhost:8080/author/likes",null, function (response) {
                if (response.data.code == 200){
                    setLikesLi(response.data.data)
                }
            })
        },
        follows(){
            this.messageType = '关注你的人'
            getData("http://localhost:8080/author/follows",null, function (response) {
                if (response.data.code == 200){
                    setFollowsLi(response.data.data)
                }
            })
        },
        others(){
            this.messageType = '其他消息'
            setOthersLi()
        }
    }
})

/**
 * 确认登录
 */
function confirm() {
    confirmLogin()
}

/**
 * 关注
 * @param id
 */
function follow(id){
}

/**
 * 添加列表
 */
function setCommentsLi(data) {
    console.log(data)
    var innerHTML = "";
    for(var i=0;i<data.length;i++){
        innerHTML +=
            "<li class=\"\">\n" +
            "    <a href=\"\" class=\"avatar\">\n" +
            "        <img src=\"" +data[i].head+ "\">\n" +
            "    </a>\n" +
            "    <div class=\"info\">\n" +
            "        <div>\n" +
            "            <a class=\"user\" href=\"\">" + data[i].name + "</a>\n";
        if (data[i].type == 1){
            innerHTML +=
                "<span class=\"comment-slogan\">在文章 </span>\n";
        } else{
            innerHTML +=
                "<span class=\"comment-slogan\">在评论 </span>\n";
        }
        innerHTML +=
            "            <a href=\"http://localhost:8080/articleShow.html?" + data[i].id + "\">" +
            "            "  +data[i].article+"</a>\n" +
            "            <span class=\"comment-slogan\">中写了一条新评论</span>\n" +
            "        </div>\n" +
            "        <div class=\"time\">"+data[i].time+"</div>\n" +
            "    </div>\n" +
            "    <p>"+data[i].comment+"\n" +
            "        <a href=\"\" rel=\"nofollow\" target=\"\">\n" +
            "            \n" +
            "        </a>\n" +
            "    </p>\n" +
            // "    <div class=\"meta\">\n" +
            // "        <a class=\"function-btn\">\n" +
            // "            <i class=\"iconfont ic-comment\"></i>\n" +
            // "            <span>回复</span>\n" +
            // "        </a>\n" +
            // "        <a href=\"\" class=\"function-btn\">\n" +
            // "            <i class=\"iconfont ic-go\"></i>\n" +
            // "            <span>查看对话</span>\n" +
            // "        </a>\n" +
            // "        <a class=\"report\" reportable-type=\"[object Object]\">\n" +
            // "            <span>举报</span>\n" +
            // "        </a>\n" +
            // "    </div>\n" +
            "</li>";
    }
    document.getElementById("messageList").innerHTML = innerHTML
}

function setLikesLi(data) {
    console.log(data)
    var innerHTML = "";
    for(var i =0;i<data.length;i++){
        innerHTML +=
            "<li class=\"\">\n" +
            "    <a href=\"\" class=\"avatar\">\n" +
            "        <img src=\""+data[i].head+"\">\n" +
            "    </a>\n" +
            "    <div class=\"info\">\n" +
            "        <div>\n" +
            "            <a class=\"user\" href=\"\">"+data[i].name+"</a>\n" +
            "            <span class=\"comment-slogan\">点赞：</span>\n" +
            "            <a href=\"http://localhost:8080/articleShow.html?" + data[i].id + "\">"
            +                   data[i].article+"</a>\n" +
            "        </div>\n" +
            "        <div class=\"time\">"+data[i].time+"</div>\n" +
            "    </div>\n" +
            "</li>";
    }
    document.getElementById("messageList").innerHTML = innerHTML;

}

function setFollowsLi(data) {
    console.log(data)
    var innerHTML = "";
    for(var i=0;i<data.length;i++){
        innerHTML +=
            "<li class=\"\">\n" +
            "    <a href=\"\" class=\"avatar\">\n" +
            "        <img src=\""+data[i].head+"\">\n" +
            "    </a>\n" +
            "    <div class=\"info\">\n" +
            "        <div>\n" +
            "            <a class=\"user\" href=\"\">"+ data[i].user +"</a>\n" +
            "            <span class=\"comment-slogan\">关注了你</span>\n" +
            "            <button type=\"button\" class=\"el-button el-button--success is-round\" tabindex=\"0\" " +
            "               onclick='follow("+data[i].account+")' style='float: right'><span>关注</span></button>" +
            "        </div>\n" +
            "        <div class=\"time\">"+data[i].time+"</div>\n" +
            "    </div>\n" +
            "</li>";
    }
    document.getElementById("messageList").innerHTML = innerHTML;
}

function setOthersLi() {
    document.getElementById("messageList").innerHTML =
        "<li class=\"\">\n" +
        "    <span class=\"comment-slogan\">无</span>\n" +
        "</li>";
}

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

/**
 * 从后台获取数据
 */
function getData(url,data, callback){
    axios.post(url, {data}).then(callback);
}