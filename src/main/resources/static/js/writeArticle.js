new Vue({
    el: "#app",
    data(){
        return{
            // preview: false
        }
    },
    mounted(){
        //确认登录
        confirm()
    },
    methods: {

    }
})
function mdSwitch() {
    var mdValue = document.getElementById("md-area").value;
    var converter = new showdown.Converter();
    var html = converter.makeHtml(mdValue);
    document.getElementById("show-area").innerHTML = html;
}

/**
 * 保存内容
 */
function save() {
    var article = {
        title: $('#title').val(),
        content: $('#md-area').val(),
    };
    ajax('/article/publishArticle',true, JSON.stringify(article), saveCallback);
}

/**
 * 保存文章成功回调
 * @param result
 */
function saveCallback(result) {
    new Vue().$message({showClose: true, message: '发表成功', type: 'success',offset: 50});
    window.setTimeout(skip, 1000);
}

function skip() {
    window.location.replace("http://localhost:8080/community.html");
}
/**
 * 添加图片
 * @param image
 */
function addImg(image) {
    $('#md-area').val($('#md-area').val() + '!['+image+'](http://localhost:8080/img/'+image+')  ');
}

/**
 * 保存图片到本地
 */
function saveImg() {
    var formData = new FormData();
    var file = ($("#file"))[0].files[0];
    if (file == null){
        new Vue().$message.error('图片不能为空');
        return;
    }
    formData.append("file", file);
    ajaxFile("/img/imgUpload", false, formData, callback);
}

/**
 * 回调
 * @param result
 */
function callback(result) {
    addImg(result);
}

/**
 * 确认登录
 */
function confirm() {
    confirmLogin()
}