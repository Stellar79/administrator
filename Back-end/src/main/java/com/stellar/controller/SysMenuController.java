package com.stellar.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stellar.entity.SysRoleMenu;
import com.stellar.entity.SysUser;
import com.stellar.service.ISysMenuService;
import com.stellar.service.ISysRoleMenuService;
import com.stellar.service.ISysUserService;
import com.stellar.common.lang.Result;
import com.stellar.dto.SysMenuDto;
import com.stellar.entity.SysMenu;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    @Autowired
    ISysUserService sysUserService;
    @Autowired
    ISysMenuService sysMenuService;
    @Autowired
    ISysRoleMenuService sysRoleMenuService;
    /*Get the menus, actually permissions of current user.*/
    @GetMapping("/nav")
    public Result getNav(Principal principal){
        String username = principal.getName();
        SysUser sysUser = sysUserService.getByUsername(username);
        // Get Authorities: ROLE_Admin,sys:user:save
        String[] authoritiesInfoArray =
            StringUtils.tokenizeToStringArray(sysUserService.getUserAuthorityInfo(sysUser.getId()),
                ",");

        // Get Navigation of current user
        List<SysMenuDto> currentUserNav = sysMenuService.getCurrentUserNav();
        return Result.succ(
            MapUtil.builder()
                .put("nav",currentUserNav)
                .put("authorities",authoritiesInfoArray)
                .map()
        );

    }

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result info(@PathVariable(name="id") Long id){
        return Result.succ(sysMenuService.getById(id));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:menu:list')")
    public Result list(){
        List<SysMenu> menus = sysMenuService.tree();
        return Result.succ(menus);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:menu:save')")
    public Result save(@Validated @RequestBody SysMenu sysMenu){

        sysMenu.setCreated(LocalDateTime.now());
        sysMenuService.save(sysMenu);

        return Result.succ(sysMenu);

    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:menu:update')")
    public Result update(@Validated @RequestBody SysMenu sysMenu){
        sysMenu.setUpdated(LocalDateTime.now());
        sysMenuService.updateById(sysMenu);

        // Clear permission information related to the old menu in cache.
        sysUserService.clearUserAuthorityInfoByMenuId(sysMenu.getId());

        return Result.succ(sysMenu);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('sys:menu:delete')")
    public Result delete(@PathVariable(name="id") Long id){
        int parentMenuCount = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (parentMenuCount>0){
            return Result.fail("Please delete sub menu first! ");
        }

        // Clear permission information related to the old menu in cache.
        sysUserService.clearUserAuthorityInfoByMenuId(id);
        sysMenuService.removeById(id);

        //delete intermediate table
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("menu_id",id));
        return Result.succ("");

    }

}
