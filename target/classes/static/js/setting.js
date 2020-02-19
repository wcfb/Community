new Vue({
    el: '#app',
    data() {
        return {
            radio : 0,
            email: '59***@qq.com',
            phone: '173****0704',
            head: '',
            name: '',
        }
    },
    methods : {
        submit () {
            saveImg(this)
        }
    }
})

/**
 * 保存图片到本地
 */
function saveImg(vue) {
    var formData = new FormData();
    var file = ($("#file"))[0].files[0];
    if (file == null){
        new Vue().$message.error('图片不能为空');
        return;
    }
    formData.append("file", file);
    ajaxFile("/img/imgUpload", false, formData, function (response) {
        console.log(response)
        axios.post('http://localhost:8080/user/set', {
            'head': response,
            'name': vue.name,
            'sex': vue.radio
        }).then(function (responce) {
            if (responce.data.code == 200) {
                vue.$message({showClose: true, message: '修改成功', type: 'success',offset: 50});
            }
        }).catch(function (error) {
            console.log(error);
        });

    });
}