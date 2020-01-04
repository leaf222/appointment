package controller;

import com.example.UserAccess.UserSign;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}