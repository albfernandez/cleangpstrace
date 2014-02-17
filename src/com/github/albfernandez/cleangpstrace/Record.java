package com.github.albfernandez.cleangpstrace;

import org.apache.commons.lang3.StringUtils;

public class Record {
	

	private String[] data = null;
	private String timeAsString;
	private Record(String data){
		super();
		this.data = new String[]{data};
	}
	public static Record createRecord (String line) {
		if (StringUtils.isBlank(line)){
			return null;
		}
		if (!NMEAChecksum.isValidCheckSum(line)){
			return null;
		}
		if (line.startsWith("$GPRMC")){
			return createGPRMCRecord(line);
		}
		else if (line.startsWith("$GPGGA")){
			return createGPGGARecord(line);
		}
		return null;
	}
	
	private static Record createGPGGARecord(String line) {
		Record record = new Record(line);
		return record;
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
	private static Record createGPRMCRecord(String line) {
		// TODO Auto-generated method stub
		Record record = new Record(line);
		return record;
		
		/*
		RMC - NMEA has its own version of essential gps pvt (position, velocity, time) data. It is called RMC, The Recommended Minimum, which will look similar to:

		$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A

		Where:
		     RMC          Recommended Minimum sentence C
		     123519       Fix taken at 12:35:19 UTC
		     A            Status A=active or V=Void.
		     4807.038,N   Latitude 48 deg 07.038' N
		     01131.000,E  Longitude 11 deg 31.000' E
		     022.4        Speed over the ground in knots
		     084.4        Track angle in degrees True
		     230394       Date - 23rd of March 1994
		     003.1,W      Magnetic Variation
		     *6A          The checksum data, always begins with *
		*/

	}
	public Object getTimeAsString() {
		return timeAsString;
	}
	
}

// REFS: 
// http://aprs.gids.nl/nmea/
// http://www.gpsinformation.org/dale/nmea.htm

