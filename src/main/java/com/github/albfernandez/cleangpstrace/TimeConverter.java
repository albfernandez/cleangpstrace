package com.github.albfernandez.cleangpstrace;

import java.util.Date;

public interface TimeConverter {

    public long convertTime(String date);
    public String convertToString(Date date);
}
