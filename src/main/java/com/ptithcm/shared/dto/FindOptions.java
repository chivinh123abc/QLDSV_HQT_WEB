package com.ptithcm.shared.dto;

import java.util.Map;

public class FindOptions {
    private Map<String, String> order;

    public FindOptions() {
    }

    public FindOptions(Map<String, String> order) {
        this.order = order;
    }

    public Map<String, String> getOrder() {
        return order;
    }

    public void setOrder(Map<String, String> order) {
        this.order = order;
    }
}
