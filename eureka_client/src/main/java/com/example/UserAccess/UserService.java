package com.example.UserAccess;

import net.sf.json.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: appointment
 * @description:Some Services about user.
 * @author: Yifan Ye
 * @create: 2020/01/04
 **/
public class UserService
{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/se?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

    private Connection connection = null;

    public static void main(String[] args) {
        UserService userService = new UserService();
        JSONObject result = userService.GetParticipantInfo("user2");
        System.out.println(result);
    }

    public UserService()
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

    //通过account获取用户信息,private
    private ResultSet GetInfo(String account)
    {
        try
        {
            String sql = "SELECT userid, account, password, identity, status, name, gender, phonenumber, job " +
                    "FROM user WHERE account = '" + account + "'";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //通过id获取account
    private String GetAccount(int userid)
    {
        try
        {
            String select = "SELECT * FROM `se`.`user` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            resultSet.next();
            String account = resultSet.getString("account");
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    //活动发布者获取审核信息
    public JSONObject GetParticipantInfo(String account)
    {
        Map map = new HashMap();
        try
        {
            ResultSet resultSet = GetInfo(account);
            resultSet.next();
            int userid = resultSet.getInt("userid");

            String select1 = "SELECT * FROM `se`.`activity` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select1);
            int i = 0;
            while (resultSet1.next())
            {
                int activityid = resultSet1.getInt("activityid");
                String activityname = resultSet1.getString("activityname");
                String select2 = "SELECT * FROM `se`.`participant` WHERE `activityid` = " + activityid + " AND `accept` = 0;";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select2);
                while (resultSet2.next())
                {
                    i++;
                    int auserid = resultSet2.getInt("userid");
                    String aaccount = GetAccount(auserid);
                    Map amap = new HashMap();
                    amap.put("activityname",activityname);
                    amap.put("account",aaccount);
                    map.put("participant" + String.valueOf(i),amap);
                }
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //审核结果
    /*public JSONObject Accept(String account, int activityid, int accept)
    {

    }*/
}