package com.vn.DineNow.payload.response.googleMap;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeocodingResult {
    Geometry geometry;
}
