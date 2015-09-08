package com.github.albfernandez.cleangpstrace;



import org.junit.Assert;
import org.junit.Test;

public class NMEAChecksumTest {

	@Test
	public void testCalculateChecksum () {
		Assert.assertEquals("45", NMEAChecksum.calculateCheckSum("GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A"));
	}
	
	@Test
	public void testValidChecksum () {
		Assert.assertTrue(NMEAChecksum.isValidCheckSum("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45"));
	}
	@Test
	public void testInvalidChecksum () {
		Assert.assertFalse(NMEAChecksum.isValidCheckSum("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*47"));
	}
}
