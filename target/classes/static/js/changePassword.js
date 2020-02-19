new Vue({
    el: '#app',
    data() {
        return {
            password: '',
            confirmPassword: '',
        }
    },
    mounted(){
        this.getCaptcha()
    },
    methods: {
        change(){
            var vue = this
            if (this.password == null || this.confirmPassword == null
                || this.password == '' || this.confirmPassword == ''){
                vue.$message.error('密码不能为空')
                return;
            }
            if (this.password != this.confirmPassword){
                vue.$message.error('密码不一致')
                return;
            }
            if(this.password.length<6){
                vue.$message.error('密码强度太弱')
                return;
            }
            axios.post('http://localhost:8080/user/changePassword', {
                'email' : this.getAccount(),
                'password' : this.password
            }).then(function (responce) {
                if (responce.data.code == 200){
                    vue.$message({showClose: true, message: '修改成功，现在可以登录了', type: 'success',offset: 50});
                    window.setTimeout(vue.skip, 1000);
                }
            }).catch(function (error) {
                    console.log(error);
                });
        },
        //根据url得到账号
        getAccount(){
            //获取url中"?"符后的字串
            var url = location.search;
            return url.substr(1)
        },
        skip(){
            window.parent.location.href="login.html"
        },
    }
})
