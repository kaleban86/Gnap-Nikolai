package com.app.dao;

import com.app.sms.reader.impl.SimpleSmsReader;

import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Calendar;

public class DaoImpl extends SimpleSmsReader implements Dao {

    @Override
    public  void save(String Phone, String TextMessages) {
       try(Connection c = getConnection()){
           String sql =  "INSERT INTO sms.log (Phone,TextMessages) VALUES (?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,Phone);
            ps.setString(2,TextMessages);
            ps.execute();
        }catch(Exception ex){
            throw new RuntimeException(ex);

        }
    }


    private Connection getConnection() throws SQLException {


        return DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root", "530433");







    }




}
