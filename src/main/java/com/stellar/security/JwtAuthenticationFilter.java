package com.stellar.security;

import cn.hutool.core.util.StrUtil;
import com.stellar.entity.SysUser;
import com.stellar.service.ISysUserService;
import com.stellar.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.jar.JarException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/** 解析jwt*/

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    ISysUserService userService;
    
    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        /*When accessing anonymously, skip the interception*/
        String jwt = request.getHeader(jwtUtils.getHeader());
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }

        /*Parse jwt*/
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null){
            throw new JarException("exception jwt");
        }
        if(jwtUtils.isTokenExpired(claim)){
            throw new JarException("jwt has expired.");
        }
        String username = claim.getSubject();

        SysUser sysUser = userService.getByUsername(username);

        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(username,null,userDetailsService.getUserAuthority(
                sysUser.getId()));
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request,response);

    }
}
