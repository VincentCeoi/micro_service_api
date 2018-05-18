package com.lazada.microservice.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class HttpFilter implements Filter {

    /**
     * 初始化方法
     * @param filterConfig ：过滤配置
     * @throws ServletException ：servlet异常
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 过滤执行方法
     * @param servletRequest ：请求对象
     * @param servletResponse ：响应对象
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //异常处理
        try {
            //转换ServletRequest对象
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            //转换ServletResponse对象
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            //获取登录的用户
            Object obj = request.getAttribute("users");
            //获取请求的URI
            String url = request.getRequestURI();
            //判断是否登录
            if(obj == null){
                //判断访问路径，是不是登录
                if(url.indexOf("login") != -1 || url.indexOf("getCookie") != -1 ||
                        url.indexOf("createCode") != -1){
                    //通过校验，将请求数据进行传输
                    filterChain.doFilter(servletRequest, servletResponse);
                }else{
                    //获取URL地址
                    String path = request.getScheme()+"://"; //请求协议 http 或 https
                    path+=request.getHeader("host");  // 请求服务器
                    path+=request.getServletPath();
                    //返回登录页面
                    response.sendRedirect(path+"/login.html");
                }
            }else{
                //通过校验，将请求数据进行传输
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 销毁过滤器
     */
    @Override
    public void destroy() {

    }
}
