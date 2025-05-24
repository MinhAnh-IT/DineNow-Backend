package com.vn.DineNow.services.common.googleMaps;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.googleMap.Coordinates;
import com.vn.DineNow.payload.response.googleMap.GeocodingResult;
import com.vn.DineNow.payload.response.googleMap.Location;
import com.vn.DineNow.payload.response.googleMap.GeocodingResponse;
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

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${DineNow.google.maps.url}")
    String geocodeUrl;

    final RestTemplate restTemplate;

    @Override
    public Coordinates getCoordinates(String address) throws CustomException {
        URI uri = UriComponentsBuilder.fromUriString(geocodeUrl)
                .queryParam("address", address)
                .build()
                .encode()
                .toUri();

        log.info("[Geocoding] Request: {}", uri);

        GeocodingResponse resp;
        try {
            resp = restTemplate.getForObject(uri, GeocodingResponse.class);
        } catch (RestClientException ex) {
            log.error("[Geocoding] API call failed: {}", ex.getMessage(), ex);
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    "Geocoding API request failed: " + ex.getMessage());
        }

        Location loc = getLocation(address, resp);

        log.info("[Geocoding] Result for '{}': lat={}, lng={}", address, loc.getLat(), loc.getLng());
        return Coordinates.builder()
                .lat(loc.getLat())
                .lng(loc.getLng())
                .build();
    }

    private static Location getLocation(String address, GeocodingResponse resp) throws CustomException {
        if (resp == null || !"OK".equals(resp.getStatus()) || resp.getResults() == null || resp.getResults().isEmpty()) {
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    String.format("No valid coordinates found for address '%s'", address));
        }

        GeocodingResult result = resp.getResults().get(0);
        if (result == null || result.getGeometry() == null || result.getGeometry().getLocation() == null) {
            throw new CustomException(StatusCode.INVALID_ADDRESS,
                    String.format("Invalid geometry/location data for address '%s'", address));
        }

        return result.getGeometry().getLocation();
    }
}
