package com.lazada.microservice.controller;

import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lazada.microservice.constans.BasicResult;
import com.lazada.microservice.constans.LayUIResult;
import com.lazada.microservice.constans.ResultConstant;
import com.lazada.microservice.model.Users;
import com.lazada.microservice.service.UsersService;
import com.lazada.microservice.util.DataXUtil;
import com.lazada.microservice.util.MD5Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * Created by HayLeung on 2017/3/8.
 * 用户所有操作的工作单元
 */
@RestController
@RequestMapping("/user")
public class UserLoginController {


    @Autowired
    private UsersService usersService;

    @Autowired
    private LayUIResult result;

    @Autowired
    private BasicResult basicResult;

    /**
     * 生成验证码
     * @param request ：请求对象
     * @return
     */
    @PostMapping(value = "/createCode",produces = "text/application;charset=UTF-8")
    public String createCode(HttpServletRequest request){
        //用于接收验证码
        String  code = "";
        //验证码长度
        Integer codeLength = 4;
        //验证码随机人、内容
        Object[] arr = {0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F','G','H','J','K','L','M','N','P','Q','R',
                    'S','T','U','V','W','X','Y','Z'};
        //遍历随机
        for(int i = 0; i < codeLength; i++) {
            int index = (int)Math.floor(Math.random()*34);
            code += arr[index];
        }
        //将验证码存入session
        request.getSession().setAttribute("code",code);
        //将验证码返回
        return  code;
    }


    /**
     * 获取cookie
     * 新增cookie
     * 删除cookie
     * @param request ：请求对象
     * @param response ：响应对象
     * @return
     */
    @PostMapping(value = "/getCookie",produces = "application/json;charset=UTF-8")
    public String addCookie(HttpServletRequest request, HttpServletResponse response,
                            String userName, String password, Integer flag) throws UnsupportedEncodingException {
        //如果参数为空，证明是获取cookie进行显示
        if (userName == null && password == null) {
            //获取cookie数组
            Cookie[] cookieArr = request.getCookies();
            //判断cookie数组是否为空
            if (cookieArr == null || cookieArr.length < 0) {
                //cookie中无值
                return ResultConstant.RESULT_NULL_ERROR;
            }
            String getCookieName = null;
            String getCookiePass = null;
            String infoFlag = null;
            //遍历cookie数组
            for (Cookie cookie : cookieArr) {
                //获取cookies中的用户名
                if (cookie.getName().equals("userName")) {
                    getCookieName = URLDecoder.decode(cookie.getValue(),"utf-8");
                }
                //获取cookies中的密码
                if (cookie.getName().equals("password")) {
                    getCookiePass = URLDecoder.decode(cookie.getValue(),"utf-8");
                }
            }
             //判断cookie中的值是否为空
            if(getCookieName!= null  && !getCookieName.equals("")&&
                    getCookiePass!=null && !getCookiePass.equals("")){
                //创建JSON对象
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", getCookieName);
                jsonObject.put("password", getCookiePass);
                //将结果进行封装
                String resuMsg = jsonObject.toJSONString();
                //将数据返回
                return resuMsg;
            }else{
                //无返回结果
                return ResultConstant.RESULT_NULL_ERROR;
            }

            //如果参数有值，则证明是保存或删除
        } else{
            //创建cookie [此处还需要进行修改]
            Cookie cookieName = new Cookie("userName", URLEncoder.encode(userName,"utf-8"));
            //设置cookie的各种属性  [此处还需要进行修改]
            Cookie cookiePass = new Cookie("password", URLEncoder.encode(password,"utf-8"));
            //判断是保存还是删除
            if (flag == 1) {  //保存
                cookiePass.setMaxAge(1*(60*60*24));
                cookiePass.setPath("/");
                cookieName.setMaxAge(1*(60*60*24));
                cookieName.setPath("/");
            } else {   //删除
                cookiePass.setMaxAge(0);
                cookiePass.setPath("/");
                cookieName.setMaxAge(0);
                cookieName.setPath("/");
            }
            //将cookies添加进去
            response.addCookie(cookieName);
            response.addCookie(cookiePass);
            //成功存入
            return ResultConstant.SUCCESS;
        }
    }



