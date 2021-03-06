package com.sech.framework.system.auth.config;

import com.sech.framework.core.configuration.SechUrlsConfiguration;
import com.sech.framework.system.auth.component.ajax.AjaxSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author sech.io 认证服务器开放接口配置
 */
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER - 2)
@Configuration
// @EnableResourceServer
@EnableWebSecurity
public class SechResourceServerConfiguration extends WebSecurityConfigurerAdapter {// ResourceServerConfigurerAdapter {

    @Autowired
    private SechUrlsConfiguration sechUrlsConfiguration;

    @Autowired
    private AjaxSecurityConfigurer ajaxSecurityConfigurer;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .formLogin()
                // 可以通过授权登录进行访问
                .loginPage("/auth/login").loginProcessingUrl("/auth/signin").and()
                .authorizeRequests();

        for (String url : sechUrlsConfiguration.getCollects()) {
            registry.antMatchers(url).permitAll();
        }

        registry.anyRequest().authenticated().and().csrf().disable();
        http.apply(ajaxSecurityConfigurer);
    }

}
