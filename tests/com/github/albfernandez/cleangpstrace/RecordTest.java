package com.github.albfernandez.cleangpstrace;

import org.junit.Assert;
import org.junit.Test;

public class RecordTest {

	private static final String TEST_GPGGA_1 = "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47";
	private static final String TEST_GPGGA_BAD = "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*48";
	@Test
	public void testGGANulls () {
		Assert.assertNull(Record.createRecord(null));
		Assert.assertNull(Record.createRecord(""));
		Assert.assertNull(Record.createRecord(" "));
		Assert.assertNull(Record.createRecord("prueba"));
	}
	@Test 
	public void testGGAChecksum () {
		Assert.assertNull(Record.createRecord(TEST_GPGGA_BAD));
	}
	@Test
	public void testDateTime () {
		Record record = Record.createRecord(TEST_GPGGA_1);
		Assert.assertEquals("123519", record.getTimeAsString());
		Assert.assertTrue(NMEAFixQuality.GPS_FIX == record.getFixQuality());
		//Assert.assertEquals(expected, actual);
	}
	

	
	
	/*
	GGA - essential fix data which provide 3D location and accuracy data.

	 $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47

	Where:
	     GGA          Global Positioning System Fix Data
	     123519       Fix taken at 12:35:19 UTC
	     4807.038,N   Latitude 48 deg 07.038' N
	     01131.000,E  Longitude 11 deg 31.000' E
	     1            Fix quality: 0 = invalid
	                               1 = GPS fix (SPS)
	                               2 = DGPS fix
	                               3 = PPS fix
				       4 = Real Time Kinematic
				       5 = Float RTK
	                               6 = estimated (dead reckoning) (2.3 feature)
				       7 = Manual input mode
				       8 = Simulation mode
	     08           Number of satellites being tracked
	     0.9          Horizontal dilution of position
	     545.4,M      Altitude, Meters, above mean sea level
	     46.9,M       Height of geoid (mean sea level) above WGS84
	                      ellipsoid
	     (empty field) time in seconds since last DGPS update
	     (empty field) DGPS station ID number
	     *47          the checksum data, always begins with *
	*/
	
	
	/*
	GGA - essential fix data which provide 3D location and accuracy data.

	 $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47

	Where:
	     GGA          Global Positioning System Fix Data
	     123519       Fix taken at 12:35:19 UTC
	     4807.038,N   Latitude 48 deg 07.038' N
	     01131.000,E  Longitude 11 deg 31.000' E
	     1            Fix quality: 0 = invalid
	                               1 = GPS fix (SPS)
	                               2 = DGPS fix
	                               3 = PPS fix
				       4 = Real Time Kinematic
				       5 = Float RTK
	                               6 = estimated (dead reckoning) (2.3 feature)
				       7 = Manual input mode
				       8 = Simulation mode
	     08           Number of satellites being tracked
	     0.9          Horizontal dilution of position
	     545.4,M      Altitude, Meters, above mean sea level
	     46.9,M       Height of geoid (mean sea level) above WGS84
	                      ellipsoid
	     (empty field) time in seconds since last DGPS update
	     (empty field) DGPS station ID number
	     *47          the checksum data, always begins with *
	*/
}
