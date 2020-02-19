/**
 * 请求后台判断是否登录
 * @returns {Promise<*>}
 */
async function confirmLogin() {
    var vue = new Vue();
    await axios.post('http://localhost:8080/user/confirmLogin', {})
        .then(response=>{
            if (response.data.code != 200){
                vue.$message.closeAll()
                vue.$message.error('请先登录')
                window.setTimeout(toLogin, 1500);
            }
        })
}

/**
 * 跳转到登录页面
 */
function toLogin() {
    window.parent.location.href="login.html"
}