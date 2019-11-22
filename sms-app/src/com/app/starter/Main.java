package com.app.starter;

import com.app.dao.Dao;
import com.app.dao.DaoImpl;
import com.app.sms.reader.SmsReader;
import com.app.sms.reader.impl.SimpleSmsReader;
import com.app.sms.sending.sms.TerminalSendSMS;
import com.app.sms.writer.SmsWriter;
import com.app.sms.writer.impl.SimpleSmsWriter;
import com.app.text.Decoder;
import com.app.text.Encoder;
import com.app.text.decoder.impl.SimpleDecoder;
import com.app.text.encoder.impl.SimpleEncoder;
import com.app.workflow.SmsWorkflow;
import com.app.workflow.impl.SimpleSmsWorkflow;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        SmsReader  smsReader = new SimpleSmsReader();
        Decoder decoder = new SimpleDecoder();
        Dao dao = new DaoImpl();
        SmsWriter smsWriter = new SimpleSmsWriter();
        Encoder encoder = new SimpleEncoder();
        TerminalSendSMS sendSMS = new TerminalSendSMS();

        SmsWorkflow smsWorkflow = new SimpleSmsWorkflow(smsReader, decoder, dao, smsWriter, encoder);
        smsWorkflow.processSms();


    }
}
