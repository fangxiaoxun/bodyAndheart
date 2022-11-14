window.onload=function(){
    // 首页显示用户信息
    const user_id = document.querySelector('.dropnav #id p');
    const logoff = document.getElementById('logoff');
    const loginBox = document.querySelector('.loginBox');
    const island = document.querySelector('header .island');
   
    ajax({
        url:"http://localhost:8080/team_project/user/userExist",
        type:"GET",
        async:true,
        data:{},
        success: function(resp){
            console.log(resp);
            //已登录 
            if(resp.message=='1'){
                loginBox.classList.add('Login');
                // 显示用户信息、
                user_id.innerHTML = "ID:"+resp.data.user.id;
                let iconMark = resp.data.user.iconMark;
                // 用户登录头像
                    // 退出登录
                ajax({
                    url:"http://localhost:8080/team_project/icon/getIcon",
                    type:"GET",
                    async:true,
                    data:{
                        "iconMark":iconMark,
                    },
                    success: function(resp){
                        const user_image = document.querySelector('.loginBox img');
                              user_image.src=resp.data.icon.iconUrl;
                            // 退出登录
                    },error:function(error){
                        console.log(error);
                    }
                })
            }else{//未登录
                loginBox.classList.remove('Login');
            }
                
        }
    });

    // 退出登录
    logoff.addEventListener('click',()=>{
        loginBox.classList.remove('Login');
        ajax({
            url:"http://localhost:8080/team_project/user/logoff",
            type:"GET",
            async:true,
            success: function(){
                logoff.addEventListener('click',()=>{
                    console.log("退出登录");
                    user_id.innerHTML ='';
                })
            },error:function(error){
                console.log(error);
            }
        })
    });
}