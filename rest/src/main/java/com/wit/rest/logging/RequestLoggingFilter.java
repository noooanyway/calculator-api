package com.wit.rest.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro responsável por adicionar um identificador único (requestId) a cada requisição HTTP.
 * O requestId é propagado nos logs via MDC e adicionado no cabeçalho da resposta HTTP.
 */
@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest httpRequest) {
            // Verifica se a requisição já possui um requestId (ex.: propagado por um gateway)
            String requestId = httpRequest.getHeader("X-Request-ID");
            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString(); // Gera um novo ID caso não exista
            }

            MDC.put(REQUEST_ID, requestId); // Adiciona no MDC para logging

            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("X-Request-ID", requestId); // Adiciona no cabeçalho da resposta

            logger.info("Iniciando requisição com requestId={}", requestId); // Loga o início da requisição
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Remove o requestId do MDC após a execução da requisição
        }
    }
}
