package com.stellar.security;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.stellar.common.exception.CaptchaException;
import com.stellar.common.lang.Const;
import com.stellar.utils.RedisUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/*Image authentication code calibration filter before login filter */
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {
    private final String loginUrl = "/login";
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandler loginFailureHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException, IOException {
        String url = request.getRequestURI();
        // 对登录请求进行拦截
        if (loginUrl.equals(url)&&request.getMethod().equals("POST")){
            log.info("Access to the login link, is calibration verification code--" + url);
            try {
                validate(request);
            } catch (CaptchaException e) {
                log.info(e.getMessage());

            // Hand over to the login failure handler for processing
            loginFailureHandler.onAuthenticationFailure(request,response,e);
            }
            
        }
        filterChain.doFilter(request,response);

    }

    // Validate captcha
    private void validate(HttpServletRequest request) throws CaptchaException {
        String captcha = request.getParameter("captcha");
        String key = request.getParameter("token");
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(key)){
            throw new CaptchaException("The captcha code cannot be empty.");
        }
        if (!captcha.equals(redisUtil.hget(Const.captcha_KEY,key))){
            throw new CaptchaException("The verification code is incorrect.");
        }
        // key and value is just one time
        redisUtil.hdel(Const.captcha_KEY, key);
    }
}
