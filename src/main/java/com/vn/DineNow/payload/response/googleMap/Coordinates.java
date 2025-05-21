package com.vn.DineNow.payload.response.googleMap;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coordinates {
    double lat;
    double lng;
}
