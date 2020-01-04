package com.example.ActivityAccess;

import com.mysql.cj.protocol.Resultset;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: appointment
 * @description: Use different methods to browse activities.
 * @author: Yifan Ye
 * @create: 2020/01/03
 **/
public class BrowseActivity
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
        BrowseActivity browseActivity = new BrowseActivity();
        JSONObject result = browseActivity.GetAllInfo();
        System.out.println(result);
    }

    public BrowseActivity()
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

    //获取所有活动信息
    public JSONObject GetAllInfo()
    {
        Map map = new HashMap();
        try
        {
            String select = "SELECT * FROM `se`.`activity`;";
            ResultSet resultset = statement.executeQuery(select);
            int i = 0;
            while (resultset.next())
            {
                i++;
                Map amap = new HashMap();
                int userid = resultset.getInt("userid");
                String activityname = resultset.getString("activityname");
                Timestamp starttime = resultset.getTimestamp("starttime");
                Timestamp endtime = resultset.getTimestamp("endtime");
                String address = resultset.getString("address");

                amap.put("userid",String.valueOf(userid));
                amap.put("activityname",activityname);
                amap.put("starttime",String.valueOf(starttime));
                amap.put("endtime",String.valueOf(endtime));
                amap.put("address",address);

                map.put("activity"+String.valueOf(i),amap);
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("??","???");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //通过id获取活动信息（此函数为tag查询功能实现）
    private JSONObject GetInfoById(int id)
    {
        Map map = new HashMap();
        try
        {
            String select = "SELECT * FROM `se`.`activity` WHERE `activityid` =" + id + ";";
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                String activityname = resultSet.getString("activityname");
                Timestamp starttime = resultSet.getTimestamp("starttime");
                Timestamp endtime = resultSet.getTimestamp("endtime");
                String address = resultSet.getString("address");

                map.put("activityname",activityname);
                map.put("starttime",String.valueOf(starttime));
                map.put("endtime",String.valueOf(endtime));
                map.put("address",address);
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return  jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return  jsonObject;
    }
}