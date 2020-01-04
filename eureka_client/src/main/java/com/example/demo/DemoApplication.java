package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.*;


@EnableEurekaClient
@SpringBootApplication
@RestController
public class DemoApplication {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/websoa?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";


    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Value("${server.port}")
	String port;
	@RequestMapping("/hi")
	public String home(@RequestParam String name)
    {
        try
        {
            // 注册 JDBC 驱动
            Class.forName(JDBC_DRIVER);

            // 打开链接
            System.out.println("连接数据库...");
            Connection conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            Statement stmt = conn.createStatement();
            String sid = name;
            String sql;
            sql = "SELECT studentid, password, studentname FROM student WHERE studentid = '" + sid + "'";
            ResultSet rs = stmt.executeQuery(sql);

            /*if(!rs.next())
            {
                return "";
            }*/
            while (rs.next())
            {
                String id  = rs.getString("studentid");
                String password = rs.getString("password");
                String studentName = rs.getString("studentname");
                return "id:" + id + "pw:" + password + "name:" + studentName;
            }
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }
        return "";
	}
}
