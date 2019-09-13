package com.bank.service.buildISO.paymentISO;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentISO {
    public String internetPaymentInquiryResponse(String accountNumber, String pinNumber, int amount, String internetCompanyName) {
        if (internetCompanyName.length()>20)
            internetCompanyName = internetCompanyName.substring(0,20);
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(2, accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, ""+amount);
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "000001");
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            isoMsg.set(39, "nn");
            isoMsg.set(41, "12340001");
            isoMsg.set(42, "000000000000000");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(48, "0");
            isoMsg.set(49, "840");
            isoMsg.set(62, "0");
            isoMsg.set(63, "0");
            isoMsg.set(102, internetCompanyName);

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println("InternetPaymentInquiry: " + e);
            return null;
        }
    }

    public String internetPaymentResponse(String accountNumber, int amount, String internetCompanyName, boolean status) {
        if (internetCompanyName.length()>20)
            internetCompanyName = internetCompanyName.substring(0,20);
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(2, accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, ""+amount);
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "000001");
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            if(status==true)
                isoMsg.set(39, "yy");
            else
                isoMsg.set(39, "nn");
            isoMsg.set(41, "12340001");
            isoMsg.set(42, "000000000000000");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(48, "0");
            isoMsg.set(49, "840");
            isoMsg.set(62, "0");
            isoMsg.set(63, "0");
            isoMsg.set(102, internetCompanyName);

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println("InternetPaymentInquiry: " + e);
            return null;
        }
    }

    public String rejectPayment(){
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0210");

            isoMsg.set(39, "05");

            isoMsg.set(102, "0");

            byte[] result = isoMsg.pack();
            return new String(result);
        }catch (Exception e){
            return null;
        }
    }
}
