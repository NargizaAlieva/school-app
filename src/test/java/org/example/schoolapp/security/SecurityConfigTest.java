package org.example.schoolapp.security;

import org.example.schoolapp.config.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    void securityFilterChain_ShouldConfigureEndpointAuthorization() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf(any())).thenReturn(http);
        when(http.cors(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.oauth2Login(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);

        ArgumentCaptor<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> captor =
                ArgumentCaptor.forClass(AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry.class);

        securityConfig.securityFilterChain(http);
    }

    @Test
    void securityFilterChain_ShouldConfigureCors() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf(any())).thenReturn(http);
        when(http.cors(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authenticationProvider(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.logout(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.oauth2Login(any())).thenReturn(http);
        when(http.exceptionHandling(any())).thenReturn(http);

        securityConfig.securityFilterChain(http);
    }
}
