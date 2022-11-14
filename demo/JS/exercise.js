const tabli = document.querySelectorAll('.tab li');
const content = document.querySelectorAll('.content');
for (let i = 0; i < tabli.length; i++) {
    tabli[i].index = i;
    tabli[i].addEventListener('click', function() {
        for (let j = 0; j < tabli.length; j++) {
            tabli[j].classList.remove('active');
            content[j].classList.add('hidden')
        }
        this.classList.add('active');
        content[this.index].classList.remove('hidden')
    })

}


// 点击页码
// 这里需要数据动态渲染
let pageContainer = document.querySelector('.Vcontainer');

const btn = document.querySelectorAll('.content1 button');
var index = 0;

btn[0].addEventListener('click', () => {
    // console.log(click);
    index++;
    console.log(index);
    if (index > 0) {
        pageContainer.style.left = 0;
    } else {
        changePage(index, 1)
    }
})
btn[1].addEventListener('click', () => {
    console.log(index);

    index--;
    // 加终点判断

    changePage(index, -1)
})





let allpage = document.querySelectorAll('.course ul');

function changePage(index, arr) {
    console.log('enter');
    let allpage = document.querySelectorAll('.course ul');
    // console.log(allpage[0].offsetWidth);
    let pageContainer = document.querySelector('.Vcontainer');
    console.log(index);
    pageContainer.style.left = index * allpage[0].offsetWidth + 'px'
    console.log(pageContainer.offsetLeft);
}




// 点击切换分类

const videourl = ['../video/【全身燃脂_力量训练】20分钟自重+60分钟哑铃力量训练 - 2.60分钟力量训练(Av633725627,P2).mp4',
    '../video/每天10min，轻松get直角肩＋少女背! 消除猥琐斜方肌!圆肩驼背必看！ - 1.直角肩(Av752502257,P1).mp4',
    '../video/【无器械健身合集】在家健身这一个视频就够了（家庭居家必备） - 1.无器械合集(Av801460785,P1).mp4',
    '../video/【全身燃脂_力量训练】20分钟自重+60分钟哑铃力量训练 - 2.60分钟力量训练(Av633725627,P2).mp4'
]
const titleContent = ['【全身燃脂_力量训练】20分钟自重+60分钟哑铃力量训练', '../video/每天10min，轻松get直角肩＋少女背! 消除猥琐斜方肌!圆肩驼背必看！', '【无器械健身合集】在家健身这一个视频就够了（家庭居家必备）', '【全身燃脂_力量训练】20分钟自重+60分钟哑铃力量训练']
const label = [
    ['健身教程', '瘦身', '有氧运动'],
    ['直角肩', '斜方肌', '圆肩驼背'],
    ['无器械健身', '在家健身'],
    ['燃脂', '力量训练', '哑铃']
];
// 获取元素
const video = document.querySelector('video');
const Vtitle = document.querySelector('.left p');
const labelBox = document.querySelectorAll('.labelBox .label span');

const navtop = document.querySelectorAll('.navtop>ul>li');
for (let i = 0; i < navtop.length; i++) {
    navtop[i].index = i;
    navtop[i].addEventListener('click', function() {
        for (let j = 0; j < navtop.length; j++) {
            navtop[j].classList.remove('active');
        }
        this.classList.add('active');
        video.src = videourl[this.index];
        Vtitle.innerHTML = titleContent[this.index];
        for (let k = 0; k < label[this.index].length; k++) {
            labelBox[k].innerHTML = label[this.index][k];
        }
    })
}



// 计算
const selectBox = document.querySelector('.index');
selectBox.addEventListener('click', () => {
    selectBox.classList.toggle('click');
    for (let i = 0; i < selectli.length; i++) {
        selectli[i].addEventListener('click', function() {
            num = this.id
            selectBox.querySelector('input').value = num;
        })
    }
})

// 获取活动系数
let num;
const selectli = document.querySelectorAll('.index li')





// 获取相关数据
// let H = document.querySelector('.height input').value;


// 获取显示元素
// document.querySelector('.BMI b');
// document.querySelector('.MI b');
// document.querySelector('.BMR b');
// console.log(document.querySelector('.BMI b'));
const check = document.querySelector('.btn');

check.addEventListener('click', function() {
    // 获取相关数据
    let H = document.querySelector('.height input').value;
    let W = document.querySelector('.weight input').value;
    let A = document.querySelector('.size input').value;
    console.log(A);
    let S;

    // 获取单选按钮
    let selectBox = document.querySelectorAll('form input');
    console.log(selectBox);
    for (let i = 0; i < selectBox.length; i++) {
        if (selectBox[i].checked) {
            S = selectBox[i].value;
        }
    }

    // 获取活动系数
    for (let i = 0; i < selectli.length; i++) {
        selectli[i].addEventListener('click', function() {
            num = this.id
        })
    }

    // keepTwoDecimal();

    // 获取性别
    let bmi = keepTwoDecimal(BMI(parseFloat(W), parseFloat(H)));
    let mr = keepTwoDecimal(MI(bmi, parseFloat(A), parseFloat(S)));
    let bmr = keepTwoDecimal(BMR(parseFloat(W), parseFloat(H), parseFloat(A)))
    let HRAT = keepTwoDecimal(heat(bmi, parseFloat(num)));
    console.log(HRAT);
    console.log(parseFloat(W));
    document.querySelector('.BMI b').innerHTML = bmi;
    document.querySelector('.MR b').innerHTML = mr;
    document.querySelector('.meta b').innerHTML = bmr;
    document.querySelector('.heat b').innerHTML = HRAT;
    //    这里

    // document.querySelector('.MBR b').innerHTML = MBR(parseFloat(W),parseFloat(H),parseFloat(A));


})






