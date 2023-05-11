package com.example.aiot.config;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @TypeConverter
    public static String toString(Date date) {
        return date == null ? null : formatter.format(date);
    }

    @TypeConverter
    public static Date toDate(String strDate) {
        try {
            return strDate == null ? null : formatter.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }
}
