package org.example.schoolapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.schoolapp.config.CustomAccessDeniedHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private CustomAccessDeniedHandler accessDeniedHandler;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws IOException {
        accessDeniedHandler = new CustomAccessDeniedHandler();
        responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
    }

    @Test
    void handle_ShouldSetForbiddenStatus() throws IOException {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        accessDeniedHandler.handle(request, response, exception);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void handle_ShouldSetJsonContentType() throws IOException {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        accessDeniedHandler.handle(request, response, exception);

        verify(response).setContentType("application/json");
    }

    @Test
    void handle_ShouldWriteErrorJson() throws IOException {
        AccessDeniedException exception = new AccessDeniedException("Access denied");

        accessDeniedHandler.handle(request, response, exception);

        String expectedJson = "{\"error\": \"You do not have permission to access this resource.\"}";
        assertEquals(expectedJson, responseWriter.toString().trim());
    }

    @Test
    void handle_WhenIOExceptionOccurs_ShouldPropagateException() throws IOException {
        AccessDeniedException exception = new AccessDeniedException("Access denied");
        when(response.getWriter()).thenThrow(new IOException("Failed to get writer"));

        assertThrows(IOException.class, () -> {
            accessDeniedHandler.handle(request, response, exception);
        });
    }

    @Test
    void handle_WithDifferentExceptionMessage_ShouldStillReturnStandardResponse() throws IOException {
        AccessDeniedException exception = new AccessDeniedException("Custom message");

        accessDeniedHandler.handle(request, response, exception);

        String expectedJson = "{\"error\": \"You do not have permission to access this resource.\"}";
        assertEquals(expectedJson, responseWriter.toString().trim());
    }
}