function BMI(weight, height) {
    height = height / 100
        // console.log(height);
    return weight / height / height;
}

function MI(bmi, age, sex) {
    return 1.2 * bmi + 0.23 * age - 5.4 - 10.8 * sex;
}

function BMR(weight, height, age) {
    // console.log(0);
    return 655 + (9.6 * weight) + (1.8 * height / 100) - (4.7 * age);
}

function heat(bmi, index) {
    return bmi * index;
}


// 健身板块 问答专区 start
//获取元素
const myself = document.querySelector('.myself');
const iconImg = myself.querySelector('.img'); //登录者的头像
const inputCase = myself.querySelector('.inputCase');
const ask_title = myself.querySelector('input'); //提问问题输入框
const ask_content = myself.querySelector('textarea'); //提问内容输入框
const btnPublic = myself.querySelector('.btnPublic');
const userQuestion = document.querySelector('.userQuestion');
const questionCase = userQuestion.querySelector('.questionCase');
const moreQuestion = userQuestion.querySelector('.moreQuestion');
const userImg = userQuestion.querySelector('.userImg');


//调用判断是否显示用户问答区函数
judgeShowUserQuestion();

//点击输入框,输入框拉伸
inputCase.addEventListener('click', (e) => {
    e.stopPropagation(); //阻止冒泡
    inputCase.style.height = 144 + 'px'
});
//点击空白处缩小输入框
document.addEventListener('click', (e) => {
    inputCase.style.height = 50 + 'px';
})

//调用输入框点击清空，回车填入函数
inputFocus(ask_title);

//调用接口获取用户信息
ajax({
    url: "http://localhost:8080/team_project/user/userExist", // url---->地址
    type: "GET", // type ---> 请求方式
    async: true, // async----> 同步：false，异步：true 
    data: {}, //传入信息
    success: function(result) { //返回接受信息
        if (result.message == 1) {
            let iconMark = result.data.user.iconMark;
            //调用获取个人头像函数
            getMyIconByMark(iconMark, iconImg);
            getMyIconByMark(iconMark, userImg);
        }
    },
    error: function(error) {
        console.log(error);
    }
});


//点击发布按钮，发布你的问题
btnPublic.addEventListener('click', () => {
    let title = ask_title.value;
    let body;
    if (ask_content.value != '') {
        body = ask_content.value;
    } else {
        body = '';
    }
    inputCase.style.height = 50 + 'px';
    //调用发布提问接口
    ajax({
        url: "http://localhost:8080/team_project/sport/releaseQuestion", // url---->地址
        type: "GET", // type ---> 请求方式
        async: true, // async----> 同步：false，异步：true 
        data: {
            "title": title,
            "body": body
        }, //传入信息
        success: function(result) { //返回接受信息
            console.log(result);
            let div = document.createElement('div');
            let str = ''
            str += '<div class="question">' + result.data.question.title + '</div>';
            str += '<div class="describeQuestion"><p>' + result.data.question.body + '</p></div>';
            str += '<div class="questionCount">';
            str += '    <span>回答</span><span>(0)</span>';
            str += '</div>';
            div.innerHTML = str;
            div.className = 'questionArea';
            questionCase.insertBefore(div, questionCase.children[0]);

            //调用判断是否显示用户问答区函数
            judgeShowUserQuestion();

        },
        error: function(error) {
            console.log(error);
        }
    });
})

//判断是否显示用户问答区
function judgeShowUserQuestion() {
    if (questionCase.children.length == 0) {
        userQuestion.style.display = 'none'; //隐藏用户问答区
    } else {
        userQuestion.style.display = 'block'; //显示用户问答区
        userQuestion.style.height = questionCase.children[0].offsetHeight + 80 + 'px';
        let flag = false;
        //点击更多提问,显示当前登录用户的全部问答
        moreQuestion.addEventListener('click', () => {
            if (!flag) { //如果关闭,则打开
                userQuestion.style.height = 'auto';
                flag = true;
            } else { //如果打开,则关闭
                userQuestion.style.height = questionCase.children[0].offsetHeight + 80 + 'px';
                flag = false;
            }
        });
    }
}


//获取随机问答
const getRandomQuestion = document.querySelector('.getRandomQuestion');
//调用获取随机问答接口
ajax({
    url: "http://localhost:8080/team_project/sport/getQuestions", // url---->地址
    type: "POST", // type ---> 请求方式
    async: true, // async----> 同步：false，异步：true 
    data: {
        "size": 4
    }, //传入信息
    success: function(result) { //返回接受信息
        console.log(result);

        for (let i = 0; i < result.data.questionsWithAnswers.length; i++) {
            console.log(result.data.questionsWithAnswers[i].question.quizzer)
            let str = '';
            str += '<div class="randomQuestion">';
            str += '    <div class="userImg"><img src="' + result.data.questionsWithAnswers[i].question.quizzer.iconUrl + '"/></div>';
            str += '    <h4><span>id:' + result.data.questionsWithAnswers[i].question.quizzer.id + '</span><span>的提问</span></h4>';
            str += '    <h5>期待着你的回答</h5>';
            str += '    <div class="questionCount">';
            str += '        <span>回答</span><span>(XX)</span>';
            str += '    </div>';
            str += '    <div class="writeAnAnswer">写回答</div>';
            str += '    <div class="question">' + result.data.questionsWithAnswers[i].question.title + '</div>';
            str += '    <div class="describeQuestion">';
            str += '        <p>' + result.data.questionsWithAnswers[i].question.body + '</p>';
            str += '    </div>';
            str += '    <div class="ask_underline"></div>';
            str += '</div>';
            getRandomQuestion.innerHTML += str;
        }

    },
    error: function(error) {
        console.log(error);
    }
});


// 健身板块 问答专区 end