new Vue({
    el: '#app',
    data() {
        return {
            email: ''
        }
    },
    mounted(){
    },
    methods: {
        find(){
            var vue = this
            if(vue.email.indexOf('@') == -1){
                vue.$message.error('邮箱格式不正确')
                return;
            }
            vue.$message({showClose: true, message: '已发送短信', type: 'success',offset: 50});
            axios.post('http://localhost:8080/user/retrieve', {
                'email' : this.email
            }).then(function (responce) {
                if (responce.data.code == 200){
                    window.setTimeout(vue.skip, 1000);
                } else {
                    vue.$message.error(responce.data.value)
                }
            })
        },
        skip(){
            window.parent.location.href="login.html"
        },
    }
})
