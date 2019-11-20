package com.app.workflow.impl;

import com.app.dao.Dao;
import com.app.sms.reader.SmsReader;
import com.app.sms.writer.SmsWriter;
import com.app.text.Decoder;
import com.app.text.Encoder;
import com.app.workflow.SmsWorkflow;

import java.io.IOException;

public class SimpleSmsWorkflow implements SmsWorkflow {
    private SmsReader smsReader;
    private Decoder decoder;
    private Dao dao;
    private SmsWriter smsWriter;
    private Encoder encoder;

    public SimpleSmsWorkflow(SmsReader smsReader, Decoder decoder, Dao dao, SmsWriter smsWriter, Encoder encoder) {
        this.smsReader = smsReader;
        this.decoder = decoder;
        this.dao = dao;
        this.smsWriter = smsWriter;
        this.encoder=encoder;
    }

    @Override
    public void processSms() throws IOException {
            String smsContent = smsReader.readSms();
            String smsDecodedContent = decoder.decode(smsContent);
          //  dao.save(smsDecodedContent);
            smsWriter.writeSms(smsDecodedContent);
            encoder.encode(smsDecodedContent);
    }
}
