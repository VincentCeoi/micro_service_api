/**
 * 用于操作 页面内容
 * */

function submitForm(flag){
    //判断是否校验通过
    if(!alldata.addUserValid(flag)){
        return;
    }
    var data = $("#myForm").serialize();
    //调用接口
    $.post(alldata.path+"user/addOrEditUser",data,function (resu) {
        //将返回结果转成JSON
        //判断是否成功
        if(resu.code == "10000"){
            alert(resu.msg);
            $("#data-iframe",parent.document).attr("src","../layui-html/userList.html?"+new Date().getTime());
        }else{
            alert(resu.msg);
        }
    });


}
