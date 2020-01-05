package controller;

import com.example.ActivityAccess.BrowseActivity;
import com.example.ActivityAccess.CreateActivity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @program: appointment
 * @description:
 * @author: Yifan Ye
 * @create: 2020/01/04
 **/
@Transactional
@RestController
@RequestMapping(value = "/activity")
public class ActivityController
{
    @Autowired
    BrowseActivity browseActivity;
    CreateActivity createActivity;

    //查看所有活动
    @PostMapping("/allactivity")
    public JSONArray ControllerAllActivityInfo(@RequestBody JSONObject jsonObject)
    {
        return browseActivity.GetAllInfo();
    }

    //搜索活动
    @PostMapping("/searchactivity")
    public JSONArray ControllerSearchActivity(@RequestBody JSONObject jsonObject)
    {
        return browseActivity.SearchActivity(jsonObject.getString("activityname"));
    }

    //查看活动详情，活动本身的信息
    @PostMapping("/activityinfo")
    public JSONObject ControllerGetActivityInfo(@RequestBody JSONObject jsonObject)
    {
        return browseActivity.GetInfo(jsonObject.getInt("activityid"));
    }

    //返回活动的tag
    @PostMapping("/activitytag")
    public JSONArray ControllerActivityTag(@RequestBody JSONObject jsonObject)
    {
        return browseActivity.GetTag(jsonObject.getInt("activityid"));
    }

    //返回活动的描述
    @PostMapping("/activitydescription")
    public JSONArray ControllerActivityDescription(@RequestBody JSONObject jsonObject)
    {
        return  browseActivity.GetDescription(jsonObject.getInt("activityid"));
    }

    //返回活动的评论
    @PostMapping("/activitycomment")
    public JSONArray ControllerActivityComment(@RequestBody JSONObject jsonObject)
    {
        return browseActivity.GetComment(jsonObject.getInt("activityid"));
    }

    //创建活动
    @PostMapping("/createactivity")
    public JSONObject ControllerCreateActivity(@RequestBody JSONObject jsonObject)
    {
        Timestamp starttime= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        SimpleDateFormat df = new SimpleDateFormat(jsonObject.getString("starttime"));
        String timeStr = df.format(starttime);
        starttime = Timestamp.valueOf(timeStr);
        Timestamp endtime= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        df = new SimpleDateFormat(jsonObject.getString("endtime"));
        timeStr = df.format(endtime);
        endtime = Timestamp.valueOf(timeStr);

        return createActivity.AddToActivity(jsonObject.getString("account")
        ,jsonObject.getString("activityname")
        ,starttime//这里创建Timestamp的时候，以毫秒数
        ,endtime//作为构造的参数
        ,jsonObject.getString("address")
        ,jsonObject.getString("imgurl")
        ,jsonObject.getString("description"));
    }

    //删除活动
    @PostMapping("/deleteactivity")
    public JSONObject ControllerDeleteActivity(@RequestBody JSONObject jsonObject)
    {
        return createActivity.DeleteActivity(jsonObject.getInt("activityid"));
    }

    //修改活动信息
    @PostMapping("/modifyactivity")
    public JSONObject ControllerModifyActivity(@RequestBody JSONObject jsonObject)
    {
        Timestamp time= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        SimpleDateFormat df = new SimpleDateFormat(jsonObject.getString("time"));
        String timeStr = df.format(time);
        time = Timestamp.valueOf(timeStr);

        return createActivity.ModifyActivity(jsonObject.getString("d_content")
        ,time
        ,jsonObject.getInt("activityid"));
    }
}