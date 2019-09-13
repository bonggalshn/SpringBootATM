package com.bank.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Scanner;

public class ClientHelper {
    private String accountNumber;
    private String pinNumber;
//    private AccountService accountService;

//    public ClientHelper(AccountService accountService) {
//        this.accountService = accountService;
//    }

    ClientHelper() {
    }

    boolean showMainMenu() {
        System.out.println("\n\n-----------------------");
        System.out.print("Account Number: ");
        accountNumber = read();
        System.out.print("Pin Number    : ");
        pinNumber = read();
        System.out.println("-----------------------");

        this.accountNumber = accountNumber;
        this.pinNumber = pinNumber;

        System.out.println("--ATM MACHINE----------");
        System.out.println("1. Tarik Tunai    4. Info Saldo");
        System.out.println("2. Transfer       5. Pembayaran");
        System.out.println("3. Pembelian      6. Keluar");
        System.out.println("-----------------------");

        return true;
    }

    public static String read() {
        Scanner scan = new Scanner(System.in);
        String result = scan.nextLine();
        return result;
    }

    void processMenu(int entry) {
        switch (entry) {
            case 1:
                WithdrawalHelper withdrawal = new WithdrawalHelper();
                withdrawal.withdrawalMain(accountNumber, pinNumber);
                break;

            case 2:
                TransferHelper transferHelper = new TransferHelper();
                transferHelper.TransferMain(accountNumber, pinNumber);
                break;
            case 3:

                break;
            case 4:
                BalanceHelper balance = new BalanceHelper();
                balance.inquiry(accountNumber, pinNumber);
                break;

            case 5:
                PaymentHelper paymentHelper = new PaymentHelper();
                paymentHelper.PaymentMain(accountNumber, pinNumber);
                break;
            case 6:
                System.out.println("Keluar");
                break;
            default:
                System.out.println("Masukan salah.");
                break;
        }
    }

    public static String sendData(String data, String uri) {
        try{
            CloseableHttpClient client = HttpClients.createSystem();
            HttpPost httpPost = new HttpPost(uri);

            StringEntity entity = new StringEntity(data);

            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse response = client.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity());
            client.close();
            return result;
        }catch (Exception e){
            return "";
        }
    }
}
