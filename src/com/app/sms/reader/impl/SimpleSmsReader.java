package com.app.sms.reader.impl;

import com.app.dao.DaoImpl;
import com.app.sms.reader.SmsReader;
import com.app.sms.sending.sms.TerminalSendSMS;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SimpleSmsReader extends PhoneAdmin  implements SmsReader {


    Path path = Paths.get("C:\\Users\\Николай\\Documents\\TestSMS\\");

    @Override
    public String readSms() throws IOException {
        WatchService watchService = null;
        try {
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);


        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // Infinite loop
        for (; ; ) {
            WatchKey key = null;
            try {
                key = watchService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Iterations for each event
            for (WatchEvent event : key.pollEvents()) {
                switch (event.kind().name()) {
                    case "OVERFLOW":
                        System.out.println("We lost some events");
                        break;
                    case "ENTRY_CREATE":
                        smsReader();
                        break;

                    case "ENTRY_MODIFY":

                        break;
                }
            }
            // Resetting the key is important for future notifications
            key.reset();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }


    public void smsReader() throws IOException, NumberFormatException {

        try {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            List<Long> integers = new ArrayList<>();
        // Reading lines from a file.
        String fileName = defineFileName(path.toString());
        List<String> lines = Files.readAllLines(Paths.get("C:\\Users\\Николай\\Documents\\TestSMS\\" + fileName));
        String line1 = lines.get(0);
        String line2 = lines.get(3);
        String TextMessages = lines.get(13);
            System.out.println(line1);
          //  System.out.println(line2);
            System.out.println(TextMessages);


        line1 = line1;
        Pattern pattern = Pattern.compile("\\d+\\S?\\d*");
        Matcher matcher = pattern.matcher(line1);
            while (matcher.find()) {
                String Phone = matcher.group(0);
                if (Phone.replaceAll("\\D", " ").length() == Phone.length()) {


                  try { integers.add(Long.parseLong(Phone));

                      phoneAdmin();


                  }catch (NumberFormatException e){


                  }

                  }



                line2 = line2;
                Pattern pattern1 = Pattern.compile("\\d+\\S?\\d*\\d+\\S?\\d*\\d+\\S?\\d");
                Matcher matcher1 = pattern1.matcher(line2);
                while (matcher1.find()) {
                    String Date = matcher1.group(0);
                    if (Date.replaceAll("\\D", " ").length() == Date.length()) {

                        String  dateTime = Date;
                       // DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy-MM-dd'TTTTTT'hh:mm:ss");
                        System.out.print(dateTime+" ");
                        DaoImpl dao = new DaoImpl();
                        dao.save(Phone,TextMessages);

                    }

                }

      }

            //Delete after reading

            File file = new File("C:\\Users\\Николай\\Documents\\TestSMS\\" + fileName);
            if (file.delete()) {
                System.out.println("delete");
                readSms();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private String defineFileName (String dirpas) {
        File file = new File(dirpas);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File a = files[i];
            String fileName = a.getName();
             System.out.println(fileName);
            return fileName;
        }
        return null;
    }
}

