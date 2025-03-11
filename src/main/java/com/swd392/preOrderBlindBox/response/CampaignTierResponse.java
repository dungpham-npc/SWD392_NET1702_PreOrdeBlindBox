package com.swd392.preOrderBlindBox.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CampaignTierResponse {
    private Long id;
    private String tierName;
    private int minQuantity;
    private Integer maxQuantity;
    private int discountPercent;
    private boolean isActive;
}