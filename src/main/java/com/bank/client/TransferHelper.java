package com.bank.client;

import com.bank.Util.ISOUtil;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransferHelper {
    private String accountNumber, pinNumber;
    private ISOUtil isoUtil = new ISOUtil();

    void TransferMain(String accountNumber, String pinNumber) {
        this.accountNumber = accountNumber;
        this.pinNumber = pinNumber;

        transferMenu();
        try {
            System.out.print("Entry: ");
            int entry = Integer.parseInt(ClientHelper.read());
            transferCase(entry);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void transferMenu() {
        System.out.println("\n--Transfer----------");
        System.out.println("1. Ke Bank Sama");
        System.out.println("2. Ke Bank Lain");
        System.out.println("----------------------");
    }

    private void transferCase(int entry) {
        switch (entry) {
            case 1:
                String message = transferInquiry();
                transferProcess(message);
                break;
            case 2:
                break;
            default:
                System.out.println("Masukan salah.");
                break;
        }
    }

    private String transferInquiry() {
        System.out.print("Masukkan nomor rekening pemindahbukuan: ");
        String tujuan = ClientHelper.read();
        System.out.print("Masukkan jumlah yang akan ditransfer: ");
        int amount = Integer.parseInt(ClientHelper.read());
        String message = buildISOInquiry(tujuan, amount);

        return ClientHelper.sendData(message, "http://localhost:8080/transfer/sameInquiry");
    }

    private void transferProcess(String message) {
        ISOMsg isoMessage = isoUtil.stringToISO(message);

        if (isoMessage.getString(39).equals("nn")) {
            System.out.println("Transaksi tidak dapat dilakukan");
            return;
        }

        String nama = isoMessage.getString(103);
        String rekTujuan = isoMessage.getString(102);
        String jumlah = isoMessage.getString(4);

        System.out.println("\n\n------------------------------------");
        System.out.println("Anda akan melakukan transfer kepada:");
        System.out.println("Nama           : " + nama);
        System.out.println("Nomor Rekening : " + rekTujuan);
        System.out.println("Jumlah         : " + (Integer.parseInt(jumlah)));

        System.out.println("Apakah anda yakin?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");

        int entry = 0;
        do {
            try {
                System.out.print("Entry: ");
                entry = Integer.parseInt(ClientHelper.read());
                break;
            } catch (Exception e) {
                System.out.println("Masukan salah");
            }
        } while (true);

        if (entry == 2) {
            return;
        }

        String transferMessage = buildISOTransfer(rekTujuan, Integer.parseInt(jumlah));
        String response = ClientHelper.sendData(transferMessage, "http://localhost:8080/transfer/same");
        System.out.println(response);
        String result = isoUtil.stringToISO(response).getString(39);

        if (result.equals("yy")){
            System.out.println("Transaksi Berhasil");
        }else {
            System.out.println("Transaksi Gagal");
        }
    }

    private String buildISOInquiry(String tujuan, int jumlah) {
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");

            isoMsg.set(2, this.accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, jumlah + "");
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "000001");
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            isoMsg.set(41, "12340001");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(48, "0");
            isoMsg.set(49, "360");
            isoMsg.set(52, this.pinNumber);
            isoMsg.set(62, "0");
            isoMsg.set(100, "001");
            isoMsg.set(102, tujuan);//rek tujuan
            isoMsg.set(103, "0");//nama
            isoMsg.set(125, "0");
            isoMsg.set(127, "001");

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String buildISOTransfer(String tujuan, int jumlah) {
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");

            isoMsg.set(2, this.accountNumber);
            isoMsg.set(3, "300000");
            isoMsg.set(4, jumlah + "");
            isoMsg.set(7, new SimpleDateFormat("MMddHHmmss").format(new Date()));
            isoMsg.set(11, "000001");
            isoMsg.set(12, new SimpleDateFormat("HHmmss").format(new Date()));
            isoMsg.set(13, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(15, new SimpleDateFormat("MMdd").format(new Date()));
            isoMsg.set(18, "0000");
            isoMsg.set(32, "00000000000");
            isoMsg.set(33, "00000000000");
            isoMsg.set(37, "000000000000");
            isoMsg.set(41, "12340001");
            isoMsg.set(43, "0000000000000000000000000000000000000000");
            isoMsg.set(48, "0");
            isoMsg.set(49, "360");
            isoMsg.set(52, this.pinNumber);
            isoMsg.set(62, "0");
            isoMsg.set(100, "001");
            isoMsg.set(102, tujuan);//rek tujuan
            isoMsg.set(103, "0");//nama
            isoMsg.set(125, "0");
            isoMsg.set(127, "001");

            byte[] result = isoMsg.pack();
            return new String(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
