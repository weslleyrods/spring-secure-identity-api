package com.weslley.ssi_api.infra.security;

import com.weslley.ssi_api.exception.InvalidTokenException;
import com.weslley.ssi_api.exception.UserNotFoundException;
import com.weslley.ssi_api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token == null){
            filterChain.doFilter(request, response);
            return;
        }
        try{
            var login = tokenService.validateToken(token);
            if (login == null) throw new InvalidTokenException("Invalid token");

            UserDetails user = userRepository.findByEmail(login);
            if (user == null) throw new UserNotFoundException("Authenticated user not found");

            var authentication = new UsernamePasswordAuthenticationToken(user, login, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (InvalidTokenException e) {
            responseError(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (UserNotFoundException e) {
            responseError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    private void responseError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"" + message + "\"}");
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

}
