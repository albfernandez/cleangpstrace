package com.github.albfernandez.cleangpstrace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeekNumberRolloverTimeConverter implements TimeConverter {

    private SimpleDateFormat simple = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy HHmmss");
    private int epoch = -1;
    long[] limits = null;
    
    
    public WeekNumberRolloverTimeConverter() {
        this(-1);
    }
    
    public WeekNumberRolloverTimeConverter(int epoch) {
        super();
        this.epoch = epoch;
        try {
            limits = new long[]{
                  simple.parse("1980-01-06 00:00:00").getTime(),
                  simple.parse("1999-07-21 23:59:47").getTime(), // strange, but gpsbabel seems to use that
                  simple.parse("2019-04-06 23:59:42").getTime(),
                  simple.parse("2039-04-06 23:59:42").getTime()
            };
        } 
        catch (ParseException pe) {
            
        }
    }

    private long convertTime(Date date, int ep) {
        long temporal = date.getTime();
        int dateEpoch = getEpoch(date);
        
        if (dateEpoch == ep) {
            return temporal;
        }
        
        long diff = temporal - limits[dateEpoch];
        temporal = limits[ep] + diff;
        

        return temporal;
    }
    
    private int getEpoch(Date date) {
        long temporal = date.getTime();
        int currentEpoch = 0;
        while (limits[currentEpoch+1] < temporal) {
            currentEpoch++;
        }
        return currentEpoch++;
        
    }

    
    
    @Override
    public long convertTime(String date) {
        
        
        try {
            Date d = sdf.parse(date);
            return convertTime(d, this.epoch);
            
            
        } catch (ParseException e) {
            return 0;
        }
    }
    /*
Table 3.19. GPS week rollover dates
Starting from:  gps-week-rollover value:
1980-01-06 00:00:00 UTC 0
1999-08-21 23:59:47 UTC 1
2019-04-06 23:59:42 UTC 2

The default behaviour when gps-week-rollover isn't given (or is a negative number) is to assume the input data has been logged within the preceding 1024 weeks from the time gpsbabel is run, which should be perfectly fine in almost all cases. 
     */

    @Override
    public String convertToString(Date date) {
        return sdf.format(date);
    }
    
    
    
}
