package com.hy.demo.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultFilter implements Filter {

    private AtomicInteger cookieIndex = new AtomicInteger(1);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("defaultFilter拦截");
        HttpServletRequest request2=(HttpServletRequest) request;
        HttpServletResponse response2=(HttpServletResponse) response;
        //response2.addCookie();
        String sessionId = request2.getSession().getId();
        Cookie[] cookies = request2.getCookies();
        boolean flag = true;
        for(Cookie cookie : cookies ){
            System.out.println(cookie.getValue());
            if(cookie.getValue().equals(sessionId)) {
                flag = false;
                break;
            }
        }
        if(flag){
            response2.addCookie(new Cookie("cookie" + cookieIndex , sessionId));
            cookieIndex.addAndGet(1);
        }

        request2.setCharacterEncoding("UTF-8");
        response2.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }

}

