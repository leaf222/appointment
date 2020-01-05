package controller;

import com.example.UserAccess.UserService;
import com.example.UserAccess.UserSign;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
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
@RequestMapping(value = "/user")
public class UserController
{
    @Autowired
    UserSign userSign;
    UserService userService;

    //用户登录
    @PostMapping("/signin")
    public JSONObject ControllerSignIn(@RequestBody JSONObject jsonObject)
    {
        return  userSign.SignIn(jsonObject.getString("account")
        ,jsonObject.getString("password"));
    }

    //用户注册
    @PostMapping("/signup")
    public JSONObject ControllerSignUp(@RequestBody JSONObject jsonObject)
    {
        return userSign.SignUp(jsonObject.getString("account")
        ,jsonObject.getString("password"));
    }

    //审核申请
    @PostMapping("/accept")
    public JSONObject ControllerAccept(@RequestBody JSONObject jsonObject)
    {
        return userService.Accept(jsonObject.getString("account")
        ,jsonObject.getInt("activityid")
        ,jsonObject.getInt("accept"));
    }

    //退出登录
    @PostMapping("/signout")
    public JSONObject ControllerSignOut(@RequestBody JSONObject jsonObject)
    {
        return userSign.SignOut(jsonObject.getString("account"));
    }

    //修改个人信息
    @PostMapping("/modifyinfo")
    public JSONObject ControllerModifyInfo(@RequestBody JSONObject jsonObject)
    {
        return userSign.UpdateInfo(jsonObject.getString("account")
        ,jsonObject.getString("name")
        ,jsonObject.getBoolean("gender")
        ,jsonObject.getString("phonenumber")
        ,jsonObject.getString("job"));
    }

    //修改头像
    @PostMapping("/addpicture")
    public JSONObject ControllerAddPicture(@RequestBody JSONObject jsonObject)
    {
        return userSign.AddPicture(jsonObject.getString("account")
        ,jsonObject.getString("url"));
    }

    //显示审核信息
    @PostMapping("/viewaccept")
    public JSONArray ControllerViewAccept(@RequestBody JSONObject jsonObject)
    {
        return userService.GetParticipantInfo(jsonObject.getString("account"));
    }

    //评论活动
    @PostMapping("/commentactivity")
    public JSONObject ControllerCommentActivity(@RequestBody JSONObject jsonObject)
    {
        Timestamp c_time= new Timestamp(System.currentTimeMillis());//获取系统当前时间
        SimpleDateFormat df = new SimpleDateFormat(jsonObject.getString("c_time"));
        String timeStr = df.format(c_time);
        c_time = Timestamp.valueOf(timeStr);
        return userService.Comment(jsonObject.getString("c_content")
        ,c_time
        ,jsonObject.getString("account")
        ,jsonObject.getInt("activityid")
        , (float) jsonObject.getDouble("score"));
    }

    //申请参加活动
    @PostMapping("/joinactivity")
    public JSONObject ControllerJoinActivity(@RequestBody JSONObject jsonObject)
    {
        return userService.JoinActivity(jsonObject.getString("account")
        ,jsonObject.getInt("activityid"));
    }

    //退出活动
    @PostMapping("/exitactivity")
    public JSONObject ControllerExitActivity(@RequestBody JSONObject jsonObject)
    {
        return userService.ExitActivity(jsonObject.getString("account")
        ,jsonObject.getInt("activityid"));
    }

    //收藏活动
    @PostMapping("/favoriteactivity")
    public JSONObject ControllerFavoriteActivity(@RequestBody JSONObject jsonObject)
    {
        return userService.FavoriteActivity(jsonObject.getString("account")
        ,jsonObject.getInt("activityid"));
    }

    //取消收藏
    @PostMapping("/cancelfavorite")
    public JSONObject ControllerCancelFavorite(@RequestBody JSONObject jsonObject)
    {
        return userService.CancelFavorite(jsonObject.getString("account")
        ,jsonObject.getInt("activityid"));
    }

    //查看收藏
    @PostMapping("/viewfavorite")
    public JSONArray ControllerViewFavorite(@RequestBody JSONObject jsonObject)
    {
        return userService.ViewFavorite(jsonObject.getString("account"));
    }

    //查看发布
    @PostMapping("/viewpublish")
    public JSONArray ControllerViewPublish(@RequestBody JSONObject jsonObject)
    {
        return userService.ViewPublish(jsonObject.getString("account"));
    }

    //查看参与
    @PostMapping("/viewparticipant")
    public JSONArray ControllerViewParticipant(@RequestBody JSONObject jsonObject)
    {
        return userService.ViewParticipant(jsonObject.getString("account"));
    }

    //查看申请
    @PostMapping("/viewmypermission")
    public JSONArray ControllerViewMyPermission(@RequestBody JSONObject jsonObject)
    {
        return userService.ViewAccept(jsonObject.getString("account"));
    }

    //普通用户申请等级
    @PostMapping("/getlevel")
    public JSONObject ControllerGetLevel(@RequestBody JSONObject jsonObject)
    {
        return userService.GetLevel(jsonObject.getString("account")
        ,jsonObject.getInt("level"));
    }

    //管理员通过审核
    @PostMapping("/acceptpermission")
    public JSONObject ControllerAcceptPermission(@RequestBody JSONObject jsonObject)
    {
        return userService.LevelPermission(jsonObject.getString("account")
        ,jsonObject.getInt("level"));
    }

    //管理员拒绝审核
    @PostMapping("/refusepermission")
    public JSONObject ControllerRefusePermission(@RequestBody JSONObject jsonObject)
    {
        return userService.RefusePermission(jsonObject.getString("account")
        ,jsonObject.getInt("level"));
    }

    //展示审核列表
    @PostMapping("/viewallpermission")
    public JSONArray ControllerViewAllPermission(@RequestBody JSONObject jsonObject)
    {
        return userService.ViewAllPermission();
    }
}