    /**
     * 用户进行登录
     * @param request ：请求对象
     * @return ：返回登录的状态码
     */
    @PostMapping(value = "/login",produces = "application/text;charset=UTF-8")
    public String userLogin(HttpServletRequest request, HttpServletResponse response) throws  UnsupportedEncodingException, ParseException {
        //获取验证码
        String paramCode = request.getParameter("code");
        //获取session中的验证码
        Object sessionCode = request.getSession().getAttribute("code");
        //判断session中的验证码是否存在
        if(sessionCode == null){
            return ResultConstant.resuInfo(ResultConstant.ERROR,"验证码已失效，请刷新页面！");
        }else{
            //接收验证码
            String code = sessionCode.toString();
            //判断是否相同 //验证码错误
            if (!paramCode.toUpperCase().equals(code.toUpperCase())){
                return ResultConstant.resuInfo(ResultConstant.LOGIN_CODE,"验证码错误！");
            }
        }
        //获取用户名
        String name = request.getParameter("userName");
        //获取密码
        String password = request.getParameter("password");
        //创建对象，接收用户对象
        Users users = null;
        //异常处理
        try {
            //调用接口，验证用户登录
            users = usersService.queryUsersByName(name);
            //判断用户是否为空
            if(users == null){
                //返回用户不存在的状态码
                return ResultConstant.resuInfo(ResultConstant.ERROR,"用户不存在！");
            }else{
                //判断用户是否可用
                if(users.getStatus() == 2){
                    //返回用户不可用的状态码
                    return ResultConstant.resuInfo(ResultConstant.ERROR,"用户不可用！");
                }else{
                    //判断密码
                    if(MD5Check.verify(password,users.getPassword())){
                        //获取保存用户名密码标识
                        String cookiePass = request.getParameter("cookie_pass");
                        //判断是否记住
                        if(cookiePass != null && cookiePass.equals("1")){
                            //记住：调用函数 1 -保存
                            addCookie(request,response, name,password,1);
                        }else{
                            //删除用户名、密码  2 -删除
                            addCookie(request,response,name,password,2);
                        }
                    }//密码错误
                    else{
                        //返回成功登录的状态码
                        return ResultConstant.resuInfo(ResultConstant.ERROR,"密码错误！");
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return ResultConstant.resuInfo(ResultConstant.ERROR,"系统异常！");
        }
        //将对象存入session中
        request.setAttribute("users",users);
        //返回成功登录的状态码
        return ResultConstant.resuInfo(ResultConstant.SUCCESS,"用户登录成功！");
    }


    /**
     * 获取用户列表
     * @param request ：请求对象
     * @return
     */
    @RequestMapping("/userList")
    public LayUIResult queryUserList(HttpServletRequest request,
                                     @RequestParam(value = "page",required = true)Integer pageNum,
                                     @RequestParam(value = "limit",required = true)Integer limit,
                                     @RequestParam(value = "name",required = false)String name,
                                     @RequestParam(value = "startTime",required = false)String startTime,
                                     @RequestParam(value = "stopTime",required = false)String stopTime){
        //异常处理
        try {
            //创建参数集合
            Map<String,Object> param = new HashMap<>();
            param.put("first",(pageNum - 1) *limit);
            param.put("size",limit);
            param.put("name",name);
            param.put("startTime",startTime);
            param.put("stopTime",stopTime);
            Page<Users> page = PageHelper.startPage(pageNum,limit);
            //调用接口，获取用户列表
            List<Users> list = usersService.queryUsersList(param);
            //填充数据
            result.setCode(0);
            result.setMsg("查询用户列表！");
            result.setCount((int)page.getTotal());
            //填充集合数据
            result.setData(list);
        }catch (Exception e){
            //填充数据
            result.setCode(10000);
            result.setMsg("查询用户列表，出现异常！");
            result.setCount(0);
        }
        //返回数据
        return result;
    }


    /**
     * 新增或修改用户信息
     * @param request ：请求对象
     * @param id ：ID
     * @param name ：用户名
     * @param email ：邮箱
     * @param password ：密码
     * @return
     */
    @RequestMapping("/addOrEditUser")
    public BasicResult addUserInfo(HttpServletRequest request,
                                   @RequestParam(value = "id",required = false)Integer id,
                                   @RequestParam(value = "name",required = true)String name,
                                   @RequestParam(value = "email",required = true)String email,
                                   @RequestParam(value = "password",required = false)String password){
        try{
            //创建变量，接收返回的MSG
            String msg = "";
            //创建变量，接收返回结果值
            Integer count = 0;
            //创建用户对象
            Users user = new Users();
            user.setId(id);
            user.setCreateTime(DataXUtil.format.format(new Date()));
            user.setName(name);
            user.setEmail(email);
            user.setStatus(1);
            //根据用户民获取对象
            Users userObj = usersService.queryUsersByName(name);
            //判断密码是否有值，有值则进行加密
            if(password != null && !"".equals(password)){
                //判断是否存在
                if(userObj != null){
                    basicResult.setCode(10001);
                    basicResult.setMsg("用户名已存在！");
                }else{
                    //调用MD5 加密
                    String pass = MD5Check.generate(password);
                    //将密码存入对象中
                    user.setPassword(pass);
                    //调用新增接口
                    count = usersService.addUsers(user);
                    //判断是否成功
                    if(count > 0 ){
                        basicResult.setCode(10000);
                        basicResult.setMsg("新增用户成功！");
                    }else{
                        basicResult.setCode(10001);
                        basicResult.setMsg("新增用户失败！");
                    }
                }
            }else{
                //判断用户是否为空
                if(userObj != null){
                    //不跟上传的ID相同，则表示已存在要修改的用户名
                    if(!userObj.getId().equals(id)){
                        basicResult.setCode(10001);
                        basicResult.setMsg("用户名已存在！");
                    }else{
                        //调用修改接口
                        count = usersService.updateUsers(user);
                    }
                }else{  //为空则不存在，可执行修改
                    //调用修改接口
                    count = usersService.updateUsers(user);
                }
                //判断是否成功
                if(count > 0 ){
                    basicResult.setCode(10000);
                    basicResult.setMsg("修改用户成功！");
                }else{
                    basicResult.setCode(10001);
                    basicResult.setMsg("修改用户失败！");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            basicResult.setCode(10001);
            basicResult.setMsg("出现异常！");
        }
        return basicResult;
    }


    /**
     * 删除用户信息
     * @param request ：请求对象
     * @param id ：ID字符串 ，可单个、批量删除用户
     * @return
     */
    @RequestMapping("/deleteUser")
    public BasicResult deleteUser(HttpServletRequest request,
                                  @RequestParam(value = "id",required = true)String id){
        try {
            //调用接口，进行删除用户信息
            Integer count = usersService.deleteUsers(id);
            //判断受影响的行数
            if(count <= 0){
                basicResult.setCode(10001);
                basicResult.setMsg("用户删除失败！");
            }else{
                basicResult.setCode(10000);
                basicResult.setMsg("成功删除用户！");
            }
        }catch (Exception e){
            e.printStackTrace();
            basicResult.setCode(10001);
            basicResult.setMsg("出现异常！");
        }
        return basicResult;
    }



}
