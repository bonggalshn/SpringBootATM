package com.bank.service;

import com.bank.Util.ISOUtil;
import com.bank.entity.Customer;
import com.bank.repository.AccountRepository;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class WithdrawalService {
    private AccountService accountService;
    private ISOUtil isoUtil = new ISOUtil();

    public WithdrawalService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String process(String message) {
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        String pinNumber = isoMessage.getString(52);
        int amount = Integer.parseInt(isoMessage.getString(4));

        boolean status = false;
        Customer customer;
        if (accountService.checkAccount(accountNumber, pinNumber)) {
            customer = accountService.findByAccountNumber(accountNumber);
            if (customer != null) {
                if (customer.getBalance() > amount && amount % 50000 == 0) {
                    status = true;
                    customer.setBalance((customer.getBalance() - amount));
                    accountService.update(customer);
                }
            }
        }

        String response = buildISO(accountNumber, status);

        return response;
    }

    private String buildISO(String accountNumber, boolean status) {
        try {
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

            if (!status)
                isoMsg.set(39, "nn");
            else
                isoMsg.set(39, "yy");

            isoMsg.set(41, "12340001");
            isoMsg.set(42, "000000000000000");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(49, "840");
            isoMsg.set(54, "0");
            isoMsg.set(62, "0");
            isoMsg.set(102, "0");

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
