package com.bank.Util;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;

import java.io.InputStream;

public class ISOUtil {
    public void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }

    public ISOMsg stringToISO(String isoMessage){
        ISOMsg isoMsg = new ISOMsg();

        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);

            // Setting packager
            isoMsg.setPackager(packager);

            byte[] bIsoMessage = new byte[isoMessage.length()];
            for (int i = 0; i < bIsoMessage.length; i++) {
                bIsoMessage[i] = (byte) (int) isoMessage.charAt(i);
            }

            isoMsg.unpack(bIsoMessage);

            return isoMsg;

        }catch (Exception e){
            System.out.println("ISOUTIL Exception: "+e.getMessage());
            return isoMsg;
        }
    }
}
