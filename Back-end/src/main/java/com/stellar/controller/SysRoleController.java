package com.stellar.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stellar.entity.SysRole;
import com.stellar.entity.SysRoleMenu;
import com.stellar.entity.SysUserRole;
import com.stellar.service.ISysRoleMenuService;
import com.stellar.service.ISysRoleService;
import com.stellar.service.ISysUserRoleService;
import com.stellar.service.ISysUserService;
import com.stellar.common.lang.Const;
import com.stellar.common.lang.Result;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {
    @Autowired
    ISysRoleMenuService sysRoleMenuService;
    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    ISysUserService sysUserService;
    @Autowired
    ISysUserRoleService sysUserRoleService;

    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable(name = "id") Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        List<SysRoleMenu> roleMenus =
            sysRoleMenuService.list(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        List<Long> menuIds =
            roleMenus.stream().map(p -> p.getMenuId()).collect(Collectors.toList());
        sysRole.setMenuIds(menuIds);
        return Result.succ(sysRole);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result list(String name) {
        Page<SysRole> roles = sysRoleService.page(getPage(),
            new QueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(name), "name", name)
        );
        return Result.succ(roles);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:role:save')")
    public Result save(@Validated @RequestBody SysRole sysRole) {

        sysRole.setCreated(LocalDateTime.now());
        sysRole.setStatus(Const.STATUS_ON);
        sysRoleService.save(sysRole);

        return Result.succ(sysRole);

    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:role:update')")
    public Result update(@Validated @RequestBody SysRole sysRole) {
        sysRole.setUpdated(LocalDateTime.now());

        sysRoleService.updateById(sysRole);

        // Update the cache
        sysUserService.clearUserAuthorityInfoByRoleId(sysRole.getId());
        return Result.succ(sysRole);
    }

    @PostMapping("/delete")
    @Transactional
    @PreAuthorize("hasAuthority('sys:role:delete')")
    public Result delete(@RequestBody Long[] ids){

        List<Long> ArrayIds = Arrays.asList(ids);
        sysRoleService.removeByIds(ArrayIds);

        // Delete intermediate table
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().in("role_id",ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("role_id",ids));

        // Clear permission information related to the old menu in cache.
        Arrays.stream(ids).forEach(id ->{
        sysUserService.clearUserAuthorityInfoByRoleId(id);
        });
        return Result.succ("");

}

    @Transactional
    @PreAuthorize("hasAuthority('sys:role:perm')")
    @PostMapping("/permission/{roleId}")
    public Result assignPermissions(@PathVariable("roleId") Long roleId, @RequestBody Long[] menuIds){
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId ->{
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);

            sysRoleMenus.add(sysRoleMenu);
        });

        // Update to databases
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq("role_id", roleId));
        sysRoleMenuService.saveBatch(sysRoleMenus);

        // Clear permission information related to the old menu in cache.
        sysUserService.clearUserAuthorityInfoByRoleId(roleId);

        return Result.succ(menuIds);




    }


}
