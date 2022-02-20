package com.august.refactoring._11_primitive_obsession._30_replace_primitive_with_object;

import java.util.List;

public class Priority {

    private String value;
    private List<String> legalValues = List.of("low", "normal", "high", "rush");

    public Priority(String value) {
        if (legalValues.contains(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("illegal value for priority" + value);
        }
    }

    @Override
    public String toString() {
        return this.value;
    }

    public boolean higherThan(Priority other) {
        return this.index() > other.index();
    }

    private int index() {
        return this.legalValues.indexOf(this.value);
    }
}
