package com.bls.patronage.db.model;

public enum Amount {
    ONE(1), FIVE(5), TEN(10), FIFTEEN(15), TWENTY(20);
    private final int value;

    Amount(int value) {
        this.value = value;
    }

    public static Amount fromString(final String string) {
        return Amount.valueOf(string.toUpperCase());
    }

    public int getValue() {
        return value;
    }
}
