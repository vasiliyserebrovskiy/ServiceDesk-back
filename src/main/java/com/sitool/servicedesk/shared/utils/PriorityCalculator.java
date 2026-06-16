package com.sitool.servicedesk.shared.utils;

import com.sitool.servicedesk.shared.enums.Impact;
import com.sitool.servicedesk.shared.enums.Priority;
import com.sitool.servicedesk.shared.enums.Urgency;

public class PriorityCalculator {

    private PriorityCalculator() {}

    public static Priority calculate(Impact impact, Urgency urgency) {
        if (impact == Impact.CRITICAL && urgency == Urgency.CRITICAL) return Priority.CRITICAL;
        if (impact == Impact.CRITICAL && urgency == Urgency.HIGH) return Priority.CRITICAL;
        if (impact == Impact.CRITICAL && urgency == Urgency.MEDIUM) return Priority.HIGH;
        if (impact == Impact.CRITICAL && urgency == Urgency.LOW) return Priority.MEDIUM;

        if (impact == Impact.HIGH && urgency == Urgency.CRITICAL) return Priority.CRITICAL;
        if (impact == Impact.HIGH && urgency == Urgency.HIGH) return Priority.HIGH;
        if (impact == Impact.HIGH && urgency == Urgency.MEDIUM) return Priority.HIGH;
        if (impact == Impact.HIGH && urgency == Urgency.LOW) return Priority.MEDIUM;

        if (impact == Impact.MEDIUM && urgency == Urgency.CRITICAL) return Priority.HIGH;
        if (impact == Impact.MEDIUM && urgency == Urgency.HIGH) return Priority.HIGH;
        if (impact == Impact.MEDIUM && urgency == Urgency.MEDIUM) return Priority.MEDIUM;
        if (impact == Impact.MEDIUM && urgency == Urgency.LOW) return Priority.LOW;

        if (impact == Impact.LOW && urgency == Urgency.CRITICAL) return Priority.MEDIUM;
        if (impact == Impact.LOW && urgency == Urgency.HIGH) return Priority.MEDIUM;
        if (impact == Impact.LOW && urgency == Urgency.MEDIUM) return Priority.LOW;

        return Priority.LOW; // LOW + LOW
    }
}