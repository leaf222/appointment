package controller;

import com.example.UserAccess.UserSign;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
@RestController
@RequestMapping(value = "/user")
public class UserController
{
    @Autowired
    UserSign userSign;

    @PostMapping("/signin")
    public JSONObject ControllerSignIn(@RequestBody JSONObject jsonObject)
    {
        return  userSign.SignIn(jsonObject.getString("account")
        ,jsonObject.getString("password"));
    }
}