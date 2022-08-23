package com.stellar.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.stellar.entity.SysUser;
import com.stellar.service.ISysUserService;
import com.stellar.common.lang.Const;
import com.stellar.common.lang.Result;
import com.stellar.utils.RedisUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;


@RestController
public class AuthController extends BaseController{
    @Autowired
    Producer producer;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ISysUserService userService;
    @GetMapping("/captcha")
    public Result captcha() throws IOException{
        String key = UUID.randomUUID().toString();
        String captchaCode = producer.createText();

        System.out.println(key);
        System.out.println(captchaCode);

        BufferedImage captchaImage = producer.createImage(captchaCode);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(captchaImage,"jpg",byteArrayOutputStream);

        BASE64Encoder base64Encoder = new BASE64Encoder();
        String prefix = "data:image/jpeg;base64,";
        String base64Img = prefix + base64Encoder.encode(byteArrayOutputStream.toByteArray());
        redisUtil.hset(Const.captcha_KEY,key,captchaCode,120);

        return Result.succ(
            MapUtil.builder()
                .put("token",key)
                .put("captchaImg",base64Img)
                .build()
        );
    }

    @GetMapping("/sys/userInfo")
    public Result getUserInfo(Principal principal){
        SysUser sysUser = userService.getByUsername(principal.getName());
        return Result.succ(
            MapUtil.builder()
                .put("id",sysUser.getId())
                .put("username",sysUser.getUsername())
                .put("avatar",sysUser.getAvatar())
                .put("created",sysUser.getCreated())
                .map()
        );
    }
}
