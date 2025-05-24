package com.vn.DineNow.payload.response.googleMap;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeocodingResponse {
    String status;
    List<GeocodingResult> results;
}
