package me.tiary.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(request, response);

        log(request, response);
    }

    private void log(final HttpServletRequest request, final HttpServletResponse response) {
        final String queryString = (request.getQueryString() == null) ? ("") : ('?' + request.getQueryString());

        log.info(
                "{} - \"{} {} {}\" {}",
                request.getRemoteAddr(),
                request.getMethod(),
                request.getRequestURI() + queryString,
                request.getProtocol(),
                response.getStatus()
        );
    }
}