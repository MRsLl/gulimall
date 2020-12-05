package com.atguigu.guli.service.ucenter.controller.api;

import com.atguigu.guli.common.util.utils.Constant;
import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.helper.JwtHelper;
import com.atguigu.guli.service.base.helper.JwtInfo;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.service.MemberService;
import com.atguigu.guli.service.ucenter.util.UcenterProperties;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "微信 api 管理")
@Slf4j
@Controller

@RequestMapping("/api/ucenter/wx")
public class ApiWxController {

    @Autowired
    private UcenterProperties ucenterProperties;
    @Autowired
    private MemberService memberService;

    private String appId;
    private String appSecret;
    private String redirectUri;

    @PostConstruct
    public void init() {
        this.appId = ucenterProperties.getAppId();
        this.appSecret = ucenterProperties.getAppSecret();
        this.redirectUri = ucenterProperties.getRedirectUri();
    }


    /**
     * 1. 第三方发起微信授权登录请求，微信用户允许授权第三方应用后，微信会拉起应用或重定向到第三方网站，并且带上授权临时票据code参数；
     * 2. 通过code参数加上AppID和AppSecret等，通过API换取access_token；
     * 3. 通过access_token进行接口调用，获取用户基本数据资源或帮助用户实现基本操作。
     *
     * @return
     */
    @ApiOperation("微信登录")
    @GetMapping("login")
    public String login(HttpSession session) {
        String codeUrl = "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=%s&" +
                "redirect_uri=%s&" +
                "response_type=code&" +
                "scope=snsapi_login&" +
                "state=%s#" +
                "wechat_redirect";
        //用urlEncoder 处理回调地址
        String callBackUrl = "";
        try {
            callBackUrl = URLEncoder.encode(this.redirectUri, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new GuliException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
        //随机生成一个状态码保存到session 中，以防csrf 攻击
        String state = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute(Constant.WX_STATE, state);

        //将参数填充到微信授权链接
        codeUrl = String.format(codeUrl, this.appId, callBackUrl, state);

        //使客户端浏览器重定向到微信授权页面
        return "redirect:" + codeUrl;
    }

    @ApiOperation("用户微信授权后获取用户信息")
    @GetMapping("callback")
    public String callback(String code, String state, HttpSession session) {

        //判断回调请求是否合法
        if (!session.getAttribute(Constant.WX_STATE).equals(state)) {
            throw new GuliException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //填充得到从微信获取 access_token 的地址
        String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=%s&" +
                "secret=%s&" +
                "code=%s&" +
                "grant_type=authorization_code";

        accessTokenUrl = String.format(accessTokenUrl, this.appId, this.appSecret, code);

        HttpClientUtils clientUtils = new HttpClientUtils(accessTokenUrl);

        String responseBody = "";
        try {
            //使用HttpClientUtils 向微信发送请求获取access_token
            clientUtils.get();
            responseBody = clientUtils.getContent();
        } catch (Exception e) {
            log.error("获取access_token失败");
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        Gson gson = new Gson();
        Map resMap = gson.fromJson(responseBody, Map.class);

        if (resMap.get("errcode") != null) {
            throw new GuliException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }

        //根据openid 从数据库中查询，看用户账号是否已注册
        String accessToken = (String) resMap.get("access_token");
        String openid = (String) resMap.get("openid");

        Member member = memberService.getByOpenId(openid);

        JwtInfo jwtInfo = new JwtInfo();
        //若用户已注册，直接获取用户信息，封装成token 返回给用户前端
        if (member != null) {
            jwtInfo.setId(member.getId());
            jwtInfo.setNickname(member.getNickname());
            jwtInfo.setAvatar(member.getAvatar());
        } else {
            //若用户没有注册，则携带access_token 和 openid 向微信发送请求获取用户信息
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                    "access_token=%s&" +
                    "openid=%s";
            userInfoUrl = String.format(userInfoUrl, accessToken, openid);

            HashMap<String, String> paramMap = new HashMap<>();
            //access_token=ACCESS_TOKEN&openid=OPENID
            paramMap.put("access_token", accessToken);
            paramMap.put("openid", openid);

            HttpClientUtils userClient = new HttpClientUtils(userInfoUrl, paramMap);
            String userInfo = null;

            try {
                userClient.get();
                userInfo = userClient.getContent();
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            Map userInfoMap = gson.fromJson(userInfo, Map.class);

            if (userInfoMap.get("errcode") != null) {
                log.error("获取用户信息失败" + "，message：" + userInfoMap.get("errmsg"));
                throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
            }

            //将所获用户信息注册
            Member wxMember = memberService.saveMap(userInfoMap);

            //将所获用户信息封装成 token 返回给用户前端
            String nickname = (String) userInfoMap.get("nickname");
            String avatar = (String) userInfoMap.get("headimgurl");

            //必须设置jwtInfo 的id，JwtHelper 要用
            jwtInfo.setId(wxMember.getId());
            jwtInfo.setNickname(nickname);
            jwtInfo.setAvatar(avatar);
        }

        String token = JwtHelper.createToken(jwtInfo);
        //将token 传递给用户前端，并使其重定向到主页
        return "redirect:http://localhost:3333?token=" + token;

    }
}
