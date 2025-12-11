package com.blobstore.nox.auth_module.utils;

import com.blobstore.nox.auth_module.services.JWTService;
import com.blobstore.nox.common.exception_handling.CustomAppException;
import com.blobstore.nox.common.response_handling.ApiResponse;
import com.blobstore.nox.common.response_handling.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String headerContent = request.getHeader("Authorization");
            if (headerContent == null || headerContent.isEmpty()) {
                throw new CustomAppException("Authorization header is null", ResponseCode.HEADER_NOT_PRESENT);
            }
            String[] splitHeaderContent = headerContent.split(" ");
            if ((splitHeaderContent.length == 2) || (splitHeaderContent[0].equals("Bearer"))) {
                final String JwtHeaderToken = splitHeaderContent[1];
                boolean isTokenValid = jwtService.isAccessTokenValid(JwtHeaderToken);
                if (isTokenValid) {
                    doFilter(request, response, filterChain);
                }
            } else {
                throw new CustomAppException("No auth token received", ResponseCode.AUTH_TOKEN_NOT_VALID);
            }
        } catch (Exception ex) {
            handleFilterException(response, ex);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String[] allowedUrlsForJwtFilter = {
                "/auth/v1/register",
                "/auth/v1/login",
                "/auth/v1/refresh"
        };
        final String requestUrl = request.getRequestURI();
        for (String url : allowedUrlsForJwtFilter) {
            if (url.matches(requestUrl)) return true;
        }
        return false;
    }

    public void handleFilterException(HttpServletResponse response, Exception ex) throws IOException {
        ApiResponse apiResponse = new ApiResponse(false, ResponseCode.AUTH_TOKEN_NOT_VALID, ex.getMessage(), null);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
    }

}
