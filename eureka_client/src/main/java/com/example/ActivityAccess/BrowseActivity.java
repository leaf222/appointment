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

    public static void main(String[] args)
    {
        BrowseActivity browseActivity = new BrowseActivity();
        JSONObject result = browseActivity.GetInfoByTag("娱乐");
        System.out.println(result);
    }

    public BrowseActivity()
    {
        try
        {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL,USER,PASS);
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
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(select);
            int i = 0;
            while (resultset.next())
            {
                i++;
                Map amap = new HashMap();
                int activityid = resultset.getInt("activityid");
                int userid = resultset.getInt("userid");
                String activityname = resultset.getString("activityname");
                Timestamp starttime = resultset.getTimestamp("starttime");
                Timestamp endtime = resultset.getTimestamp("endtime");
                String address = resultset.getString("address");

                amap.put("activityid",String.valueOf(activityid));
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

    //通过活动id获取活动信息（此函数为tag查询功能实现）
    private Map GetInfoById(int activityid)
    {
        Map map = new HashMap();
        try
        {
            String select = "SELECT * FROM `se`.`activity` WHERE `activityid` =" + activityid + ";";
            Statement statement = connection.createStatement();
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
            return  map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  map;
    }

    //通过tag名称获取所有拥有此tag的活动信息
    public JSONObject GetInfoByTag(String tagname)
    {
        Map map = new HashMap();
        try
        {
            int tagid = 0;
            String select = "SELECT * FROM `se`.`tag` WHERE `tagname` = '" + tagname + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                tagid = resultSet.getInt("tagid");
            }
            String select2 = "SELECT * FROM `se`.`has_tag` WHERE `tagid` =" + tagid + ";";
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(select2);
            int i = 0;
            while (resultSet2.next())
            {
                i++;
                int activityid = resultSet2.getInt("activityid");
                Map amap = GetInfoById(activityid);

                map.put("activity"+String.valueOf(i),amap);
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //通过用户id返回发布的所有活动
    public JSONObject GetInfoByUserId(int userid)
    {
        Map map = new HashMap();
        try
        {
            String select = "SELECT * FROM `se`.`activity` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                Map amap = new HashMap();
                int activityid = resultSet.getInt("activityid");
                String activityname = resultSet.getString("activityname");
                Timestamp starttime = resultSet.getTimestamp("starttime");
                Timestamp endtime = resultSet.getTimestamp("endtime");
                String address = resultSet.getString("address");

                amap.put("activityid",String.valueOf(activityid));
                amap.put("userid",String.valueOf(userid));
                amap.put("activityname",activityname);
                amap.put("starttime",String.valueOf(starttime));
                amap.put("endtime",String.valueOf(endtime));
                amap.put("address",address);

                map.put("activity"+String.valueOf(i),amap);
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