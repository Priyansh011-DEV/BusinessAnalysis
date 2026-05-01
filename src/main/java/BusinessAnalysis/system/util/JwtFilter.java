package BusinessAnalysis.system.util;

import BusinessAnalysis.system.model.User;
import BusinessAnalysis.system.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

// ✅ ADD THESE DEBUG LOGS
        System.out.println("📍 Path: " + path);
        System.out.println("🔑 Auth Header: " + authHeader);
        System.out.println("📋 All Headers: " + java.util.Collections.list(request.getHeaderNames()));

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ No Bearer token — rejecting");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;  // ✅ REJECT instead of passing through
        }

        String token = authHeader.substring(7);

        // ❌ Invalid token → reject
        if (!jwtUtil.isTokenValid(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {

            // ✅ FIX: Add at least one authority
            var authorities = List.of(
                    new SimpleGrantedAuthority("USER")
            );

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(user, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}