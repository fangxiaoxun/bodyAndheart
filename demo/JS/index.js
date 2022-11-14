

// 元素是否出现在视口
const wrapper = document.querySelectorAll('main>div');

// 出现特效,待改
const images = document.querySelectorAll('.part1 .left .image'),
      introduce = document.querySelector('.part1 .introduce'),
      welc = document.querySelector('.part1 .welc'),
      theme = document.querySelector('.part1 .bottom h1');
// console.log(part1.getBoundingClientRect());
const node=document.querySelectorAll("main div,footer div,main a,footer a,main i,footer i,main span footer span");
// const imgArr = document.querySelazyloadlectorAll('img');

window.addEventListener('load',()=>{
    // 动态页面高度
    const parts = document.querySelectorAll('main>div');
    for(let i of parts){
        i.style.height = window.screen.height+'px';
    }
    // 


    navColor();
    // 懒加载调用
    // lazyload()
    
})
// 导航条
const navItem = document.querySelectorAll('#navbarSupportedContent li '),
      move = document.querySelector('.hori-selector');
for(let i = 0;i<navItem.length;i++){
    navItem[i].addEventListener('click',function(){
        for(let j = 0;j<navItem.length;j++){
            navItem[j].classList.remove('active');
        }
        this.classList.add('active');
        // move.style.width = this.offsetWidth+"px";
        move.style.left =this.offsetLeft+"px";
    });
}

// 底部菜单按钮
const menu_box = document.querySelector('.menu_box'),
      menu_btn = document.querySelector('.menu_btn');

menu_btn.addEventListener('click',()=>{
    menu_box.classList.toggle('active')
})


// 搜索框
const clickbtn = document.querySelector('.navRight .clickbtn'),
      optBox = document.querySelector('.navRight .optBox');
clickbtn.addEventListener('click',()=>{
    clickbtn.classList.toggle('active');
})


// 导航条点击分类
const optBtn = document.querySelector('.navRight .optBtn');

optBtn.addEventListener('click',()=>{
    optBtn.classList.toggle('active');
    optBox.classList.remove('checked')
});
const clicked = document.querySelector('.navRight .optBtn span'),
      searchList = document.querySelectorAll('.navRight .optList li');
for(let i = 0;i<searchList.length;i++){
    searchList[i].addEventListener('click',function(){
        clicked.innerHTML = this.querySelector('a').innerHTML
        optBox.classList.add('checked');
        optBtn.classList.remove('active');
    })
}

// 导航条颜色改变
function navColor(){
    const navColor = document.querySelectorAll('#navbarSupportedContent ul li a');
    if(parts[0].offsetTop>=0){
        for(let i of navColor){
            i.style.color='rgba(255,255,255,0.5)';
        }
    }else{
        for(let i of navColor){
            i.style.color='';
        }
    }
}

// 滚动效果
// 获取所有板块
const wrap = document.querySelectorAll('main>div>div');
      parts = document.querySelectorAll('main>div');
   
// index记位
var index=0;
var state = true;
function changestate() {
    state = true;
}

let imgArr =new Array();
for(let i=0;i<parts.length;i++){
    imgArr[i] = new Array();
    imgArr[i] = parts[i].querySelectorAll('img');
}

function lazyload(){
    for(let i =0 ;i<parts.length;i++){
        imgArr[i] = new Array();
        imgArr[i] = parts[i].querySelectorAll('img');
        if(parts[i].classList.contains('curr')){
            console.log('lazyload');
            for(let j=0;j<imgArr[i].length;j++){
                if(imgArr[i][j].src==''){
                    imgArr[i][j].src = imgArr[i][j].getAttribute('data-src')
                  
                
                }
            }
        }
        
    }
}

window.onscroll = function(){
    lazyload();
}

