package com.ecobank.core.Enums;

import java.util.List;

public enum OfferStatus {
    CREATION,
    REVIEW,
    APPROVAL,
    PUBLISHING,
    MONITORING,
    UPDATE_OR_PAUSE,
    EXPIRY_AND_ARCHIVAL;

    public static List<OfferStatus> activeStatuses() {
        return List.of(PUBLISHING, MONITORING);
    }
}
