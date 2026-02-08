package com.hrms.hrms_backend.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;


    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {


        String header = request.getHeader("Authorization");


        logger.info("Processing request to: {}", request.getRequestURI());
        logger.info("Authorization header: {}", header);


        if (header != null && header.startsWith("Bearer ")) {


            String token = header.substring(7);
            logger.info("Extracted token: {}", token);


            try {
                if (jwtUtil.isTokenValid(token)) {


                    String email = jwtUtil.extractEmail(token);
                    List<String> roles = jwtUtil.extractRoles(token);


                    logger.info("Email from token: {}", email);
                    logger.info("Roles from token: {}", roles);


                    var authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();


                    logger.info("Created authorities: {}", authorities);


                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    authorities
                            );


                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);


                    logger.info("Authentication set successfully");
                }
            } catch (Exception e) {
                logger.error("JWT Authentication failed", e);
            }
        }


        filterChain.doFilter(request, response);
    }
}
