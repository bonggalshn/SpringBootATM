package com.bank.client.interfaceClient;

public interface Payment {
    String payInquiry(String accountNumber, String pinNumber);
    String pay(String message);
}
