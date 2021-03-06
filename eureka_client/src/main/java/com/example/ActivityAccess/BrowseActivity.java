package com.example.ActivityAccess;

import com.mysql.cj.protocol.Resultset;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
        JSONArray result = browseActivity.GetAllInfo();
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
    public JSONArray GetAllInfo()
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`activity`;";
            Statement statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery(select);
            int i = 0;
            while (resultset.next())
            {
                Map amap = new HashMap();
                int activityid = resultset.getInt("activityid");
                int userid = resultset.getInt("userid");
                String activityname = resultset.getString("activityname");
                Timestamp starttime = resultset.getTimestamp("starttime");
                Timestamp endtime = resultset.getTimestamp("endtime");
                String address = resultset.getString("address");
                String imgurl = resultset.getString("imgurl");

                amap.put("activityid",String.valueOf(activityid));
                amap.put("userid",String.valueOf(userid));
                amap.put("activityname",activityname);
                amap.put("starttime",String.valueOf(starttime));
                amap.put("endtime",String.valueOf(endtime));
                amap.put("address",address);
                amap.put("imgurl",imgurl);

                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
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
                int userid = resultSet.getInt("userid");
                String activityname = resultSet.getString("activityname");
                Timestamp starttime = resultSet.getTimestamp("starttime");
                Timestamp endtime = resultSet.getTimestamp("endtime");
                String address = resultSet.getString("address");
                String imgurl = resultSet.getString("imgurl");

                map.put("userid",String.valueOf(userid));
                map.put("activityid",String.valueOf(activityid));
                map.put("activityname",activityname);
                map.put("starttime",String.valueOf(starttime));
                map.put("endtime",String.valueOf(endtime));
                map.put("address",address);
                map.put("imgurl",imgurl);
            }
            return  map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  map;
    }

    //通过id获取活动信息
    public JSONObject GetInfo(int activityid)
    {
        Map map = new HashMap();
        try
        {
            String select = "SELECT * FROM `se`.`activity` WHERE `activityid` =" + activityid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                int userid = resultSet.getInt("userid");
                String activityname = resultSet.getString("activityname");
                Timestamp starttime = resultSet.getTimestamp("starttime");
                Timestamp endtime = resultSet.getTimestamp("endtime");
                String address = resultSet.getString("address");
                String imgurl = resultSet.getString("imgurl");

                map.put("userid",String.valueOf(userid));
                map.put("activityid",String.valueOf(activityid));
                map.put("activityname",activityname);
                map.put("starttime",String.valueOf(starttime));
                map.put("endtime",String.valueOf(endtime));
                map.put("address",address);
                map.put("imgurl",imgurl);
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return  jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //通过tag名称获取所有拥有此tag的活动信息
    public JSONArray GetInfoByTag(String tagname)
    {
        List list = new LinkedList();
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
            while (resultSet2.next())
            {
                int activityid = resultSet2.getInt("activityid");
                Map amap = GetInfoById(activityid);

                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
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
                String imgurl = resultSet.getString("imgurl");

                amap.put("activityid",String.valueOf(activityid));
                amap.put("userid",String.valueOf(userid));
                amap.put("activityname",activityname);
                amap.put("starttime",String.valueOf(starttime));
                amap.put("endtime",String.valueOf(endtime));
                amap.put("address",address);
                amap.put("imgurl",imgurl);

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

    //搜索活动
    public JSONArray SearchActivity(String s)
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`activity` WHERE `activityname` LIKE '%" + s + "%';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                Map amap = new HashMap();
                int activityid = resultSet.getInt("activityid");
                int userid = resultSet.getInt("userid");
                String activityname = resultSet.getString("activityname");
                Timestamp starttime = resultSet.getTimestamp("starttime");
                Timestamp endtime = resultSet.getTimestamp("endtime");
                String address = resultSet.getString("address");
                String imgurl = resultSet.getString("imgurl");

                amap.put("activityid",String.valueOf(activityid));
                amap.put("userid",String.valueOf(userid));
                amap.put("activityname",activityname);
                amap.put("starttime",String.valueOf(starttime));
                amap.put("endtime",String.valueOf(endtime));
                amap.put("address",address);
                amap.put("imgurl",imgurl);

                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //返回活动对应的tag
    public JSONArray GetTag(int activityid)
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`has_tag` WHERE `activityid` =" + activityid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                int tagid = resultSet.getInt("tagid");
                String select1 = "SELECT * FROM `se`.`tag` WHERE tagid = " + tagid + ";";
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(select1);
                while (resultSet1.next())
                {
                    Map map = new HashMap();
                    String tagname = resultSet1.getString("tagname");
                    map.put("tag",tagname);
                    list.add(map);
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

    //返回活动对应的描述
    public JSONArray GetDescription(int activityid)
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`description` WHERE `activityid` =" + activityid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                Map amap = new HashMap();
                String d_content = resultSet.getString("d_content");
                Timestamp time = resultSet.getTimestamp("time");
                amap.put("d_content",d_content);
                amap.put("time",String.valueOf(time));
                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //返回活动对应的评论
    public JSONArray GetComment(int activityid)
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT * FROM `se`.`comment` WHERE `activityid` = " + activityid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next())
            {
                Map amap = new HashMap();
                int userid = resultSet.getInt("userid");
                String account = null;
                String select2 = "SELECT * FROM `se`.`user` WHERE `userid` = " + userid + ";";
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(select2);
                while (resultSet2.next())
                {
                    account = resultSet2.getString("account");
                }

                String c_content = resultSet.getString("c_content");
                Timestamp c_time =  resultSet.getTimestamp("c_time");
                Float score = resultSet.getFloat("score");
                amap.put("account",account);
                amap.put("c_content",c_content);
                amap.put("c_time",String.valueOf(c_time));
                amap.put("score",String.valueOf(score));
                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return jsonArray;
    }

    //返回活动对应的收藏数，参加人数
    public JSONObject GetOtherInfo(int activityid)
    {
        Map map = new HashMap();
        try
        {
            int participantNumber = 0;
            int favoriteNumber = 0;
            String getFavorite = "SELECT * FROM\n" +
                    "(SELECT `activityid`,`count_userid` FROM\n" +
                    "(SELECT `activityid`,count(`userid`) as `count_userid`\n" +
                    "FROM `se`.`favorite`\n" +
                    "GROUP BY `activityid`) as `q`\n" +
                    "ORDER BY `count_userid` DESC) as `p`\n" +
                    "WHERE `activityid` = " + activityid + ";";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getFavorite);
            while(resultSet.next())
            {
                favoriteNumber = resultSet.getInt("count_userid");
                map.put("favoritenumber",String.valueOf(favoriteNumber));
            }
            String getParticipant = "SELECT * FROM " +
                    "(SELECT `activityid`,`count_userid` FROM " +
                    "(SELECT `activityid`,count(`userid`) as `count_userid` " +
                    "FROM `se`.`participant` " +
                    "GROUP BY `activityid`) as `q` " +
                    "ORDER BY `count_userid` DESC) as `p` " +
                    "WHERE `activityid` = 1;";
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(getParticipant);
            while (resultSet1.next())
            {
                participantNumber = resultSet1.getInt("count_userid");
                map.put("participantnumber",String.valueOf(participantNumber));
            }
            JSONObject jsonObject = JSONObject.fromObject(map);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    //按评分返回活动
    public JSONArray RankByScore()
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT `activityid`,`avg_score` FROM " +
                    "(SELECT `activityid`,avg(`score`) as `avg_score` " +
                    "FROM `se`.`comment` " +
                    "GROUP BY `activityid`) as `q` " +
                    "ORDER BY `avg_score` DESC;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                int activityid = resultSet.getInt("activityid");
                Map amap = GetInfoById(activityid);
                amap.put("rank",String.valueOf(i));
                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return  jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return  jsonArray;
    }

    //按参与人数返回
    public JSONArray RankByParticipant()
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT `activityid`,`count_userid` FROM " +
                    "(SELECT `activityid`,count(`userid`) as `count_userid` " +
                    "FROM `se`.`comment` " +
                    "GROUP BY `activityid`) as `q` " +
                    "ORDER BY `count_userid` DESC;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                int activityid = resultSet.getInt("activityid");
                Map amap = GetInfoById(activityid);
                amap.put("rank",String.valueOf(i));
                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return  jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return  jsonArray;
    }

    //按收藏数返回
    public JSONArray RankByFavorite()
    {
        List list = new LinkedList();
        try
        {
            String select = "SELECT `activityid`,`count_userid` FROM " +
                    "(SELECT `activityid`,count(`userid`) as `count_userid` " +
                    "FROM `se`.`favorite` " +
                    "GROUP BY `activityid`) as `q` " +
                    "ORDER BY `count_userid` DESC;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(select);
            int i = 0;
            while (resultSet.next())
            {
                i++;
                int activityid = resultSet.getInt("activityid");
                Map amap = GetInfoById(activityid);
                amap.put("rank",String.valueOf(i));
                list.add(amap);
            }
            JSONArray jsonArray = JSONArray.fromObject(list);
            return  jsonArray;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.fromObject(list);
        return  jsonArray;
    }
}