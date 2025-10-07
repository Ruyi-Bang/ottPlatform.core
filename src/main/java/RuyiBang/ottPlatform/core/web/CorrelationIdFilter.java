package RuyiBang.ottPlatform.core.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String HDR_REQUEST_ID = "X-Request-Id";
    public static final String MDC_UID = "uuid";

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String uuid = StringUtils.hasText(req.getHeader(HDR_REQUEST_ID))
                ? req.getHeader(HDR_REQUEST_ID)
                : UUID.randomUUID().toString();

        MDC.put(MDC_UID, uuid);
        res.setHeader(HDR_REQUEST_ID, uuid);

        try {
            chain.doFilter(req, res);
        } finally {
            MDC.remove(MDC_UID);
        }
    }
}
