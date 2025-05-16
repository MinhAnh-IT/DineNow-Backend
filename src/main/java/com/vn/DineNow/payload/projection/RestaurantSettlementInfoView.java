package com.vn.DineNow.payload.projection;
public interface RestaurantSettlementInfoView {
    Long getRestaurantId();
    String getRestaurantName();
    String getAddress();
    String getPhoneNumber();
    String getImageUrl();
}
