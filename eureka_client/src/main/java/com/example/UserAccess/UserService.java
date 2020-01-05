package com.example.UserAccess;

import net.sf.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
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

        Timestamp c_time= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        SimpleDateFormat df = new SimpleDateFormat("2019-12-2 12:30:30");
        String timeStr = df.format(c_time);
        c_time = Timestamp.valueOf(timeStr);

        UserService userService = new UserService();
        JSONObject result = userService.Comment("xxx",c_time,"user3",1, (float) 9.9);
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

    //审核结果“审核结果”:“审核完成”“审核异常”
    public JSONObject Accept(String account, int activityid, int accept)
    {
        Map map = new HashMap();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int usreid = 0;
            while (resultSet.next())
            {
                usreid = resultSet.getInt("userid");
            }
            String update = "UPDATE `se`.`participant` "
                    + "SET `accept` = " + accept
                    + " WHERE `activityid` = " + activityid + " AND `userid` = " + usreid + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);
            map.put("审核结果","审核完成");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("审核结果","审核异常");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //评价活动“评价结果”:“评价成功”“已经评论过”
    public JSONObject Comment(String c_content, Timestamp c_time, String account, int activityid, float score)
    {
        Map map = new HashMap();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while (resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String insert = "INSERT INTO `se`.`comment`(`activityid`,`userid`,`c_content`,`c_time`,`score`) "
                    + "VALUES(" + activityid + "," + userid + ",'" + c_content + "','" + c_time + "'," + score + ");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            map.put("评价结果","评价成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("评价结果","已经评论过");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }
}