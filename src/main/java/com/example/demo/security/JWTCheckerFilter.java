package com.example.demo.security;

import com.example.demo.entities.User;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final UsersService usersService;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public JWTCheckerFilter(JWTTools jwtTools, UsersService usersService) {
        this.jwtTools = jwtTools;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // **************************** AUTENTICAZIONE *******************
        //1 leggo l'header authorization
        String authHeader = request.getHeader("Authorization");
        //2 se manca o non inizia con BEARER blocco la richiesta. (cioè il client non ha inviato il token).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Metti il token nell'header Authorization (Bearer ...)");
        }
        //3 estraggo il token togliendo bearer
        String accessToken = authHeader.substring(7);

        //4 verifico firma e scadenza
        jwtTools.verifyToken(accessToken);



        // **************************** AUTORIZZAZIONE *******************


        // 1 cerco utente nel db tramite id
        Long userId = jwtTools.extractIdFromToken(accessToken);
        //2 leggiamo id del token
        User authenticatedUser = this.usersService.findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                authenticatedUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
//se tutto ok lascio proseguire la request al controller.
        filterChain.doFilter(request, response);
    }
    //no filter
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return matcher.match("/auth/**", request.getServletPath());
    }
}