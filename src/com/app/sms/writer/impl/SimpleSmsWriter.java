package com.app.sms.writer.impl;

import com.app.sms.writer.SmsWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleSmsWriter implements SmsWriter {

    private static final String SMS_PATH = "sms-out";
    private static final String FILE_FORMAT = "sms%s.txt";

    @Override
    public void writeSms(String text) {

        String fileName= String.format(FILE_FORMAT, System.currentTimeMillis());

        File file = new File(SMS_PATH, fileName);

        try(FileOutputStream out = new FileOutputStream(file)){
            byte[] data = text.getBytes();
            out.write(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
