package com.example.ActivityAccess;

import net.sf.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: appointment
 * @description: Create, delete and something but no identity.
 * @author: Yifan Ye
 * @create: 2020/01/03
 **/
public class CreateActivity
{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/se?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

    private Connection connection = null;
    private Statement statement = null;

    public static void main(String[] args)
    {
        CreateActivity createActivity = new CreateActivity();

        Timestamp starttime= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        SimpleDateFormat df = new SimpleDateFormat("2019-12-1 12:30:30");
        String timeStr = df.format(starttime);
        starttime = Timestamp.valueOf(timeStr);
        Timestamp endtime= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        df = new SimpleDateFormat("2019-12-1 12:30:30");
        timeStr = df.format(endtime);
        endtime = Timestamp.valueOf(timeStr);

        JSONObject result = createActivity.AddToActivity("qwe","跟我冲",starttime,endtime,"嘉定");
        System.out.println(result);
    }

    public CreateActivity()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //根据用户account查询用户id
    private int GetUserId(String account)
    {
        try
        {
            String sql = "SELECT userid, account, password, identity, status, name, gender, phonenumber, job " +
                    "FROM user WHERE account = '" + account + "'";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next())
            {
                int id = rs.getInt("userid");
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //将活动信息添加至Activity表中
    public JSONObject AddToActivity(String account, String activityname, Timestamp starttime, Timestamp endtime, String address)
    {
        Map map = new HashMap();
        int userid = GetUserId(account);
        String insert = "INSERT INTO `se`.`activity`(`userid`,`activityname`,`starttime`,`endtime`,`address`) " +
                "values(" + userid + ",'" + activityname + "','" + starttime + "','" + endtime + "','" + address + "');";
        try
        {
            statement.executeUpdate(insert);
            map.put("创建结果","创建成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("创建结果","创建失败");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }
}