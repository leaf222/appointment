package controller;

import com.example.UserAccess.UserService;
import com.example.UserAccess.UserSign;
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

    //显示审核信息
    @PostMapping("/viewaccept")
    public JSONObject ControllerViewAccept(@RequestBody JSONObject jsonObject)
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
}