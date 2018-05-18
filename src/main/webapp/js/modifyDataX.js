/**
 * 用于操作 页面内容
 * */


/**
 * 填充表单数据
 * @param fileName ：文件名
 */
function fullFormData(){
    //获取父级容器 index.html
    //获取文件名
    var fileName = $("input[name='fileName']",parent.document).val();
    //获取操作说明
    var name = $("input[name='name']",parent.document).val();
    //获取表达式
    var expression = $("input[name='expression']",parent.document).val();
    //获取修改时间
    var createTime = $("input[name='createTime']",parent.document).val();
    //获取ID
    var id = $("input[name='id']",parent.document).val();
    //获取状态
    var status = $("input[name='status']",parent.document).val();
    //根据文件名，调用接口，获取填充数据
    $.post(alldata.path+"/dataX/getDataXJson",{fileName:fileName},function (result) {
        console.log(result);
        //判断result是否有值
        if(result.code == 10000){
            //获取返回的内容
            var dataX = JSON.parse(result.obj);
            //获取channelCount
            var channelCount = dataX.job.setting.speed.channel;
            //获取数据源用户名
            var outUserName = dataX.job.content[0].reader.parameter.username;
            //获取数据源密码
            var outPassword = dataX.job.content[0].reader.parameter.password;
            //获取数据切片
            var splitPk = dataX.job.content[0].reader.parameter.splitPk;
            //获取数据库连接 [ 需要处理 ]
            var outJdbcUrl = dataX.job.content[0].reader.parameter.connection[0].jdbcUrl[0];
            //处理连接，获取host 跟 port
            var outUrlStr = outJdbcUrl.substring(13);
            //获取host
            var outHost = outUrlStr.substring(0,outUrlStr.indexOf(":"));
            //获取端口
            var outPort = outUrlStr.substring(outUrlStr.indexOf(":")+1,outUrlStr.indexOf("/"));
            //获取数据库名
            var outDatabaseName = outUrlStr.substring(outUrlStr.indexOf("/")+1,outUrlStr.indexOf("?"));
            //获取列名，如果列名存在，就获取表名，如果不存在，就获取查询SQL
            var outColumn = dataX.job.content[0].reader.parameter.column;
            //判断是否有值 获取查询SQL
            if(outColumn == null || outColumn == "" || outColumn == undefined){
                //获取查询SQL
                var querySql = dataX.job.content[0].reader.parameter.connection[0].querySql[0];
                //填充查询SQL
                $("input[name='querySql']").val(querySql);
                //设置下拉选中
                $("option[value='sqlSync']").attr("selected","selected");
                //调用change事件
                $("select[name='syncMethod']").change();
            }
            //获取列名，表名
            else{
                //接收列名
                var outColumnStr = outColumn.join(",");
                //获取数据源表名
                var outTable = dataX.job.content[0].reader.parameter.connection[0].table.join(",");
                //填充表名
                $("input[name='outTable']").val(outTable);
                //填充列名
                $("input[name='outColumn']").val(outColumnStr);
                //设置下拉选中
                $("option[value='columnSync']").attr("selected","selected");
                //调用change事件
                $("select[name='syncMethod']").change();
            }
            /***************************************获取写入块************************************************/
            //获取写入模型
            var writerMode = dataX.job.content[0].writer.parameter.writeMode;
            //设置选中下拉
            $("option[value='"+writerMode+"']").attr("selected","selected");
            //获取接收的用户名
            var inUserName = dataX.job.content[0].writer.parameter.username;
            //获取接收的密码
            var inPassword =dataX.job.content[0].writer.parameter.password;
            //获取接收的列名
            var inColumn = dataX.job.content[0].writer.parameter.column.join(",");
            //获取接收的表名
            var inTable = dataX.job.content[0].writer.parameter.connection[0].table.join(",");
            //获取接收的数据库连接
            var inJdbcUrl = dataX.job.content[0].writer.parameter.connection[0].jdbcUrl;
            //处理连接，获取host 跟 port
            var inUrlStr = inJdbcUrl.substring(13);
            //获取host
            var inHost = inUrlStr.substring(0,inUrlStr.indexOf(":"));
            //获取端口
            var inPort = inUrlStr.substring(inUrlStr.indexOf(":")+1,inUrlStr.indexOf("/"));
            //获取数据库名
            var inDatabaseName = inUrlStr.substring(inUrlStr.indexOf("/")+1,inUrlStr.indexOf("?"));
            //获取同步前，接收数据库执行的SQL
            var perSql = "";
            if(dataX.job.content[0].writer.parameter.preSql != undefined){
                perSql = dataX.job.content[0].writer.parameter.preSql[0];
            }
            //获取同步后，接收数据库执行的SQL
            var postSql = "";
            if(dataX.job.content[0].writer.parameter.postSql != undefined){
                postSql = dataX.job.content[0].writer.parameter.postSql[0];
            }

            /*************************开始填充数据***************************/

            $("input[name='name']").val(name);   //操作说明
            $("input[name='id']").val(id);   //id
            $("input[name='createTime']").val(createTime);   //创建时间
            $("input[name='status']").val(status);   //状态
            $("input[name='fileName']").val(fileName);
            //处理表达式
            if(expression.indexOf("?")){  //标识为每天执行
                //将表达式拆分 [小时]
                var hour = expression.substring(6,8);
                //拆分分钟
                var min = expression.substring(3,5);
                //拆分秒
                var mis = expression.substring(0,2);
                //将时分秒进行拼接
                var time = hour+":"+min+":"+mis;
                //进行填充
                $("input[name='expressTime']").val(time);
            }else{   //指定日期执行
                /*
                //拆分表达式

                //进行填充
                $("input[name='expressDate']").val(dateTime);*/
            }
            $("input[name='outUserName']").val(outUserName);  //数据源用户名
            $("input[name='outPassword']").val(outPassword);  //数据源密码
            $("input[name='inUserName']").val(inUserName);    //接收数据用户名
            $("input[name='inPassword']").val(inPassword);    //接收数据密码
            $("input[name='channelCount']").val(channelCount);  //通道数
            $("input[name='outIPAddress']").val(outHost);     //数据源IP地址
            $("input[name='outPort']").val(outPort);     //数据源端口
            $("input[name='inIPAddress']").val(inHost);     //接收数据IP地址
            $("input[name='inPort']").val(inPort);     //接收数据端口
            $("input[name='inColumn']").val(inColumn);     //接收的数据列名
            $("input[name='inTable']").val(inTable);     //接收的数据表名
            $("input[name='splitPk']").val(splitPk);     //数据切片
            $("input[name='outDatabaseName']").val(outDatabaseName);     //数据源的数据库名称
            $("input[name='inDatabaseName']").val(inDatabaseName);     //接收数据的数据库名称
            $("input[name='perSql']").val(perSql);     //接收数据库，同步前执行的SQL
            $("input[name='postSql']").val(postSql);     //接收数据库，同步后执行的SQL
        }else{
            alert(result.msg);
        }
    });
}


/**
 * 提交表单
 */
function submitForm(){
    //判断是否校验通过
    if(!alldata.validationForm()){
        return;
    }
    var data = $("#myForm").serialize();
    //调用接口
    $.post(alldata.path+"dataX/modifyDataX",data,function (resu) {
        //判断是否成功
        if(resu.code == "10000"){
            alert(resu.msg);
            $("#data-iframe",parent.document).attr("src","SyncList.html");
        }else{
            alert(resu.msg);
        }
    });
}