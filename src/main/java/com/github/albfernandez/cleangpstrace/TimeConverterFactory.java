package com.github.albfernandez.cleangpstrace;

public class TimeConverterFactory {

    private static TimeConverter converter = new DefaultTimeConverter();
    
    public static TimeConverter getConveter() {
        return converter;
    }
    public static void setConveter(TimeConverter timeConverter) {
        converter = timeConverter;
    }
    
}
