
//获取验证码
function getyanzhengma(){

    $.post(alldata.path+"user/createCode","",function (resu) {
        if(resu != ""){//如果返回来的信息说明提交的信息为正确的
            $("#inp4").val(resu);
        }
        else{//如果返回来的信息说明提供的信息为错误的
            console.log("信息为错误的")
        }
    });
}

function confirm(){
    var tel=$("#inp1").val();//获取页面中登录名和密码
    var pwd=$("#inp2").val();
    var yzm= $("#inp3").val();

    if($.trim(tel) == ""){
        alert("用户名不能为空！");
        $("#inp1").val("");
        getyanzhengma();
        return;
    }
    if($.trim(pwd) == ""){
        alert("密码不能为空！");
        $("#inp2").val("");
        getyanzhengma();
        return;
    }
    if($.trim(yzm) == ""){
        alert("验证码不能为空！");
        getyanzhengma();
        return;

    }
    // 记住密码
    var passwordCookie = 2;
    if($("#check1").prop('checked') == true){
        passwordCookie = 1;
    }else{
        passwordCookie = 2;
    }

    //弹出到期提醒功能
    var expireRemind = 2;
    if($("#check2").prop('checked') == true){
        expireRemind = 1;
    }else{
        expireRemind = 2;
    }



    //====调用登录esb接口 START====
    $.ajax({
        url:alldata.path+'user/login',//相对应的esb接口地址
        type:'post',
        data:{
            code:yzm,
            userName:tel,
            password:pwd,
            cookie_pass:passwordCookie,
            cookie_info:expireRemind
        },//向服务器（接口）传递的参数
        success:function(data){//服务器（接口）返回来的数据
            data = JSON.parse(data);   //转json
            if(data.code==10000){//如果返回来的信息说明提交的信息为正确的

               $.cookie('uname', tel, { expires: 1, path: '/' });
                $.cookie('pword', pwd, { expires: 1, path: '/' });
                window.location.href=alldata.path+'/layui-html/index.html';//正确登录后页面跳转至
            }
            else{//如果返回来的信息说明提供的信息为错误的
                // if(tel != data.username){//判断是用户名还是密码错误，提示相应信息
                console.log(data.message)
                if(data.message.indexOf("验证码") != -1){
                    alert(data.message);
                    $("#inp3").val("")
                    getyanzhengma();
                }else{
                    alert(data.message);
                    $("#inp1").val("");
                    $("#inp2").val("");
                    getyanzhengma();
                }
                $("#inp4").val("");
                return false;

            }
        },
        error:function(){
            console.log("对接登入出错")
        }
    })
    //====调用登录esb接口 END====


}
//判断是否敲击了Enter键
$(document).keyup(function(event){
    if(event.keyCode ==13){
        $("#inp5").trigger("click");
    }
});


window.onload=function(){
    getyanzhengma();
}
