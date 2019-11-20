package com.app.dao;

import com.app.sms.reader.SmsReader;
import com.app.sms.reader.impl.SimpleSmsReader;

import javax.print.attribute.DateTimeSyntax;
import java.awt.*;
import java.sql.SQLException;
import java.util.Date;

public   interface Dao    {

    public abstract void save( String Phone, String TextMessages );
   // public abstract void phoneAdmin() throws SQLException;



}
