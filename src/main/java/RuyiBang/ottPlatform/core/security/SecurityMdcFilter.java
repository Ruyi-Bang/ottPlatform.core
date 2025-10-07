package RuyiBang.ottPlatform.core.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityMdcFilter extends OncePerRequestFilter {

    public static final String MDC_USERID = "userId";
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            MDC.put(MDC_USERID, auth.getName());
        }
        try { chain.doFilter(req, res); }
        finally { MDC.remove(MDC_USERID); }
    }
}
