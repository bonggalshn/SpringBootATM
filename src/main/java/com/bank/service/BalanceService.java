package com.bank.service;

import com.bank.Util.ISOUtil;
import com.bank.entity.Customer;
import com.bank.repository.AccountRepository;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class BalanceService {
    private AccountService accountService;
    private ISOUtil isoUtil = new ISOUtil();

    public BalanceService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String process(String message){
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        String pinNumber = isoMessage.getString(52);

        boolean status = false;
        Customer customer;
        System.out.println("Account number: "+accountNumber);
        int balance=0;
        if(accountService.checkAccount(accountNumber,pinNumber)){
            status = true;
            customer = accountService.findByAccountNumber(accountNumber);
            balance = customer.getBalance();
        }

        return buildISO(accountNumber,status,balance);
    }

    private String buildISO(String accountNumber, boolean status, int balance){
        try{
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(2, accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, "0");
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "000001");
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            if(status){
                isoMsg.set(39, "yy");
                isoMsg.set(62, balance+"");
            }
            else{
                isoMsg.set(39, "nn");
                isoMsg.set(62, "0");
            }
            isoMsg.set(41, "12340001");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(49, "840");

            isoMsg.set(102, "0");

            byte[] result = isoMsg.pack();
            return new String(result);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
