
/**
 * 判断是否登录
 * @returns {boolean}
 */
function checkLogin() {
    var login = false;
    ajax("/user/checkLogin",false, null, function (result) {
        if (result.token != null && result.token != 'null' && result.token != '') {
            login = true;
        }
    });
    return login;
}

/**
 * 请求后台
 * @param url
 * @param async
 * @param data
 * @param callback
 */
function ajax(url, async, data, callback) {
    $.ajax({
        type: "post",
        url: url,
        contentType: "application/json",
        async: async,
        data: data,
        dataType: "json",
        success: function (result) {
            callback(result);
        },
        error: function (result) {
            console.log(result)
        }
    });
}

/**
 * 文件传输
 * @param url
 * @param async
 * @param data
 * @param callback
 */
function ajaxFile(url, async, data, callback) {
    $.ajax({
        type:'POST',
        url:url,
        data:data,
        contentType:false,
        processData:false,
        asyn: async,
        mimeType:"multipart/form-data",
        success:function(data){
            callback(data);
        }
    });
}

/**
 * 得到链接地址的字
 * @param variable
 * @returns {*}
 */
function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return null;
}