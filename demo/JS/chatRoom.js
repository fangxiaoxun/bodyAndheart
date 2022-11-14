const setRoomBtn = document.getElementById('setRoom');
const setroom = document.querySelector('.setroom');
const finished = document.getElementById('finished');
// 房间列表
const roomList = document.querySelector('.roomList');

setRoomBtn.addEventListener('click',function(){
    setroom.classList.toggle('hidden');
})


// // 向后端传新建房间信息并连接聊天房
// function setRoom(){
//     // 获取房间信息
//     let theme = document.querySelector('#themeInput').value;
//     let roomid = document.querySelector('#roomIdInput').value; 

//     // 添加房间
//     let div = document.createElement('div');
//     div.innerHTML = "<div class='id'><span>房间编号：</span><span class='roomId'>"+roomid+"</span></div>"+
//                     "<div class='theme'><span>房间主题：</span><i class='icon'></i><h2 class='roomTheme'>“" +theme+" ”</h2></div>"+
//                     "<div class='info'>"+
//                         "<div class='inline'><span class='inline'>"+inlineCount+"</span><span>人在线</span></div>"+
//                         "<div class='enter'></div>"+
//                     "</div>";
//     div.classList.add('room');
//     div.setAttribute('id',roomid);//绑定房间id
//     div.setAttribute('theme',theme)

//     // 插入至最前
//     let first_room=document.querySelector('.roomList:nth-child(1) .room');//得到页面的第一个元素。 
//     roomList.insertBefore(div,first_room);//在得到的第一个元素之前插入。
//     setroom.classList.add('hidden');

//     // 更新数组 //这个可能不用
//     rooms = document.querySelectorAll('.room');
//     enterRooms = document.querySelectorAll('.enter');//进入按钮
// }



// 进入房间
var rooms = document.querySelectorAll('.room');
var enterRooms = document.querySelectorAll('.enter');//进入按钮
const chatView = document.querySelector('.chatView');//聊天界面
// 使用事件委托来实现元素的点击
roomList.onclick = function (e){
    console.log(e.target);
    //判断是否为enter
    let className = e.target.className;
    if (className.indexOf("enter") > -1){
        setTimeout(function(){
            chatView.classList.remove('hidden');
        },200);
    }
}

// 退出房间
const leave = document.querySelector('.return');
leave.addEventListener('click',function(){
    // 关闭界面
    chatView.classList.add('hidden');
    // 可能需要清空聊天记录
})

// 随机获取可用房间号
const randomBtn = document.querySelector('.randomBtn');
randomBtn.addEventListener('click',() => {
    ajax({
        url: 'http://localhost:8080/team_project/room/getAvailable',
        type: 'GET',
        async: true,
        data: {},
        success: function(resp) {
            roomid = document.querySelector('#roomIdInput').value=resp.data.roomNum
        },
        error: function(error) {
            console.log(error);
        }
    })
});

// 创建房间
let inlineCount;//在线人数
// 房主连接websocket
finished.addEventListener('click',() => {
    // 显示聊天页面
    setTimeout(() => {
        chatView.classList.remove('hidden');
    }, 300);

       // 获取输入的房间信息
       let theme = document.querySelector('#themeInput').value;
       let roomid = document.querySelector('#roomIdInput').value;
       
    //    若不输入id则创建失败
       if (roomid == "") {
            alert("请输入房间ID");
            return;
        }

        // 用户信息需要sessionstory
        console.log(roomid);
        linkUrl(roomid,1547843,"fea7f657f56a2a448da7d4b535ee5e279caf3d9a",theme,2);
        
        // 这个地方得点击开启
        RTCengine.createWebsocket();
        // 初始化本地码流
        initLocalStream();
})


// 渲染当前存在的房间
function currRoom (){
    ajax({
        url: 'http://localhost:8080/team_project/room/chatRoom',
        type: 'GET',
        async: true,
        data: {},
        success: function(resp) {
            console.log(resp);
        },
        error: function(error) {
            console.log(error);
        }
    })
}