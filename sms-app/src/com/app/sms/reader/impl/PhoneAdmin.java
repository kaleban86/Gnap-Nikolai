package com.app.sms.reader.impl;

import com.app.dao.Dao;
import com.app.sms.reader.SmsReader;
import com.app.sms.sending.sms.SendSms;
import com.app.sms.sending.sms.TerminalSendSMS;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class PhoneAdmin extends Blacklist implements SendSms {





    private static boolean isPhoneAdminExists;
   // String PhoneNumber = null;

    public static void phoneAdmin(){


        try (Connection c = getConnection()) {

           // String sql = "SELECT * FROM sms.phones when PhoneNumber = 79827956841 " ;
            String sql = "SELECT * from sms_log.users where username = 'Pedro'";
            PreparedStatement ps = c.prepareStatement(sql);


            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    isPhoneAdminExists = true;


                }

                adminSmsRid();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (isPhoneAdminExists) {


            blackList();



        }

    }





    private static Connection getConnection() throws SQLException {

        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sys", "root", "530433");
    }




    private static boolean isadminSmsRidExists = false;
    public static void adminSmsRid(){

        try (Connection c  = getConnection()){

            String sql = "SELECT * from sms.log where TextMessages = 'Test ?'";
            PreparedStatement ps =  c.prepareStatement(sql);


            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    isadminSmsRidExists= true;
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();


        }

        if (isPhoneAdminExists) {

            System.out.println("SmsRid"+"Admin");
        }


    }
    }











