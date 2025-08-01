package me.tiary.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.tiary.common.util.http.HttpRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AccessLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);

        final String remoteAddress = request.getRemoteAddr();
        final String method = request.getMethod();
        final String requestUri = HttpRequestUtils.getRequestUriWithQueryString(request);
        final String protocol = request.getProtocol();
        final int status = response.getStatus();

        log.info("{} - \"{} {} {}\" {}", remoteAddress, method, requestUri, protocol, status);
    }

}
