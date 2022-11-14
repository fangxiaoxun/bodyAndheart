window.addEventListener('load', () => {
    ajax({
        url: "http://localhost:8080/team_project/user/userExist", // url---->地址
        type: "GET", // type ---> 请求方式
        async: true, // async----> 同步：false，异步：true 
        data: {}, //传入信息
        success: function(result) { //返回接受信息
            console.log(result);
        },
        error: function(error) {
            console.log(error);
        }
    })
})