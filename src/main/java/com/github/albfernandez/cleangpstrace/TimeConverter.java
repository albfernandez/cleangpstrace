package com.github.albfernandez.cleangpstrace;

import java.util.Date;

public interface TimeConverter {

    long convertTime(String date);
    String convertToString(Date date);
}
