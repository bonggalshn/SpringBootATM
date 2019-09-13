package com.bank.service;

import com.bank.Util.ISOUtil;
import com.bank.entity.Customer;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TransferService {
    private AccountService accountService;
    private ISOUtil isoUtil = new ISOUtil();

    public TransferService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String processInquiry(String message) {
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        String pinNumber = isoMessage.getString(52);
        int amount = Integer.parseInt(isoMessage.getString(4));
        String beneficiaryNumber = isoMessage.getString(102);

        boolean status = false;
        Customer customer = null;
        Customer beneficiaryAccount = null;
        if (accountService.checkAccount(accountNumber, pinNumber)) {
            customer = accountService.findByAccountNumber(accountNumber);
            beneficiaryAccount = accountService.findByAccountNumber(beneficiaryNumber);
            if (customer != null && beneficiaryAccount != null) {
                if (customer.getBalance() > amount) {

                    if (beneficiaryAccount.getAccountName().length() > 20)
                        beneficiaryAccount.setAccountName(beneficiaryAccount.getAccountName().substring(0, 20));
                    if (beneficiaryAccount.getAccountNumber().length() > 20)
                        beneficiaryAccount.setAccountNumber(beneficiaryAccount.getAccountNumber().substring(0, 20));
                    status = true;
                }
            }
        }

        String response = buildISOInquiry(accountNumber, status, beneficiaryAccount, "001", amount);

        System.out.println("ISO MEssage: " + isoMessage);
        return response;
    }

    private String buildISOInquiry(String accountNumber, boolean status, Customer beneficiaryAccount, String beneficiaryBankCode, int amount) {
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(2, accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, amount + "");
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
            isoMsg.set(48, "0");
            isoMsg.set(49, "840");
            isoMsg.set(62, "0");
            isoMsg.set(100, "001");


            if (beneficiaryAccount == null) {
                isoMsg.set(102, "0");
                isoMsg.set(103, "0");
            } else {
                isoMsg.set(102, beneficiaryAccount.getAccountNumber());
                isoMsg.set(103, beneficiaryAccount.getAccountName());
            }
            isoMsg.set(125, "0");
            isoMsg.set(127, beneficiaryBankCode);

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String process(String message) {
        // TO DO: return balance left
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        String accountNumber = isoMessage.getString(2);
        int amount = Integer.parseInt(isoMessage.getString(4));
        String beneficiaryNumber = isoMessage.getString(102);

        Customer sender = accountService.findByAccountNumber(accountNumber);
        Customer beneficiary = accountService.findByAccountNumber(beneficiaryNumber);

        sender.setBalance(sender.getBalance() - amount);
        beneficiary.setBalance(beneficiary.getBalance() + amount);

        boolean status = false;
        try {
            accountService.update(sender);
            accountService.update(beneficiary);
            status = true;
        } catch (Exception e) {
            status = false;
        }

        return buildISOTransfer(sender.getAccountNumber(), status, beneficiary, "001", amount);

    }

    private String buildISOTransfer(String accountNumber, boolean status, Customer beneficiaryAccount, String beneficiaryBankCode, int amount) {
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(2, accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, amount + "");
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
            isoMsg.set(48, "0");
            isoMsg.set(49, "840");
            isoMsg.set(62, "0");
            isoMsg.set(100, "001");
            if (beneficiaryAccount == null) {
                isoMsg.set(102, "0");
                isoMsg.set(103, "0");
            } else {
                isoMsg.set(102, beneficiaryAccount.getAccountNumber());
                isoMsg.set(103, beneficiaryAccount.getAccountName());
            }
            isoMsg.set(125, "0");
            isoMsg.set(127, beneficiaryBankCode);

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
