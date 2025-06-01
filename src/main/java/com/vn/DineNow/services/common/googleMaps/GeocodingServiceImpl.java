package com.vn.DineNow.services.common.googleMaps;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.googleMap.Coordinates;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeocodingServiceImpl implements GeocodingService {

    @Value("${DineNow.key.nominatim.url}")
    String urlNominatim;

    @Override
    public Coordinates getCoordinates(String address) throws CustomException {
        String urlString;
        try {
            urlString = urlNominatim
                    + URLEncoder.encode(address, StandardCharsets.UTF_8)
                    + "&format=json";
            log.info("[Geocoding] Fetching coordinates for address: {}", urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            conn.setConnectTimeout(5000); // 5s timeout
            conn.setReadTimeout(5000);    // 5s timeout
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                String jsonResult = content.toString();
                // Parse đơn giản
                double[] coordinates = parseCoordinatesFromJson(jsonResult);

                if (coordinates != null) {
                    log.info("[Geocoding] Result for '{}': lat={}, lng={}", address, coordinates[0], coordinates[1]);
                    return Coordinates.builder()
                            .lat(coordinates[0])
                            .lng(coordinates[1])
                            .build();
                } else {
                    log.warn("No valid coordinates found for address '{}'", address);
                    throw new CustomException(StatusCode.INVALID_ADDRESS, "No valid coordinates found for address '" + address + "'");
                }
            } else {
                log.error("Error: HTTP code {}", responseCode);
                throw new CustomException(StatusCode.INVALID_ADDRESS, "Geocoding API HTTP error: " + responseCode);
            }
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            log.error("Error while fetching coordinates for address '{}': {}", address, e.getMessage());
            throw new CustomException(StatusCode.INVALID_ADDRESS, "Error while fetching coordinates: " + e.getMessage());
        }
    }

    private double[] parseCoordinatesFromJson(String json) {
        try {
            if (json.startsWith("[") && json.length() > 10) {
                int latIdx = json.indexOf("\"lat\":\"");
                int lonIdx = json.indexOf("\"lon\":\"");
                if (latIdx > 0 && lonIdx > 0) {
                    String latStr = json.substring(latIdx + 7, json.indexOf("\"", latIdx + 7));
                    String lonStr = json.substring(lonIdx + 7, json.indexOf("\"", lonIdx + 7));
                    double lat = Double.parseDouble(latStr);
                    double lon = Double.parseDouble(lonStr);
                    return new double[]{lat, lon};
                }
            }
        } catch (Exception e) {
            log.error("Error parsing coordinates from JSON: {}", e.getMessage());
        }
        return null;
    }
}
