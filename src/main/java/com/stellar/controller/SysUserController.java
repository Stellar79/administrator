package com.stellar.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.stellar.dto.PwdDto;
import com.stellar.entity.SysRole;
import com.stellar.entity.SysUser;
import com.stellar.entity.SysUserRole;
import com.stellar.service.ISysRoleService;
import com.stellar.service.ISysUserRoleService;
import com.stellar.service.ISysUserService;
import com.stellar.common.lang.Const;
import com.stellar.common.lang.Result;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    ISysUserService sysUserService;
    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ISysUserRoleService sysUserRoleService;



    // Binding role information to current user
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAuthority('sys:role:list')")
    public Result info(@PathVariable(name = "id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        Assert.notNull(sysUser, "Cannot find the administrator");

        List<SysRole> roles = sysRoleService.listRolesByUserId(id);
        sysUser.setRoles(roles);
        return Result.succ(sysUser);
    }


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('sys:user:list')")
    public Result list(String username) {
        Page<SysUser> users = sysUserService.page(getPage(),
            new QueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(username), "username", username)
        );

        users.getRecords().forEach(u -> {
            u.setRoles(sysRoleService.listRolesByUserId(u.getId()));
        });
        return Result.succ(users);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('sys:user:save')")
    public Result save(@Validated @RequestBody SysUser sysUser) {
        sysUser.setCreated(LocalDateTime.now());
        sysUser.setStatus(Const.STATUS_ON);

        String password = passwordEncoder.encode(Const.DEFALT_PASSWORD);

        sysUser.setAvatar(Const.DEFALT_AVATAR);
        sysUser.setPassword(password);

        sysUserService.save(sysUser);

        return Result.succ(sysUser);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('sys:user:update')")
    public Result update(@Validated @RequestBody SysUser sysUser) {
        sysUser.setUpdated(LocalDateTime.now());

        sysUserService.updateById(sysUser);

        return Result.succ(sysUser);
    }

    @PostMapping("/delete")
    @Transactional
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result delete(@RequestBody Long[] ids) {

        List<Long> ArrayIds = Arrays.asList(ids);
        sysUserService.removeByIds(ArrayIds);


        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));

        return Result.succ("");
    }

    @PostMapping("/role_assign/{userId}")
    @Transactional
    @PreAuthorize("hasAuthority('sys:user:role')")
    public Result roleAssign(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds){
        // Assign roles to selected user
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id",userId));

       List<SysUserRole> userRoles = new ArrayList<>();
       Arrays.stream(roleIds).forEach(roleId->{
           SysUserRole sysUserRole = new SysUserRole();
           sysUserRole.setUserId(userId);
           sysUserRole.setRoleId(roleId);
           userRoles.add(sysUserRole);
       });

       sysUserRoleService.saveBatch(userRoles);
        // Delete cache
        SysUser sysUser = sysUserService.getById(userId);
        sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

        return Result.succ("");

    }

    @PostMapping("/rest_pwd")
    @PreAuthorize("hasAuthority('sys:user:repass')")
    public Result restPwd(@RequestBody Long userId){
        SysUser sysUser = sysUserService.getById(userId);
        sysUser.setPassword(passwordEncoder.encode(Const.DEFALT_PASSWORD));
        sysUser.setUpdated(LocalDateTime.now());

        sysUserService.updateById(sysUser);
        return Result.succ("");
    }

    @PostMapping("/update_pwd")
    public Result udtPwd(@Validated @RequestBody PwdDto pwdDto, Principal principal){
        // Get current user
        String username = principal.getName();
        SysUser sysUser = sysUserService.getByUsername(username);

        // Validate the old password
        String currentPassword = pwdDto.getCurrentPassword();
        String oldPassword = sysUser.getPassword();
        boolean ifMatches =
            passwordEncoder.matches(passwordEncoder.encode(currentPassword), oldPassword);
        if (!ifMatches){
            return Result.fail("The old password is incorrect");
        }

        // Set new password to database
        sysUser.setPassword(passwordEncoder.encode(pwdDto.getNewPassword()));
        sysUser.setUpdated(LocalDateTime.now());
        sysUserService.updateById(sysUser);

        return Result.succ("");

    }


}
