package com.vn.DineNow.services.common.googleMaps;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.googleMap.Coordinates;
import com.vn.DineNow.payload.response.googleMap.GeocodingResponse;
import com.vn.DineNow.payload.response.googleMap.GeocodingResult;
import com.vn.DineNow.payload.response.googleMap.Location;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${DineNow.google.maps.url}")
    String geocodeUrl;

    final RestTemplate restTemplate;

    @Value("${DineNow.google.maps.api-key}")
    String apiKey;

    @Override
    public Coordinates getCoordinates(String address) throws CustomException {
        return getCoordinatesInternal(address, false);
    }

    private Coordinates getCoordinatesInternal(String address, boolean fallbackUsed) throws CustomException {
        URI uri = buildUri(address);
        log.info("[Geocoding] Request: {}", uri);

        GeocodingResponse resp = null;
        try {
            resp = callApi(uri);
        } catch (RestClientException ex) {
            log.error("[Geocoding] API call failed: {}", ex.getMessage(), ex);
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    "Geocoding API request failed: " + ex.getMessage());
        }

        if (resp == null) {
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    String.format("No response from Geocoding API for address '%s'", address));
        }

        String status = resp.getStatus();
        log.info("[Geocoding] API status='{}' for address='{}'", status, address);

        if ("ZERO_RESULTS".equals(status)) {
            if (!fallbackUsed) {
                String simple = getSimpleAddress(address);
                log.info("[Geocoding] ZERO_RESULTS, retrying with fallback address: {}", simple);
                return getCoordinatesInternal(simple, true);
            } else {
                throw new CustomException(StatusCode.INVALID_ADDRESS,
                        String.format("Location not found for the given address '%s' after fallback", address));
            }
        }
        if ("OVER_QUERY_LIMIT".equals(status)) {
            log.error("[Geocoding] Over query limit!");
            throw new CustomException(StatusCode.INVALID_ADDRESS, "Google Geocoding API: Over query limit.");
        }
        if ("REQUEST_DENIED".equals(status)) {
            log.error("[Geocoding] Request denied! API Key/billing/config error.");
            throw new CustomException(StatusCode.INVALID_ADDRESS, "Google Geocoding API: Request denied.");
        }
        Location loc = getLocation(address, status, resp);
        log.info("[Geocoding] Result for '{}': lat={}, lng={}", address, loc.getLat(), loc.getLng());

        return Coordinates.builder()
                .lat(loc.getLat())
                .lng(loc.getLng())
                .build();
    }

    private static Location getLocation(String address, String status, GeocodingResponse resp) throws CustomException {
        if (!"OK".equals(status) || resp.getResults() == null || resp.getResults().isEmpty()) {
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    String.format("Location not found for the given address '%s'", address));
        }

        GeocodingResult result = resp.getResults().get(0);
        if (result.getGeometry() == null || result.getGeometry().getLocation() == null) {
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    String.format("Invalid geometry/location data for address '%s'", address));
        }

        return result.getGeometry().getLocation();
    }

    private URI buildUri(String address) {
        return UriComponentsBuilder.fromUriString(geocodeUrl)
                .queryParam("address", address)
                .queryParam("key", apiKey)
                .build()
                .encode()
                .toUri();
    }

    private GeocodingResponse callApi(URI uri) {
        return restTemplate.getForObject(uri, GeocodingResponse.class);
    }

    private String getSimpleAddress(String address) {
        String[] parts = address.split(",");
        if (parts.length <= 1) return address;
        StringBuilder simple = new StringBuilder();
        for (int i = 1; i < parts.length; i++) {
            simple.append(parts[i].trim());
            if (i < parts.length - 1) simple.append(", ");
        }
        if (!simple.isEmpty()) simple.append(", ");
        simple.append("Ho Chi Minh City, Vietnam");
        return simple.toString();
    }
}
