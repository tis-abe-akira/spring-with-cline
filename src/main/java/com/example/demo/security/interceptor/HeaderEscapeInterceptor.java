package com.example.demo.security.interceptor;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * HTTPヘッダーのエスケープ処理を行うインターセプター
 */
@Component
public class HeaderEscapeInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HeaderEscapeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpServletRequest wrappedRequest = new HeaderEscapeRequestWrapper(request);
        request.setAttribute("escapedRequest", wrappedRequest);
        return true;
    }

    private static class HeaderEscapeRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String[]> escapedHeaders;
        private final HttpServletRequest originalRequest;

        public HeaderEscapeRequestWrapper(HttpServletRequest request) {
            super(request);
            this.originalRequest = request;
            this.escapedHeaders = new HashMap<>();
            escapeHeaders(request);
        }

        private void escapeHeaders(HttpServletRequest request) {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                Enumeration<String> headerValuesEnum = request.getHeaders(headerName);
                List<String> headerValuesList = Collections.list(headerValuesEnum);
                String[] headerValues = headerValuesList.toArray(new String[0]);

                // ヘッダーの値をエスケープ
                String[] escapedValues = Arrays.stream(headerValues)
                        .map(StringEscapeUtils::escapeHtml4)
                        .toArray(String[]::new);

                escapedHeaders.put(headerName, escapedValues);
                
                // デバッグログ
                logger.debug("Header: {}, Original: {}, Escaped: {}", 
                    headerName, 
                    Arrays.toString(headerValues), 
                    Arrays.toString(escapedValues));
            }
        }

        @Override
        public String getHeader(String name) {
            // デバッグログ
            logger.debug("getHeader called for: {}", name);
            
            // ヘッダー名の大文字小文字を区別せずに検索
            String[] values = null;
            for (Map.Entry<String, String[]> entry : escapedHeaders.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    values = entry.getValue();
                    break;
                }
            }
            
            if (values != null && values.length > 0) {
                logger.debug("Returning escaped header: {}", values[0]);
                return values[0];
            }
            
            // エスケープされたヘッダーがない場合は、元のヘッダーをエスケープして返す
            String originalValue = originalRequest.getHeader(name);
            if (originalValue != null) {
                String escapedValue = StringEscapeUtils.escapeHtml4(originalValue);
                logger.debug("Original header: {}, Escaped on-the-fly: {}", originalValue, escapedValue);
                return escapedValue;
            }
            
            logger.debug("Header not found: {}", name);
            return null;
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            // ヘッダー名の大文字小文字を区別せずに検索
            String[] values = null;
            for (Map.Entry<String, String[]> entry : escapedHeaders.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    values = entry.getValue();
                    break;
                }
            }
            
            if (values != null) {
                return Collections.enumeration(Arrays.asList(values));
            }
            
            // エスケープされたヘッダーがない場合は、元のヘッダーをエスケープして返す
            Enumeration<String> originalValues = originalRequest.getHeaders(name);
            if (originalValues != null && originalValues.hasMoreElements()) {
                List<String> escapedValues = new ArrayList<>();
                while (originalValues.hasMoreElements()) {
                    String originalValue = originalValues.nextElement();
                    escapedValues.add(StringEscapeUtils.escapeHtml4(originalValue));
                }
                return Collections.enumeration(escapedValues);
            }
            
            return Collections.emptyEnumeration();
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Set<String> headerNames = new HashSet<>();
            headerNames.addAll(escapedHeaders.keySet());
            
            Enumeration<String> originalHeaderNames = originalRequest.getHeaderNames();
            while (originalHeaderNames.hasMoreElements()) {
                headerNames.add(originalHeaderNames.nextElement());
            }
            
            return Collections.enumeration(headerNames);
        }
    }
}
