package com.github.albfernandez.cleangpstrace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultTimeConverter implements TimeConverter {

    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy HHmmss");
    
    public DefaultTimeConverter() {
        super();
    }
    
    @Override
    public long convertTime(String date) {
        try {
            return this.sdf.parse(date).getTime();
        }
        catch (ParseException pe) {
            return 0;
        }        
    }
    
    @Override
    public String convertToString(Date date) {
        return sdf.format(date);
    }
    
    
}
