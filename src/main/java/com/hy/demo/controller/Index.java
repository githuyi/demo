package com.hy.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.hy.demo.common.core.domain.AjaxResult;
import com.hy.demo.service.Test;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/*
* 1) 如果只是使用@RestController注解Controller，则Controller中的方法无法返回jsp页面，或者html，
* 配置的视图解析器 InternalResourceViewResolver不起作用，返回的内容就是Return 里的内容。
* 2) 如果需要返回到指定页面，则需要用 @Controller配合视图解析器InternalResourceViewResolver才行。
*     如果需要返回JSON，XML或自定义mediaType内容到页面，则需要在对应的方法上加上@ResponseBody注解。
* */

/*
* @GetMapping 用于将HTTP GET请求映射到特定处理程序方法的注释。
* 具体来说，@GetMapping是一个作为快捷方式的组合注释@RequestMapping(method = RequestMethod.GET)。
* @PostMapping 用于将HTTP POST请求映射到特定处理程序方法的注释。
* 具体来说，@PostMapping是一个作为快捷方式的组合注释@RequestMapping(method = RequestMethod.POST)。
* 此外还有@PutMapping，@PatchMapping，@DeleteMapping同上
* 所以一般情况下都是用@RequestMapping（method=RequestMethod.）即可
* */
@Controller
@RequestMapping("/")
public class Index {

    @Autowired
    @Qualifier("TestImpl1")
    Test test;

    @GetMapping("/")
    public String index(ModelMap mmap){
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        mmap.addAttribute("getId" ,session.getId());
        test.test();
        return "index";
    }

    @PostMapping("/getCurrentData")
    @ResponseBody
    public AjaxResult getCurrentData(ModelMap mmap){
        AjaxResult ajax = AjaxResult.success();


        return ajax;
    }
    @GetMapping("/get7DaysData")
    @ResponseBody
    public AjaxResult get7DaysData(){
        AjaxResult ajax = AjaxResult.success();

        String[] date = {"12-04", "12-05", "12-06", "12-07", "12-08", "12-09", "12-10"};
        int[] data = {80, 75, 60, 88, 70, 75, 86};
        ajax.put("date",date);
        ajax.put("data",data);
        return ajax;
    }


    @GetMapping("/kickout")
    @ResponseBody
    public String kickout(){
        System.out.println("进入");
        return "kickout";
    }

    @GetMapping("/login")
    public String login(){
        //getUserRealm();
        UsernamePasswordToken token = new UsernamePasswordToken("hy", "123456", "hy");
        Subject subject = SecurityUtils.getSubject();
        Subject currentUser = SecurityUtils.getSubject();
        subject.login(token);
        return "login";
    }

    @GetMapping("/authUser/list")
    @ResponseBody
    public String list(String name , String password){
        UsernamePasswordToken token = new UsernamePasswordToken(name, password, "hy");
        //获取当前用户
        Subject currentUser = SecurityUtils.getSubject();

       // boolean abc = currentUser.hasRole("abc");
       // boolean admin = currentUser.hasRole("admin");
       // PrincipalCollection principals = currentUser.getPrincipals();
       // boolean abc1 = currentUser.hasRole("abc");
        boolean authenticated = currentUser.isAuthenticated();
        return authenticated ? "操作成功" : "操作失败";
    }


    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "login";
    }

    @GetMapping("/token")
    @ResponseBody
    public String token(HttpServletRequest request){

        String sessionId = request.getSession().getId();

        return "   " + sessionId ;
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.43.191",6381);
        //String set = jedis.set("aaa", "aaaaaaaaaa", "NX", "EX", 10);
        String set = jedis.set("aaa", "aaaaaaaaaa");
        //JedisSentinelPool jedisSentinelPool = new JedisSentinelPool();
        //jedisSentinelPool();
        System.out.println(set);

    }

    /*@Test*/
    public void test(){
        JedisSentinelPool jedisSentinelPool = jedisSentinelPool();
        Jedis jedis = jedisSentinelPool.getResource();

        //String set = jedis.set("test", "test");
        String hmget = jedis.hget("huser:01", "username");
        System.out.println(hmget);
    }

    public static JedisSentinelPool jedisSentinelPool(){

        //String ip = "localhost";
        String ip = "192.168.43.191";
        //String ip = "192.168.2.103";
        Set<String> sentinels = new HashSet<String>();
        //Sentine端口
/*        sentinels.add(new HostAndPort(ip, 26379).toString());
        sentinels.add(new HostAndPort(ip, 26380).toString());
        sentinels.add(new HostAndPort(ip, 26381).toString());*/
         sentinels.add(ip+":26379");
        sentinels.add(ip+":26380");
        sentinels.add(ip+":26381");
        JedisPoolConfig config = new JedisPoolConfig();
        //config.setMaxTotal(200);
        //config.setMaxIdle(50);
        //在获取连接的时候检查有效性,
        //config.setTestOnBorrow(true);
        //在空闲时检查有效性, 默认false
        //config.setTestWhileIdle(true);
        //config.setMaxWaitMillis(1000);
        //JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels, config, 10000,"");
        JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster",sentinels,config);
        return sentinelPool;
    }


}
