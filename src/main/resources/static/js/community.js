new Vue({
    el: '#app',
    data() {
        return {
            activeIndex: '1',
            restaurants: [],
            input: '',
            headUrl: '',
            contentUrl: 'find.html',
            iframeHeight: 0
        };
    },
    methods: {
        handleSelect(key, keyPath) {
            if (key == 1){
                this.setIframe('find.html')
            } else if (key == 2){
                this.setIframe('follow.html')
            } else if (key == 3){
                this.setIframe('message.html')
            }
        },
        search() {
            this.setIframe('search.html?' + this.input)
        },
        head() {
            confirm()
            this.setIframe('user.html')
        },
        write() {
            confirm()
            setUrl('writeArticle.html')
        },
        setIframe(contentUrl){
            setIframe(contentUrl)
        },
        setHeight(){
            this.iframeHeight = document.body.clientHeight-document.getElementById("content").offsetTop
        },
        getHead(){
            var vue = this
            axios.post('http://localhost:8080/user/getHeadUrl', {})
                .then(response=>{
                    if (response.data.code == 200){
                        vue.headUrl = response.data.data
                    }
                })
        }
    },
    //初始化页面
    mounted(){
        this.setHeight()
        this.getHead()
    }
})

/**
 * 修改iframe的src
 * @param contentUrl
 */
function setIframe(contentUrl) {
    document.getElementById('content').setAttribute('src',contentUrl)
}

/**
 * 修改iframe的src
 * @param contentUrl
 */
function setUrl(contentUrl) {
    window.location.href=contentUrl
}

/**
 * 确认登录
 */
function confirm() {
    confirmLogin()
}