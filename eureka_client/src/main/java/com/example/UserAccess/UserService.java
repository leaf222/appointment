package com.example.UserAccess;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
        JSONArray result = userService.ViewAllInform("user2");
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
    public JSONArray GetParticipantInfo(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            resultSet.next();
            int userid = resultSet.getInt("userid");

            String select1 = "SELECT * FROM `se`.`activity` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select1);
            while (resultSet1.next())
            {
                int activityid = resultSet1.getInt("activityid");
                String activityname = resultSet1.getString("activityname");
                String select2 = "SELECT * FROM `se`.`participant` WHERE `activityid` = " + activityid + " AND `accept` = 0;";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select2);
                while (resultSet2.next())
                {
                    int auserid = resultSet2.getInt("userid");
                    String aaccount = GetAccount(auserid);
                    Map amap = new HashMap();
                    amap.put("activityname",activityname);
                    amap.put("account",aaccount);
                    list.add(amap);
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
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
            map.put("result","审核完成");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","审核异常");
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
            map.put("result","评价成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","已经评论过");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //申请参加活动“申请结果”：“申请成功，，等待审核”“已经申请过了”
    public JSONObject JoinActivity(String account, int activityid)
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
            String insert = "INSERT INTO `se`.`participant`(`activityid`,`userid`,`accept`) "
                    + "VALUES(" + activityid+"," + userid + ",0);";
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            map.put("result","申请成功，等待审核");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","已经申请过了");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //退出活动“退出结果”：“成功退出”“不在活动中”
    public JSONObject ExitActivity(String account, int activityid)
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
            String delete = "DELETE FROM `se`.`participant` WHERE `activityid` = "
                    + activityid + " AND `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
            map.put("result","成功退出");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","不在活动中");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //收藏活动“收藏结果”：“收藏成功”“已经收藏”
    public JSONObject FavoriteActivity(String account, int activityid)
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
            String insert = "INSERT INTO `se`.`favorite`(`activityid`,`userid`) "
                    + "VALUES(" + activityid + "," + userid + ");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            map.put("result","收藏成功");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","已经收藏");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //取消收藏“取消收藏结果”：“成功取消收藏”“未收藏”
    public JSONObject CancelFavorite(String account, int activityid)
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
            String delete = "DELETE FROM `se`.`favorite` WHERE `userid` = " + userid + " AND `activityid` = " + activityid + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
            map.put("result","成功取消收藏");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","未收藏");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //查看收藏
    public JSONArray ViewFavorite(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`favorite` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while (resultSet1.next())
            {
                Map map = new HashMap();
                int activityid = resultSet1.getInt("activityid");
                String select2 = "SELECT * FROM `se`.`activity` WHERE `activityid` = " + activityid + ";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select2);
                while (resultSet2.next())
                {
                    int userid1 = resultSet2.getInt("userid");
                    String account1 = GetAccount(userid1);
                    String activityname = resultSet2.getString("activityname");
                    Timestamp starttime = resultSet2.getTimestamp("starttime");
                    Timestamp endtime = resultSet2.getTimestamp("endtime");
                    String address = resultSet2.getString("address");
                    String imgurl = resultSet2.getString("imgurl");

                    map.put("userid",String.valueOf(userid1));
                    map.put("account",account1);
                    map.put("activityid",String.valueOf(activityid));
                    map.put("activityname",activityname);
                    map.put("starttime",String.valueOf(starttime));
                    map.put("endtime",String.valueOf(endtime));
                    map.put("address",address);
                    map.put("imgurl",imgurl);
                }
                list.add(map);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //查看发布
    public JSONArray ViewPublish(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`activity` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while (resultSet1.next())
            {
                Map map = new HashMap();

                int userid1 = resultSet1.getInt("userid");
                int activityid = resultSet1.getInt("activityid");
                String account1 = GetAccount(userid1);
                String activityname = resultSet1.getString("activityname");
                Timestamp starttime = resultSet1.getTimestamp("starttime");
                Timestamp endtime = resultSet1.getTimestamp("endtime");
                String address = resultSet1.getString("address");
                String imgurl = resultSet1.getString("imgurl");

                map.put("userid",String.valueOf(userid1));
                map.put("account",account1);
                map.put("activityid",String.valueOf(activityid));
                map.put("activityname",activityname);
                map.put("starttime",String.valueOf(starttime));
                map.put("endtime",String.valueOf(endtime));
                map.put("address",address);
                map.put("imgurl",imgurl);

                list.add(map);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //查看自己参与的
    public JSONArray ViewParticipant(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`participant` WHERE `userid` = " + userid + " AND `accept` = 2;";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while (resultSet1.next())
            {
                Map map = new HashMap();
                int activityid = resultSet1.getInt("activityid");
                String select2 = "SELECT * FROM `se`.`activity` WHERE `activityid` = " + activityid + ";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select2);
                while (resultSet2.next())
                {
                    int userid1 = resultSet2.getInt("userid");
                    String account1 = GetAccount(userid1);
                    String activityname = resultSet2.getString("activityname");
                    Timestamp starttime = resultSet2.getTimestamp("starttime");
                    Timestamp endtime = resultSet2.getTimestamp("endtime");
                    String address = resultSet2.getString("address");
                    String imgurl = resultSet2.getString("imgurl");

                    map.put("userid",String.valueOf(userid1));
                    map.put("account",account1);
                    map.put("activityid",String.valueOf(activityid));
                    map.put("activityname",activityname);
                    map.put("starttime",String.valueOf(starttime));
                    map.put("endtime",String.valueOf(endtime));
                    map.put("address",address);
                    map.put("imgurl",imgurl);
                }
                list.add(map);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //查看自己申请的
    public JSONArray ViewAccept(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`participant` WHERE `userid` = " + userid + " AND `accept` = 0;";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while (resultSet1.next())
            {
                Map map = new HashMap();
                int activityid = resultSet1.getInt("activityid");
                String select2 = "SELECT * FROM `se`.`activity` WHERE `activityid` = " + activityid + ";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select2);
                while (resultSet2.next())
                {
                    int userid1 = resultSet2.getInt("userid");
                    String account1 = GetAccount(userid1);
                    String activityname = resultSet2.getString("activityname");
                    Timestamp starttime = resultSet2.getTimestamp("starttime");
                    Timestamp endtime = resultSet2.getTimestamp("endtime");
                    String address = resultSet2.getString("address");
                    String imgurl = resultSet2.getString("imgurl");

                    map.put("userid",String.valueOf(userid1));
                    map.put("account",account1);
                    map.put("activityid",String.valueOf(activityid));
                    map.put("activityname",activityname);
                    map.put("starttime",String.valueOf(starttime));
                    map.put("endtime",String.valueOf(endtime));
                    map.put("address",address);
                    map.put("imgurl",imgurl);
                }
                list.add(map);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //普通用户申请身份“申请结果”：“等待审核”“申请失败”
    public JSONObject GetLevel(String account, int level)
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
            String insert = "INSERT INTO `se`.`permission`(`userid`,`level`) "
                    + "VALUES("+userid+","+level+");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            map.put("result","等待审核");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","已经申请");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //管理员审核申请“审核结果”：“审核完成”“审核出现意外错误”
    public JSONObject LevelPermission(String account, int level)
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
            String delete = "DELETE FROM `se`.`permission` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
            String update = "UPDATE `se`.`user` SET `identity` = " + level + " WHERE `userid` = " + userid + ";";
            Statement statement1 = connection.createStatement();
            statement.executeUpdate(update);
            map.put("result","审核完成");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","审核出现意外错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //管理员拒绝通过审核“审核结果”：“审核完成”“审核出现意外错误”
    public JSONObject RefusePermission(String account, int level)
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
            String delete = "DELETE FROM `se`.`permission` WHERE `userid` = " + userid + ";";
            Statement statement = connection.createStatement();
            statement.executeUpdate(delete);
            map.put("result","审核完成");
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("result","审核出现意外错误");
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //展示审核列表
    public JSONArray ViewAllPermission()
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`permission`;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                int userid = resultSet.getInt("userid");
                int level = resultSet.getInt("level");
                String account = GetAccount(userid);
                Map map = new HashMap();
                map.put("account",account);
                map.put("level",String.valueOf(level));
                list.add(map);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //展示所有通知
    public JSONArray ViewAllInform(String account)
    {
        List list = new LinkedList();
        try
        {
            ResultSet resultSet = GetInfo(account);
            int userid = 0;
            while(resultSet.next())
            {
                userid = resultSet.getInt("userid");
            }
            String select = "SELECT * FROM `se`.`participant` WHERE `userid` = " + userid + " AND `accept` = 2;";
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(select);
            while(resultSet1.next())
            {
                int activityid = resultSet1.getInt("activityid");
                String select1 = "SELECT * FROM `se`.`description` WHERE `activityid` =" + activityid + ";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet2 = statement1.executeQuery(select1);
                while (resultSet2.next())
                {
                    Map amap = new HashMap();
                    String d_content = resultSet2.getString("d_content");
                    Timestamp time = resultSet2.getTimestamp("time");
                    amap.put("d_content",d_content);
                    amap.put("time",String.valueOf(time));
                    list.add(amap);
                }
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }
}