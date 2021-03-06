package com.example.UserAccess;


import net.sf.json.JSONObject;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: appointment
 * @description: Sign in, sign on and sign out.
 * @author: Yifan Ye
 * @create: 2020/01/03
 **/
public class UserSign
{
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/se?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "root";

    private Connection connection = null;

    public UserSign()
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

    public static void main(String[] args)
    {
        UserSign userSign = new UserSign();
        JSONObject result = userSign.SelectInfo("leaf777");
        System.out.println(result);
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

    //用户注册“注册结果”：“用户名已存在”“注册成功”“意外的错误”
    public JSONObject SignUp(String account, String password)
    {
        Map map = new HashMap();
        try
        {
            ResultSet rs = GetInfo(account);
            if(rs.next())
            {
                map.put("result","用户名已存在");
                JSONObject jsonObject = JSONObject.fromObject(map);
                return jsonObject;
            }
            else
            {
                String insert = "INSERT INTO `se`.`user`(`account`,`password`) " +
                        "values('" + account + "','" + password + "');";
                map.put("result","注册成功");
                JSONObject jsonObject = JSONObject.fromObject(map);
                return jsonObject;
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        map.put("result","意外的错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //用户登录“登录结果”：“登录成功”“用户已登录”“密码错误”“用户名不存在”“意外的错误”
    public JSONObject SignIn(String account, String password)
    {
        Map map = new HashMap();
        try
        {
            ResultSet rs = GetInfo(account);
            while (rs.next())
            {
                String pw = rs.getString("password");//获取密码
                boolean status = rs.getBoolean("status");
                if(pw.equals(password) && status == false)
                {
                    String update = "update `se`.`user` " +
                            "set `status` = true " +
                            "where `account` = '" + account + "';";
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(update);
                    map.put("result","登录成功");
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
                else if(pw.equals(password) && status == true)
                {
                    map.put("result","用户已登录");
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
                else
                {
                    map.put("result","密码错误");
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
            }
            map.put("result","用户名不存在");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","意外的错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //用户注销“注销结果”：“注销成功”“意外的错误”
    public JSONObject SignOut(String account)
    {
        Map map = new HashMap();
        try
        {
            String update = "update `se`.`user` " +
                    "set `status` = false " +
                    "where `account` = '" + account + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);
            map.put("result","注销成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","意外的错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //用户添加个人信息“更新结果”：“更新成功”“意外的错误”
    public JSONObject UpdateInfo(String account, String name, boolean gender, String phonenumber, String job)
    {
        Map map = new HashMap();
        try
        {
            String update = "update `se`.`user` " +
                    "set `name` = '" + name + "',`gender` = " + gender + ",`phonenumber` = '" + phonenumber + "',`job` = '" + job + "' " +
                    "where `account` = '" + account + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);
            map.put("result","更新成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","意外的错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //查询个人信息
    public JSONObject SelectInfo(String account)
    {
        Map map = new HashMap();
        try
        {
            ResultSet resultSet = GetInfo(account);
            while (resultSet.next())
            {
                int identity = resultSet.getInt("identity");
                String name  = resultSet.getString("name");
                boolean gender = resultSet.getBoolean("gender");
                String phonenumber = resultSet.getString("phonenumber");
                String job = resultSet.getString("job");

                map.put("status",String.valueOf(identity));
                map.put("name",name);
                map.put("gender",String.valueOf(gender));
                map.put("phonenumber",phonenumber);
                map.put("job",job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //添加头像url至数据库
    public JSONObject AddPicture(String accout, String url)
    {
        Map map = new HashMap();
        try
        {
            String update = "UPDATE `se`.`user` SET `picture` = '" + url + "' WHERE `account` = '" + accout + "';";
            Statement statement = connection.createStatement();
            statement.executeUpdate(update);
            map.put("result","插入成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","插入失败");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //返回用户的状态
    public JSONObject GetStatus(int activityid, String account)
    {
        Map map = new HashMap();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`participant` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while (resultSet1.next())
            {
                int accept = resultSet1.getInt("accept");
                if(accept == 0)
                {
                    map.put("result",String.valueOf(2));
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
                else if(accept == 2)
                {
                    map.put("result",String.valueOf(4));
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
            }
            String select2 = "SELECT * FROM `se`.`user` WHERE `userid` = " + userid + ";";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet2 = statement1.executeQuery(select2);
            while (resultSet2.next())
            {
                int identity = resultSet2.getInt("identity");
                if(identity == 1)
                {
                    map.put("result",String.valueOf(7));
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
            }
            String select3 = "SELECT * FROM `se`.`activity` WHERE `activityid` = " + activityid + ";";
            Statement statement2 = connection.createStatement();
            ResultSet resultSet3 = statement2.executeQuery(select3);
            while(resultSet3.next())
            {
                if(resultSet3.getInt("userid")==userid)
                {
                    map.put("result",String.valueOf(7));
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    return jsonObject;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result",String.valueOf(1));
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }
}