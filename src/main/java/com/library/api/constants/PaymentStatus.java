package com.library.api.constants;

public enum PaymentStatus {

    PENDING(1),
    PAID(2);

    private int code;

    private PaymentStatus(int code){
        this.code = code;
    }

    public static PaymentStatus valueOf(int code){
        for(PaymentStatus value : PaymentStatus.values()){
            if(code == value.getCode()){
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus code");
    }

    public int getCode() {
        return code;
    }
}