package cn.hizhangyang.securitydemo.config;

import cn.hizhangyang.securitydemo.service.CustomerDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomerDetailService customerDetailService;

    @Autowired
    public SecurityConfig(CustomerDetailService customerDetailService){
        this.customerDetailService=customerDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/*/floor1/**").hasAnyRole("USER")
                .antMatchers("/*/floor2/**").hasAnyRole("ADMIN")
                .and()
                .httpBasic();
    }
}
