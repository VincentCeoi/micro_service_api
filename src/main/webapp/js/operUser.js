/**
 * 用于操作 页面内容
 * */


var dataX = {
    /**
     * 调用接口，填充table需要展示的数据
     */
    queryList : function(){
        //post调用接口
        $.post(alldata.path+"user/userList","",function (resu) {
            //将数据转成JSON对象
            var list = resu;
            //创建变量，接受html
            var tableHtml = "";
            //遍历数据
            for(var i = 0; i < list.length ; i ++){
                //单个对象转成json对象
                tableHtml += "<tr onmouseover='addBg(this)' onmouseout='removeBg(this)'align='center'>" +
                                "<td>"+list[i].name+"</td>" +
                                "<td style='width: 350px;'>"+list[i].password+"</td>" +
                                "<td>"+(list[i].status==1?"可用":"不可用")+"</td>" +
                                "<td><a href='#'>修改</a>" +
                                "</td>" +
                            "</tr>";
            }
            //获取table 的数据展示 document
            $("#userList").append(tableHtml);
        });

    }


};