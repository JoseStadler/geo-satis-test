package ec.com.jasr.geosatisws.core.security.filters;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpHeaders.VARY;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ec.com.jasr.geosatisws.core.application.AppSpringCtx;
import ec.com.jasr.geosatisws.core.security.util.SecurityConstants;
import ec.com.jasr.geosatisws.core.security.util.XSSRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        if (((HttpServletRequest) req).getRequestURI().contains("/api/")) {
            settingHeaders(response, request);
        }
        if (!HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            if (request.getContentType() != null && request.getContentType().length() >= 20 &&
                    request.getContentType().contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
                chain.doFilter(request, response);
            } else {
                XSSRequestWrapper wrappedRequest = new XSSRequestWrapper(request);
                chain.doFilter(request, response);
            }
        }
    }

    private void settingHeaders(HttpServletResponse response, HttpServletRequest request) {
        String origin = request.getHeader(ORIGIN);
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN,
                Arrays.asList(AppSpringCtx.getAppProp().getAllowedOriginsAuthorized())
                        .contains(origin) ? origin : "");
        response.setHeader(VARY, ORIGIN);
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, SecurityConstants.CORS_HEADERS.get(ACCESS_CONTROL_ALLOW_CREDENTIALS));
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, SecurityConstants.CORS_HEADERS.get(ACCESS_CONTROL_ALLOW_METHODS));
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, SecurityConstants.CORS_HEADERS.get(ACCESS_CONTROL_ALLOW_HEADERS));
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, SecurityConstants.CORS_HEADERS.get(ACCESS_CONTROL_EXPOSE_HEADERS));
    }

    @Override
    public void destroy() {
    }

}
