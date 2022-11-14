


// 饮食数据接口
ajax({
    url:'http://175.178.7.180:8080/team_project/food/allGroup',
    type:'GET',
    async:true,
    data:{},
    success:function(resp){
        // 首页分类
       const Class = document.querySelectorAll('.part3 .page1>div');
        for(let i=0;i<Class.length;i++){
           Class[i].querySelector('p').innerHTML = resp.data.groups[i].group;
    }
        
    },error:function(error){
        console.log(error);
    }
})
    








    //1.封装Ajax函数
    function ajax(options) {
        let xhr = null;
        let params = formsParams(options.data);
        //创建对象
        if (window.XMLHttpRequest) {
            xhr = new XMLHttpRequest()
        } else {
            xhr = new ActiveXObject("Microsoft.XMLHTTP");
        }
        // 连接
        if (options.type == "GET") {
            xhr.open(options.type, options.url + "?" + params, options.async);
            xhr.send(null)
        } else if (options.type == "POST") {
            xhr.open(options.type, options.url, options.async);
            xhr.setRequestHeader("Content-Type", "multipart/form-data");
            xhr.send(params);
        }
        xhr.onreadystatechange = function() {
            if (xhr.readyState == 4 && xhr.status == 200) {
                options.success(JSON.parse(xhr.responseText));
            }
        }

        function formsParams(data) {
            let arr = [];
            for (let prop in data) {
                arr.push(prop + "=" + data[prop]);
            }
            return arr.join("&");
        }

    }

    // 退出登录



