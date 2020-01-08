package com.example.demo;

import com.example.ActivityAccess.BrowseActivity;
import com.example.ActivityAccess.CreateActivity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.*;
import java.text.SimpleDateFormat;


@EnableTransactionManagement
@SpringBootApplication
@RestController
@EnableEurekaClient
@EnableSwagger2
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@RequestMapping("/allactivity")
	public JSONArray ControllerAllActivityInfo()
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetAllInfo();
	}

	//搜索活动
	@RequestMapping("/searchactivity")
	public JSONArray ControllerSearchActivity(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.SearchActivity(jsonObject.getString("activityname"));
	}

	//查看活动详情，活动本身的信息
	@RequestMapping("/activityinfo")
	public JSONObject ControllerGetActivityInfo(@RequestParam(value = "jsonObject") JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetInfo(jsonObject.getInt("activityid"));
	}

	//返回活动的tag
	@RequestMapping("/activitytag")
	public JSONArray ControllerActivityTag(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetTag(jsonObject.getInt("activityid"));
	}

	//返回活动的描述
	@RequestMapping("/activitydescription")
	public JSONArray ControllerActivityDescription(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return  browseActivity.GetDescription(jsonObject.getInt("activityid"));
	}

	//返回活动的评论
	@RequestMapping("/activitycomment")
	public JSONArray ControllerActivityComment(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetComment(jsonObject.getInt("activityid"));
	}

	//返回活动的收藏数，参加数
	@RequestMapping("/getotherinfo")
	public JSONObject ControllerGetOtherInfo(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetOtherInfo(jsonObject.getInt("activityid"));
	}

	//创建活动
	@RequestMapping("/createactivity")
	public JSONObject ControllerCreateActivity(@RequestBody JSONObject jsonObject)
	{
		CreateActivity createActivity = new CreateActivity();
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
	@RequestMapping("/deleteactivity")
	public JSONObject ControllerDeleteActivity(@RequestBody JSONObject jsonObject)
	{
		CreateActivity createActivity = new CreateActivity();
		return createActivity.DeleteActivity(jsonObject.getInt("activityid"));
	}

	//修改活动信息
	@RequestMapping("/modifyactivity")
	public JSONObject ControllerModifyActivity(@RequestBody JSONObject jsonObject)
	{
		Timestamp time= new Timestamp(System.currentTimeMillis());//获取系统当前时间
		SimpleDateFormat df = new SimpleDateFormat(jsonObject.getString("time"));
		String timeStr = df.format(time);
		time = Timestamp.valueOf(timeStr);

		CreateActivity createActivity = new CreateActivity();
		return createActivity.ModifyActivity(jsonObject.getString("d_content")
				,time
				,jsonObject.getInt("activityid"));
	}

	//根据tag获取有此tag的活动信息
	@RequestMapping("/getactivitybytag")
	public JSONArray ControllerGetActivityByTag(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.GetInfoByTag(jsonObject.getString("tagname"));
	}

	//为活动添加tag
	@RequestMapping("/addtagtoactivity")
	public JSONObject ControllerAddTagToActivity(@RequestBody JSONObject jsonObject)
	{
		CreateActivity createActivity = new CreateActivity();
		return createActivity.AddActivityTag(jsonObject.getInt("activityid")
				,jsonObject.getString("tagname"));
	}

	//返回所有tag
	@RequestMapping("/showalltag")
	public JSONArray ControllerShowAllTag(@RequestBody JSONObject jsonObject)
	{
		CreateActivity createActivity = new CreateActivity();
		return createActivity.ShowAllTag();
	}

	//按参与人数排名返回
	@RequestMapping("/rankbyparticipant")
	public JSONArray ControllerRankByParticipant(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.RankByParticipant();
	}

	//按评分排名返回
	@RequestMapping("/rankbyscore")
	public JSONArray ControllerRankByScore(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.RankByScore();
	}

	//按收藏数排名返回
	@RequestMapping("/rankbyfavorite")
	public JSONArray ControllerRankByFavorite(@RequestBody JSONObject jsonObject)
	{
		BrowseActivity browseActivity = new BrowseActivity();
		return browseActivity.RankByFavorite();
	}
}
