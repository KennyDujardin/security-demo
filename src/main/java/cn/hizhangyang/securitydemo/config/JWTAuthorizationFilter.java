package cn.hizhangyang.securitydemo.config;

import cn.hizhangyang.securitydemo.model.AppUser;
import cn.hizhangyang.securitydemo.service.CustomerDetailService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.hizhangyang.securitydemo.config.SecurityConstant.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private CustomerDetailService customerDetailService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomerDetailService customerDetailService) {
        super(authenticationManager);
        this.customerDetailService = customerDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);
        if (null == header || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthenticationToken(request);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (null == token) return null;
        String username = Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();
        UserDetails userDetails = customerDetailService.loadUserByUsername(username);
        AppUser appUser = customerDetailService.loadAppUserByUsername(username);
        return username != null ? new UsernamePasswordAuthenticationToken(appUser, null, userDetails.getAuthorities()) : null;
    }
}

