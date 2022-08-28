package com.stellar.controller;

import cn.hutool.http.server.HttpServerRequest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import javax.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

public class BaseController {
@Autowired
    ServletRequest request;
    public Page getPage(){
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 10);
        return new Page(current,size);

    }
}
