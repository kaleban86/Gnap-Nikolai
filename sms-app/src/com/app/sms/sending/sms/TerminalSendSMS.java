package com.app.sms.sending.sms;

import com.app.sms.reader.impl.SimpleSmsReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalSendSMS  implements SendSms {




 /*
    public static void errorSend() {

      Process p = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "sendsms 79827956841 'www' "});
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;

        while (true) {
            try {
                if (!((line = input.readLine()) != null)) break;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println(line);
        }

        int exitVal = 0;
        try {
            exitVal = p.waitFor();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("Exited with error code " + exitVal);


    }



     */
    }











