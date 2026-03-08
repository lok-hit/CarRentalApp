package car_rental_app.adapter.in.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CorrelationIdFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String SPAN_ID_HEADER = "X-Span-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest http = (HttpServletRequest) request;
            HttpServletResponse httpResp = (HttpServletResponse) response;

            String traceId = http.getHeader(TRACE_ID_HEADER);
            String spanId = http.getHeader(SPAN_ID_HEADER);

            if (traceId == null || traceId.isBlank()) {
                traceId = "unknown";
            }
            if (spanId == null || spanId.isBlank()) {
                spanId = "unknown";
            }

            MDC.put("traceId", traceId);
            MDC.put("spanId", spanId);

            // opcjonalnie: zwróć nagłówki do klienta
            httpResp.setHeader(TRACE_ID_HEADER, traceId);
            httpResp.setHeader(SPAN_ID_HEADER, spanId);

            chain.doFilter(request, response);

        } finally {
            MDC.remove("traceId");
            MDC.remove("spanId");
        }
    }
}
