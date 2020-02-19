new Vue({
    el: '#app',
    data() {
        return {
            radio : 0,
            email: '',
            phone: '',
            head: '',
            name: '',
        }
    },
    mounted (){
      this.load()
    },
    methods : {
        submit () {
            saveImg(this)
        },
        //加载初始信息
        load(){
            var vue = this
            axios.post('http://localhost:8080/user/getUserData', {})
                .then(function (responce) {
                    vue.email = responce.data.data.email
                    vue.phone = responce.data.data.phone
                })
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