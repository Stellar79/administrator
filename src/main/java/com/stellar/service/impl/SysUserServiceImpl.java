package com.stellar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stellar.mapper.SysUserMapper;
import com.stellar.entity.SysMenu;
import com.stellar.entity.SysRole;
import com.stellar.entity.SysUser;
import com.stellar.service.ISysMenuService;
import com.stellar.service.ISysRoleService;
import com.stellar.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stellar.utils.RedisUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Stellar
 * @since 2022-07-17
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    ISysMenuService sysMenuService;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username",username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:"+sysUser.getUsername())){
            authority = (String) redisUtil.get("GrantedAuthority:"+sysUser.getUsername());
        }else {
        // Get roles of current user
        List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
            .inSql("id","SELECT role_id FROM sys_user_role WHERE user_id =" + userId));
        // Roles separated by "ROLE_"delimiters
        if (roles.size()>0){
            String roleCodes =
                roles.stream().map(r -> "ROLE_" + r.getUniqueCode()).collect(Collectors.joining(","));

            authority = roleCodes.concat(",");
        }

        // Get the navigation menu permission code of current user
        List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
        // Get menus by menu id
        if (menuIds.size()>0){
            List<SysMenu> menus = sysMenuService.listByIds(menuIds);
            String menuPermissions =
                menus.stream().map(m -> m.getPermissionCode()).collect(Collectors.joining(","));

            authority = authority.concat(menuPermissions);

        }

        log.info("User ID - {} ---permissions：{}",userId,authority);
        redisUtil.set("GrantedAuthority:"+sysUser.getUsername(),authority,60*60);
        }
        return authority;
    }

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:"+username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>().inSql("id",
            "SELECT user_id FROM sys_user_role WHERE role_id=" +roleId));
        sysUsers.forEach(u -> {this.clearUserAuthorityInfo(u.getUsername());}
        );
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers  = sysUserMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {this.clearUserAuthorityInfo(u.getUsername());}
        );

    }



}
