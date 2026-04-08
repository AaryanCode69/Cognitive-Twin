package com.example.cognitivetwin.common.util;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidateSortAttribute {

    private static final Set<String> allowedSortFields = Set.of("createdAt", "totalAmount", "orderStatus");


    public boolean validateSortAttribute(String attribute){
        int index = attribute.indexOf(":");
        attribute = attribute.substring(0, index != -1 ? index : attribute.length());
        return !allowedSortFields.contains(attribute);
    }
}
