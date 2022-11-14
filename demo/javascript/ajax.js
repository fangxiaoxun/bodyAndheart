window.addEventListener('load', () => {

    // //1.封装Ajax函数

    // function ajax(options) {
    //     // const p = new Promise((resolve, reject) => {
    //     let xhr = null;
    //     let params = formsParams(options.data);
    //     //创建对象
    //     if (window.XMLHttpRequest) {
    //         xhr = new XMLHttpRequest()
    //     } else {
    //         xhr = new ActiveXObject("Microsoft.XMLHTTP");
    //     }
    //     // 连接
    //     if (options.type == "GET") {
    //         xhr.open(options.type, options.url + "?" + params, options.async);
    //         xhr.send(null)
    //     } else if (options.type == "POST") {
    //         xhr.open(options.type, options.url, options.async);
    //         xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    //         xhr.send(params);
    //     }
    //     xhr.onreadystatechange = function() {
    //         if (xhr.readyState == 4 && xhr.status == 200) {
    //             // resolve(options.success(JSON.parse(xhr.responseText)));
    //             options.success(JSON.parse(xhr.responseText));
    //         }
    //         // if (xhr.status !== 1 && xhr.status !== 200) {
    //         //     reject(options.error(xhr.status));
    //         // }
    //     }

    //     function formsParams(data) {
    //         let arr = [];
    //         for (let prop in data) {
    //             arr.push(prop + "=" + data[prop]);
    //         }
    //         return arr.join("&");
    //     }
    //     // })

    // }




    // //注册
    // // ajax({
    // //     url: "http://localhost:8080/team_project/user/signIn", // url---->地址
    // //     type: "POST", // type ---> 请求方式
    // //     async: true, // async----> 同步：false，异步：true 
    // //     data: {

    // //     }, //传入信息
    // //     success: function(data) { //返回接受信息
    // //         console.log(data);
    // //     },
    // //     error: function(error) {
    // //         console.log(error);
    // //     }
    // // });

    // //登录
    // ajax({
    //     url: "http://localhost:8080/team_project/user/login", // url---->地址
    //     type: "POST", // type ---> 请求方式
    //     async: true, // async----> 同步：false，异步：true 
    //     data: {
    //         "id": "1547825",
    //         "password": "123456"
    //     }, //传入信息
    //     success: function(data) { //返回接受信息
    //             console.log(data);
    //         }
    //         // ,
    //         // error: function(error) {
    //         //     console.log(error);
    //         // }
    // });
})