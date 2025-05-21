package com.vn.DineNow.services.common.googleMaps;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.googleMap.Coordinates;

public interface GeocodingService {
    Coordinates getCoordinates(String address) throws CustomException;
}
