package com.sitool.servicedesk.servicenow.client;

import com.sitool.servicedesk.servicenow.dto.request.ServiceNowIncidentRequest;
import com.sitool.servicedesk.servicenow.dto.response.ServiceNowIncidentResponse;
import com.sitool.servicedesk.servicenow.dto.response.ServiceNowIncidentResponseWrapper;
import com.sitool.servicedesk.servicenow.exceptions.ServiceNowIntegrationException;
import com.sitool.servicedesk.servicenow.settings.entity.ServiceNowSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;

/**
 * Low-level HTTP client responsible for sending incident creation requests
 * to the ServiceNow Scripted REST API. Knows only how to make the call -
 * decisions about when to call it, and what to do with the result, belong
 * to {@code ServiceNowIntegrationService}.
 */
@Slf4j
@Component
public class ServiceNowClient {

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(15);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);

    private final RestClient restClient;

    public ServiceNowClient() {
        this.restClient = RestClient.builder()
                .requestFactory(buildRequestFactory())
                .build();
    }

    /**
     * Sends an incident creation request to ServiceNow.
     *
     * @param settings the current integration settings (endpoint, username,
     *                 decrypted password), fetched fresh by the caller since
     *                 credentials can change at runtime
     * @param request  the incident data to send
     * @return the parsed response from ServiceNow
     * @throws ServiceNowIntegrationException if the call fails (network
     *                                         error, timeout, or a non-2xx response)
     */
    public ServiceNowIncidentResponse createIncident(ServiceNowSettings settings,
                                                     ServiceNowIncidentRequest request) {
        try {
            ServiceNowIncidentResponseWrapper wrapper = restClient.post()
                    .uri(settings.getEndpoint())
                    .header(HttpHeaders.AUTHORIZATION, buildBasicAuthHeader(settings))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(ServiceNowIncidentResponseWrapper.class);

            if (wrapper == null || wrapper.result() == null) {
                throw new ServiceNowIntegrationException("ServiceNow returned an empty response body");
            }

            return wrapper.result();

        } catch (RestClientResponseException ex) {
            // ServiceNow responded, but with a 4xx/5xx status
            log.error("ServiceNow returned an error status {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ServiceNowIntegrationException(
                    "ServiceNow request failed with status " + ex.getStatusCode(), ex);

        } catch (Exception ex) {
            // Network error, timeout, malformed response, etc.
            log.error("Failed to call ServiceNow", ex);
            throw new ServiceNowIntegrationException("Failed to call ServiceNow: " + ex.getMessage(), ex);
        }
    }

    private JdkClientHttpRequestFactory buildRequestFactory() {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(READ_TIMEOUT);

        return requestFactory;
    }

    private String buildBasicAuthHeader(ServiceNowSettings settings) {
        String credentials = settings.getUsername() + ":" + settings.getPassword();
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }
}