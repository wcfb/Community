new Vue({
  el: '#app',
  data() {
    return {
      loginAccount: '',
      loginPassword: '',
      registerPassword:'',
      account:'',
      phone: '',
      email:'',
      uuid:'',
      captcha:'',
      captchaPath:'',
      activeName: 'login'
    }
  },
  mounted(){
      this.getCaptcha()
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    // 获取验证码
    getCaptcha () {
        this.uuid = this.getUUID()
        this.captchaPath = 'http://localhost:8080/user/captcha.jpg?uuid='+this.uuid
    },
    login() {
        var message = this
        message.$message.closeAll()
        axios.post('http://localhost:8080/user/login', {
          'account' : this.loginAccount,
          'password' : this.loginPassword,
          'uuid': this.uuid,
          'captcha':this.captcha
          }).then(function (responce) {
            if (responce.data.code == 200){
                // document.cookie = "token="+ responce.data.data
                window.location.href="community.html"
            } else {
                message.$message.error(responce.data.value)
            }
          })
          .catch(function (error) {
            console.log(error);
        });
    },
    register() {
        var message = this
        message.$message.closeAll()
        if(this.registerPassword == null || this.registerPassword == ''){
            message.$message.error('密码为空')
            return;
        }
        if(this.registerPassword.length<6){
            message.$message.error('密码强度太弱')
            return;
        }
        axios.post('http://localhost:8080/user/register', {
            'name' : this.account,
            'phone' : this.phone,
            'email' : this.email,
            'password': this.registerPassword
        }).then(function (responce) {
            if (responce.data.code == 200){
                message.$message({showClose: true, message: '注册成功，现在可以登录了', type: 'success',offset: 50});
                window.setTimeout(message.skip, 1000);
            } else {
                message.$message.error(responce.data.value)
            }
        }).catch(function (error) {
                console.log(error);
            });
    },
    skip(){
      window.location.href="login.html"
    },
    /**
     * 得到uuid
     * @returns {string}
     */
    getUUID () {
          return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
              return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
          })
      }
  }
})
