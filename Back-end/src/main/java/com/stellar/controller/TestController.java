package com.stellar.controller;

import cn.hutool.core.map.MapUtil;
import com.stellar.service.ISysUserService;
import com.stellar.common.lang.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    @Autowired
    ISysUserService sysUserService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public Object test(){
        return Result.succ(sysUserService.list());
    }

    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/password")
    public Result pwdEncode(){
        // Encode the password
        String encodedPassword = bCryptPasswordEncoder.encode("111111");

        // Password validation
        boolean isMatches = bCryptPasswordEncoder.matches("111111", encodedPassword);

        return Result.succ(MapUtil.builder()
            .put("encodedPassword",encodedPassword)
            .put("isMatches",isMatches)
            .build()
        );
    }
}
