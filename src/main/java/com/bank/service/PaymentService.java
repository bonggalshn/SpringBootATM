package com.bank.service;

import com.bank.Util.ISOUtil;
import com.bank.entity.Customer;
import com.bank.service.buildISO.paymentISO.PaymentISO;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private AccountService accountService;
    private ISOUtil isoUtil = new ISOUtil();
    private PaymentISO paymentISO = new PaymentISO();

    public PaymentService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String processInternetInquiry(String message) {

        System.out.println("processInternetInquiry:" + message);
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        String pinNumber = isoMessage.getString(52);
        String internetCompanyNumber = isoMessage.getString(102);
        int amount = Integer.parseInt(isoMessage.getString(4));

        boolean status = false;
        Customer company = new Customer();
        Customer customer;
        if (accountService.checkAccount(accountNumber, pinNumber)) {
            company = accountService.findByAccountNumber(internetCompanyNumber);
            customer = accountService.findByAccountNumber(accountNumber);
//            if (company!=null){
//                customer.setBalance(customer.getBalance()-amount);
//                company.setBalance(company.getBalance()+amount);
//
//                accountService.update(customer);
//                accountService.update(company);
//            }
        }

        String response = paymentISO.internetPaymentInquiryResponse(accountNumber, pinNumber, amount, company.getAccountName());
        System.out.println("Payment service: " + response);
        return response;
    }

    public String processInternet(String message) {
        System.out.println("processInternet" + message);
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        String internetCompanyNumber = isoMessage.getString(102);
        int amount = Integer.parseInt(isoMessage.getString(4));

        Customer company = accountService.findByAccountNumber(internetCompanyNumber);
        Customer customer = accountService.findByAccountNumber(accountNumber);

        boolean status = false;
        try {
            if (company != null) {
                customer.setBalance(customer.getBalance() - amount);
                company.setBalance(company.getBalance() + amount);

                accountService.update(customer);
                accountService.update(company);
                status=true;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            status = false;
        }


        String response = paymentISO.internetPaymentResponse(accountNumber, amount, company.getAccountName(),status);
        System.out.println("Payment service: " + response);
        return response;
    }
}
