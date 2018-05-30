/**
 * 用于操作 页面内容
 * */


var dataX = {
    /**
     * 调用接口，填充table需要展示的数据
     */
    queryList : function(){
        //post调用接口
        $.post(alldata.path+"dataX/queryDataXList","",function (resu) {
            //将数据转成JSON对象
            var list = resu;
            //创建变量，接受html
            var tableHtml = "";
            //遍历数据
            for(var i = 0; i < list.length ; i ++){
                //单个对象转成json对象
                tableHtml += "<tr onmouseover='addBg(this)' onmouseout='removeBg(this)' align='center'>" +
                                "<td>"+list[i].name+"</td>" +
                                "<td style='width: 300px;'>"+list[i].filename+"</td>" +
                                "<td style='width: 170px;'>"+list[i].expression+"</td>" +
                                "<td>"+list[i].createtime+"</td>" +
                                "<td>"+(list[i].modifytime==null?" - ":list[i].modifytime)+"</td>" +
                                "<td>"+(list[i].status==1?"可用":"不可用")+"</td>" +
                                "<td>" ;
                                if(list[i].status == 1){
                                    tableHtml += "<a href='javascript:void(0);' " +
                                    "onclick='dataX.removeDataX("+list[i].id+",\""+list[i].filename+"\",\""+list[i].expression+"\",\""+list[i].name+"\",2,\""+list[i].createtime+"\",\""+list[i].modifytime+"\")'>弃用</a>" ;
                                }else{
                                    tableHtml += "<a href='javascript:void(0);' " +
                                        "onclick='dataX.removeDataX("+list[i].id+",\""+list[i].filename+"\",\""+list[i].expression+"\",\""+list[i].name+"\",1,\""+list[i].createtime+"\",\""+list[i].modifytime+"\")'>启用</a>";
                                }
                         tableHtml +="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                                    "<a href='javascript:void(0);' onclick='dataX.skipPage(\""+list[i].filename+"\",\""+
                                    list[i].name+"\",\""+list[i].expression+"\","+list[i].id+",\""+list[i].createtime+"\","+
                                    list[i].status+")'>修改</a></td></tr>";
            }
            //获取table 的数据展示 document
            $("#syncList").append(tableHtml);
        });

    },
    removeDataX:function (id,fileName,expression,status) {
        var data = {id:id,fileName:fileName,expression:expression,status:status};
        //post调用接口
        $.post(alldata.path+"dataX/removeDataX",data,function (resu) {
            var data = resu;
            //判断code
            if(data.code=="10000"){
                return 10000;
                //$("#data-iframe",parent.document).attr("src","../layui-html/configList.html");
            }else{
                return 10001;
            }
        });
    },

    /**
     * 跳转页面
     * @param fileName ：文件名
     * @param name ：操作说明
     * @param expression ：表达式
     */
    skipPage :function(fileName,name,expression,id,createTime,status){
        //获取父级容器 index.html
        //填充文件名
        $("input[name='fileName']",parent.document).val(fileName);
        //填充操作说明
        $("input[name='name']",parent.document).val(name);
        //填充表达式
        $("input[name='expression']",parent.document).val(expression);
        //填充ID
        $("input[name='id']",parent.document).val(id);
        //填充创建时间
        $("input[name='createTime']",parent.document).val(createTime);
        //填充状态
        $("input[name='status']",parent.document).val(status);
        //跳转到修改页面
        $("#data-iframe",parent.document).attr("src","modifySync.html");
    }
};