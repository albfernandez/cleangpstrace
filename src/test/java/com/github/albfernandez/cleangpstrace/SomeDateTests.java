package com.github.albfernandez.cleangpstrace;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;



public class SomeDateTests {
    
    public SomeDateTests() {
        super();
    }
    @Test
    public void test() {
        Assert.assertEquals(10,prueba(1563171209000L)); // 10.13    
        Assert.assertEquals(23, prueba(1548278347000L)); //23.19
        
    }

    private int prueba(long time) { 
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.add(Calendar.MILLISECOND, c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET));
        
        return c.get(Calendar.HOUR_OF_DAY);
        
        
        
    }
}
