package com.august.refactoring._16_temporary_field._36_introduce_special_case;

public class UnknownCustomer extends Customer {

    public UnknownCustomer() {
        super("unknown", null, null);
    }

    @Override
    public boolean isUnknown() {
        return true;
    }
}
