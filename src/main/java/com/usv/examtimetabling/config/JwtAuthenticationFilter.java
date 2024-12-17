package com.usv.examtimetabling.config;


import static org.apache.commons.lang3.StringUtils.startsWith;

import com.usv.examtimetabling.security.services.JwtService;
import com.usv.examtimetabling.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public final JwtService jwt;

    private final UserService usersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

//        if (StringUtils.isEmpty(authorizationHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authorizationHeader, "Bearer ")) {
        if (!StringUtils.hasText(authorizationHeader) || !startsWith(authorizationHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authorizationHeader.substring(7);
        userEmail = jwt.extractUsername(jwtToken);

        if (!userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = usersService.userDetailsService().loadUserByUsername(userEmail);

            if (jwt.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request, response);
    }
}
