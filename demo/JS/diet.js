// 未封装版本!!!
// 点击展示更多
const more = document.querySelector('.more');

// 高度变化
const list = document.querySelector('.content .list');
// // 点击展示更多
more.addEventListener('click',()=>{
    more.classList.toggle('close');
    list.classList.toggle('show')
})

// tab 切换
const tabLi = document.querySelectorAll('.tab>ul>li'),
      moveblock = document.querySelector('.tab .move');
      
const content = document.querySelectorAll('.content .active');
// 需要更多content







