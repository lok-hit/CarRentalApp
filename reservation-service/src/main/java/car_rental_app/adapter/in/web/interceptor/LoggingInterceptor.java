package car_rental_app.adapter.in.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String traceId = extractOrGenerateTraceId(request);
        MDC.put("traceId", traceId);

        request.setAttribute("startTime", System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");

        // Możesz dodać log.info tutaj, ale zwykle logujemy w controllerach lub AOP
        MDC.clear();
    }

    private String extractOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader("X-Trace-Id");
        return (traceId == null || traceId.isBlank())
                ? UUID.randomUUID().toString()
                : traceId;
    }
}
