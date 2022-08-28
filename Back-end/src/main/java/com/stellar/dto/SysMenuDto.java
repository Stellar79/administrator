package com.stellar.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/*Menu 返回前端的格式：
* {
        name: 'SysUser',
        title: 'User',
        icon: 'el-icon-s-custom',
        path: '/sys/user',
        component: 'sys/User',
        children: []
    },*/
@Data
public class SysMenuDto implements Serializable {
    private Long id;

    private String title;
    private String icon;
    private String path;
    private String name;
    private String component;
    private List<SysMenuDto> children = new  ArrayList<>();

}
