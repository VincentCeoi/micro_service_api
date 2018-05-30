//存储父级数据

var alldata = {
    //url 前缀
    path:"http://localhost:8084/lineHual/",
    /**
     * 校验表单非空数据
     * @returns {boolean}
     */
    validationForm:function (){
    //获取name对象
    var $name = $("input[name='name']");
    //校验
    if(!alldata.clickNotNull($name,"同步说明")){
        return false;
    }
    //获取选项值
    var expre = $("select[name='expre']").val();
    //判断
    if(expre != "day"){
        //获取name对象
        var $expressTime = $("input[name='expressTime']");
        //校验
        if(!alldata.clickNotNull($expressTime,"定时处理")){
            return false;
        }
        //判断是否在规定的限制内
        if($expressTime.val()< 0 || $expressTime.val() > 59){
            alert("定时处理只能是0 - 59之间！");
            return false;
        }
    }else{
        //获取name对象
        var $expressDate = $("input[name='expressDate']");
        //校验
        if(!alldata.clickNotNull($name,"定时处理")){
            return false;
        }
    }
    //获取数据源用户名
    var $outUserName = $("input[name='outUserName']");
    //校验
    if(!alldata.clickNotNull($outUserName,"数据源用户名")){
        return false;
    }
    //获取数据源密码
    var $outPassword = $("input[name='outPassword']");
    //校验
    if(!alldata.clickNotNull($outPassword,"数据源密码")){
        return false;
    }
    //获取接收数据源用户名
    var $inUserName = $("input[name='inUserName']");
    //校验
    if(!alldata.clickNotNull($inUserName,"接收数据源用户名")){
        return false;
    }
    //获取接收数据源密码
    var $inPassword = $("input[name='inPassword']");
    //校验
    if(!alldata.clickNotNull($inPassword,"接收数据源密码")){
        return false;
    }
    //获取通道数
    var $channelCount = $("input[name='channelCount']");
    //校验
    if(!alldata.clickNotNull($channelCount,"通道数")){
        return false;
    }
    //获取数据源IP地址
    var $outIPAddress = $("input[name='outIPAddress']");
    //校验
    if(!alldata.clickNotNull($outIPAddress,"数据源IP地址")){
        return false;
    }
    var $outPort = $("input[name='outPort']");
    //校验
    if(!alldata.clickNotNull($outPort,"数据源端口")){
        return false;
    }
    //接收数据源IP地址
    var $inIPAddress = $("input[name='inIPAddress']");
    //校验
    if(!alldata.clickNotNull($inIPAddress,"接收数据源IP地址")){
        return false;
    }
    //接收数据源端口
    var $inPort = $("input[name='inPort']");
    //校验
    if(!alldata.clickNotNull($inPort,"接收数据源端口")){
        return false;
    }
    //获取同步方式，进行判断
    var syncMethod = $("select[name='syncMethod']").val();
    //判断
    if(syncMethod == "columnSync"){  //列名同步
        //获取数据源列名
        var $outColumn = $("input[name='outColumn']");
        //校验
        if(!alldata.clickNotNull($outColumn,"数据源列名")){
            return false;
        }
        //获取数据源表名
        var $outTable = $("input[name='outTable']");
        //校验
        if(!alldata.clickNotNull($outTable,"数据源表名")){
            return false;
        }
    }else{  //SQL同步
        //获取查询SQL
        var $querySql = $("input[name='querySql']");
        //校验
        if(!alldata.clickNotNull($querySql,"查询SQL")){
            return false;
        }
        //获取具体值
        var val = $querySql.val();
        if(val.indexOf("drop") == 0 || val.indexOf("delete")== 0){
            return false;
        }
    }
    //获取数据源数据库名
    var $outDatabaseName = $("input[name='outDatabaseName']");
    //校验
    if(!alldata.clickNotNull($outDatabaseName,"数据源数据库名")){
        return false;
    }
    //获取接收数据源数据库名
    var $inDatabaseName = $("input[name='inDatabaseName']");
    //校验
    if(!alldata.clickNotNull($inDatabaseName,"接收数据源数据库名")){
        return false;
    }
    //获取接收的数据库列名
    var $inColumn = $("input[name='inColumn']");
    //校验
    if(!alldata.clickNotNull($inColumn,"接收的数据库列名")){
        return false;
    }
    //获取接收的数据库表名
    var $inTable = $("input[name='inTable']");
    //校验
    if(!alldata.clickNotNull($inTable,"接收的数据库表名")){
        return false;
    }
    //返回正确值
    return true;
},
    /**
     * 校验是否为空
     * @param obj ：对象
     * @param msg ：消息
     * @returns {boolean}
     */
    clickNotNull:function (obj,msg){
    //获取对象的值
    var val = $(obj).val();
    //判断是否为空
    if(val == null || val == "" || val == undefined){
        alert(msg+"不能为空！");
        return false;
    }else{
        return true;
    }
}
    /**
     * 用户添加、修改校验
     * @param flag ：标识 1 - 新增  2 - 修改
     * @returns {boolean}
     */
    ,addUserValid:function (flag) {

        var range =  new RegExp(/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/);

        //获取名称
        var $name = $("#myForm input[name='name']");
        var a = $name.val();
        //调用是否为空
        if(!alldata.clickNotNull($name,"用户名")){
            return false;
        }
        //判断是否需要获取密码
        if(flag == 1){   //新增
            //获取密码
            var $password = $("#myForm input[name='password']");
            //调用是否为空
            if(!alldata.clickNotNull($password,"密码")){
                return false;
            }
        }
        //获取邮箱
        var $email = $("#myForm input[name='email']");
        //调用是否为空
        if(!alldata.clickNotNull($email,"邮箱")){
            return false;
        }
        //邮箱格式校验
        if(!range.test($email.val())){
            alert("邮箱格式不正确！");
            return false;
        }

        return true;
    }



}