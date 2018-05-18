/**
 * 用于操作 页面内容
 * */

function submitForm(){
    //判断是否校验通过
    if(!alldata.validationForm()){
        return;
    }
    var data = $("#myForm").serialize();
    //调用接口
    $.post(alldata.path+"dataX/addDataXRec",data,function (resu) {
        //将返回结果转成JSON
        //判断是否成功
        if(resu.code == "10000"){
            alert(resu.msg);
            $("#data-iframe",parent.document).attr("src","SyncList.html");
        }else{
            alert(resu.msg);
        }
    });


}
