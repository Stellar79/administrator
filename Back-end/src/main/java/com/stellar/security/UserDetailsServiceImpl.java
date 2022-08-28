package com.stellar.security;

import com.stellar.entity.SysUser;
import com.stellar.service.ISysUserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("Username or password is incorrect.");
        }
        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(),
            getUserAuthority(sysUser.getId()));
    }

    /* Get user authorities(roles, menus) */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(userId); //Role_admin, Role_normal, sys:user:list, ...

        // Encapsulate the permission string into a GrantedAuthority list
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userAuthorityInfo);
    }
}
