package n1h5.models.config;

import java.io.IOException;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import n1h5.models.service.JwtService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final StringRedisTemplate redisTemplate; 
    
    // Bỏ qua filter này đối với các request Auth và đường dẫn /error
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/") || path.equals("/error");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        // 1. Kiểm tra sự tồn tại của Header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất Token (bỏ 7 ký tự "Bearer ")
        jwt = authHeader.substring(7);

        // 3. KIỂM TRA BLACKLIST TRONG REDIS
        Boolean isBlacklisted = redisTemplate.hasKey("BL:" + jwt);
        if (Boolean.TRUE.equals(isBlacklisted)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"message\": \"Token đã đăng xuất, vui lòng đăng nhập lại\"}");
            return;
        }

        username = jwtService.extractUsername(jwt);

        // 4. Nếu lấy được username và người dùng CHƯA được xác thực trong Context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            // 5. Kiểm tra tính hợp lệ của Token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Đánh dấu người dùng ĐÃ ĐĂNG NHẬP thành công vào hệ thống
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Chuyển request sang Filter tiếp theo trong chuỗi
        filterChain.doFilter(request, response);
    }
}