window.onwheel = function (event) {

    // 为止
    if (state) {
        lazyload();
        state = false;
        // 防止连续触发
        setTimeout(changestate, 800);
        if (event.wheelDeltaY<0) {//向下滚动
            ++index;
            // 最后
            if(index>wrap.length-1)
                index=wrap.length-1;
            parts[index].classList.add('curr');
            console.log(parts[index]);

            parts[index-1].style.top = '-'+parts[index-1].offsetHeight+'px';
            parts[index-1].style.position='';
        }

        if (event.wheelDeltaY >0){// 向上滚动
            console.log(index);
            // 最前
            if(index<=1){
                index=0;
            parts[index].style.position='';
            }
            else{
                --index;
                parts[index-1].style.position='';

            }
            parts[index].classList.add('curr');
            parts[index+1].classList.remove('curr');
            parts[index].style.top='0px';

        }
        
            navColor();
    }
}

// 首页轮播图
const box = document.querySelectorAll('.part1 .box');
var num=0;
var time;

function play(obj){
    for(let i=0; i<box.length;i++){
        box[i].classList.remove('curr');
    }
    obj.classList.add('curr');
}
function auto(){
    time=setInterval(function(){
        num%=box.length;
        play(box[num]);
        num++;
    },3500);
}

auto();





// //泡泡效果
const bm = document.querySelector('#bubble_machine'),
      bubble_rate = 500

function addBubble() {
    // 动态创建泡泡
    let b = document.createElement('div')
    b.className = 'bubble'
    // 泡泡的随机大小,位置
    b.style.width = (Math.random()*100) + 28 + 'px'
    b.style.left = Math.random()*90 + '%'
    //上升时间
    b.style.animationDuration = Math.floor(Math.random()*10) + 8 + 's'
    // 泡泡消失
    b.onanimationend = function() {
        this.remove()
    }  
    bm.appendChild(b)   
    setTimeout(addBubble, bubble_rate)
}
addBubble();



// 点击跳转
const menu_icon = document.querySelectorAll('.menu_list .icon');
for(let i = 0;i< menu_icon.length;i++){
    menu_icon[i].index = i+1;
    menu_icon[i].addEventListener('click',function(){
        navColor();
        index = this.index;
        // 板块快捷按钮
        if(this.index<menu_icon.length){
            if(this.index>0&&parts[this.index].offsetTop<0){
                console.log('向上'); 
                    
                parts[this.index].classList.add('curr');
                for(let j =0;j<parts.length;j++){
                    if(j>this.index){
                        parts[j].classList.remove('curr');
                        parts[j].style.top=0;
                        parts[j].style.position='';

                    }
                    if(j<this.index){
                        parts[j].style.position=''
                    }
                }
                parts[this.index].style.top=0;
            }
            else {     
                // 
                console.log('向下');
                if(this.index>parts.length-1)
                    this.index=parts.length-1;  
                parts[this.index].classList.add('curr');
                for(let j =0;j<parts.length;j++){
                    if(j<this.index){
                        parts[j].style.top = '-'+parts[j].offsetHeight+'px';
                    }
                }
            }
        }
        // 回到顶部
        else{
            index=0;
            for(let j=0;j<parts.length;j++){
                parts[j].classList.remove('curr');
                parts[j].style.top='';
                // parts[j].style.position=''
            }
            parts[0].classList.add('curr');
            parts[0].style.top=0;
        }
    });
}


// 菜谱tab
const tab_li = document.querySelectorAll('.part3 .tab li'),
      click_block = document.querySelector('.part3 .tab .click_block'),
      dietCon = document.querySelectorAll('.part3 .content')

tabMove(tab_li,click_block,true,dietCon);
// 心理部分
const PTab_li = document.querySelectorAll('.part4 .tab li'),
      move_block = document.querySelector('.part4 .tab .move_block');
// tabMove(PTab_li,move_block);



// 获取元素高度距离
function getTop(e) {
    let T = e.offsetTop;
    while(e = e.offsetParent) {
        T += e.offsetTop;
    }
    return T;
}




