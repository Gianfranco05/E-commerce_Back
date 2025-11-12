package com.petaByte.miApp.security.jwt;

import com.petaByte.miApp.service.impl.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailServiceImpl userDetailService;

    // Añadimos un logger para ver mejor los errores
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // --- SE HA ELIMINADO EL BLOQUE 'IF' QUE CAUSABA EL BUCLE DE LOGIN ---
        // Ahora el filtro intentará procesar el token en CADA petición.
        // La clase WebSecurityConfig ya se encarga de definir qué rutas son públicas.

        try {
            String jwt = parseJwt(request);

            // Si el token existe y es válido...
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Si tenemos un usuario y no está ya autenticado en el contexto de seguridad...
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailService.loadUserByUsername(username);

                    // Creamos el objeto de autenticación
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null, // No se necesitan credenciales (password) aquí
                                    userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecemos la autenticación en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Si algo falla (token expirado, malformado, etc.), lo registramos en la consola.
            logger.error("No se pudo establecer la autenticación del usuario: {}", e.getMessage());
        }

        // Continuamos con el resto de la cadena de filtros.
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Extrae solo el token
        }
        return null;
    }
}