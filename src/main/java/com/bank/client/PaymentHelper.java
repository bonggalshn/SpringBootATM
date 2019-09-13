package com.bank.client;

import com.bank.Util.ISOUtil;
import com.bank.client.interfaceClient.Payment;
import com.bank.client.payment.InternetPayment;
import org.jpos.iso.ISOMsg;

public class PaymentHelper {
    private String accountNumber, pinNumber;
    private ISOUtil isoUtil = new ISOUtil();

    public void PaymentMain(String accountNumber, String pinNumber) {
        this.accountNumber = accountNumber;
        this.pinNumber = pinNumber;

        showPaymentMenu();
        try {
            System.out.print("Entry: ");
            int entry = Integer.parseInt(ClientHelper.read());
            PaymentProcess(entry);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void showPaymentMenu() {
        System.out.println("\n\n-------------------------");
        System.out.println("Pembayaran");
        System.out.println("1. Internet");
    }

    private void PaymentProcess(int entry) {
        switch (entry) {
            case 1:
                System.out.print("Phone Number    : ");
                String phoneNumber = ClientHelper.read();
                System.out.print("Internet Number : ");
                String internetNumber = ClientHelper.read();
                System.out.print("Area Code       : ");
                String areaCode = ClientHelper.read();

                Payment internet = new InternetPayment.InternetBuilder(phoneNumber, internetNumber)
                        .addAreaCode(areaCode).build();
                String message = internet.payInquiry(accountNumber, pinNumber);
                ISOMsg isoMessage = isoUtil.stringToISO(message);

                System.out.println("\n\n--PEMBAYARAN--------------------");
                System.out.println("Anda akan melakukan pembayaran kepada: ");
                System.out.println("Nama    : " + isoMessage.getString(102));
                System.out.println("Jumlah  : " + Integer.parseInt(isoMessage.getString(4)));
                System.out.println("\nApakah anda ingin meneruskan pembayaran?");
                System.out.println("1. Ya");
                System.out.println("2. Tidak");

                try {
                    System.out.print("Entry: ");
                    int confirmation = Integer.parseInt(ClientHelper.read());

                    if (confirmation == 1) {
                        String response = internet.pay(message);
                        ISOMsg isoMsgResponse = isoUtil.stringToISO(response);
                        if(isoMsgResponse.getString(39).equals("yy")){
                            System.out.println("Transaksi berhasil");
                        }else {
                            System.out.println("Transaksi gagal");
                        }
                    } else
                        System.out.println("Transaksi dibatalkan");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("Masukan salah.");
                break;
        }
    }
}
