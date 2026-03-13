package com.ecobank.core.Enums;

public enum RedemptionStatus
{
    INITIATED,     // Customer clicked "Activate"
    ACTIVATED,     // Code issued / CLO enabled
    USED,          // Customer used the offer at merchant
    CONFIRMED,     // Partner confirmed usage (webhook)
    FAILED,        // Partner rejected / technical failure
    EXPIRED,       // Offer expired before use
    CANCELLED      // Customer or system cancelled
}
