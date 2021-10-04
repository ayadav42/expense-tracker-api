package com.pairlearning.expensetracker.filters;

import com.pairlearning.expensetracker.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader"+authHeader);
        if (authHeader != null) {
            String[] authHeaderArr = authHeader.split("Bearer ");
            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
                String token = authHeaderArr[1];
                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
                            .parseClaimsJws(token).getBody();
                    request.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
                } catch (Exception e) {
                    System.out.println(e);
                    response.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/expired token");
                    return;
                }
            } else {
                response.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token].");
                return;
            }
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token missing.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        Collection<String> excludeUrlPatterns = new ArrayList<>();
        excludeUrlPatterns.add(Constants.API_BASE + "/users/login");
        excludeUrlPatterns.add(Constants.API_BASE + "/users/register");
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//
//        String authHeader = httpRequest.getHeader("Authorization");
//        if (authHeader != null) {
//            String[] authHeaderArr = authHeader.split("Bearer ");
//            if (authHeaderArr.length > 1 && authHeaderArr[1] != null) {
//                String token = authHeaderArr[1];
//                try {
//                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY)
//                            .parseClaimsJws(token).getBody();
//                    httpRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
//                } catch (Exception e) {
//                    System.out.println(e);
//                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Invalid/expired token");
//                    return;
//                }
//            } else {
//                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be Bearer [token].");
//            }
//        }else {
//            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization token missing.");
//        }
//
//        chain.doFilter(request, response);
//    }
}
