package com.github.albfernandez.cleangpstrace;

/*
 (C) Copyright 2014-2015 Alberto Fernández <infjaf@gmail.com>

 This file is part of cleangpstrace.

 Foobar is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Foobar is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public final class Record {

    private String[] data = null;
    private String timeAsString;
    private double lat;
    private double lon;
    private NMEAFixQuality fixQuality;
    private int satellites;
    private double altitude;
    private double speedInKnots;
    private double trackAngle;
    private String dateAsString;
    private long time = -1;
    private SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy HHmmss");

    private Record(final String theData) {
        super();
        this.data = new String[] {theData};
    }

    public static Record createRecord(final String line) {
        if (StringUtils.isBlank(line)) {
            return null;
        }
        if (!line.startsWith("$GP")) {
            return null;
        }
        if (!NMEAChecksum.isValidCheckSum(line)) {
            return null;
        }
        if (line.startsWith("$GPRMC")) {
            return createGPRMCRecord(line);
        } else if (line.startsWith("$GPGGA")) {
            return createGPGGARecord(line);
        }
        return null;
    }

    private static Record createGPGGARecord(final String line) {
        try {
            Record record = new Record(line);
            String[] rec = line.split(",");
            record.timeAsString = rec[1];
            record.lat = NMEAParser.parseNmeaPosition(rec[2], rec[3]);
            record.lon = NMEAParser.parseNmeaPosition(rec[4], rec[5]);
            record.fixQuality = NMEAFixQuality.fromCode(rec[6]);
            record.satellites = Integer.parseInt(rec[7]);
            // hdp rec-8 - rec-9
            record.altitude = Double.parseDouble(rec[9]);
            // rec-11 unidades
            // rec-12 y rec13 Height of geoid (mean sea level) above WGS84 ellipsoid
            return record;
        }
        catch (Exception e) {
            return null;
        }
        /*
         * GGA - essential fix data which provide 3D location and accuracy data.
         *
         * $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
         *
         * Where: GGA Global Positioning System Fix Data 123519 Fix taken at
         * 12:35:19 UTC 4807.038,N Latitude 48 deg 07.038' N 01131.000,E
         * Longitude 11 deg 31.000' E 1 Fix quality: 0 = invalid 1 = GPS fix
         * (SPS) 2 = DGPS fix 3 = PPS fix 4 = Real Time Kinematic 5 = Float RTK
         * 6 = estimated (dead reckoning) (2.3 feature) 7 = Manual input mode 8
         * = Simulation mode 08 Number of satellites being tracked 0.9
         * Horizontal dilution of position 545.4,M Altitude, Meters, above mean
         * sea level 46.9,M Height of geoid (mean sea level) above WGS84
         * ellipsoid (empty field) time in seconds since last DGPS update (empty
         * field) DGPS station ID number47 the checksum data, always begins with
         * *
         */

    }

    private static Record createGPRMCRecord(final String line) {
        try {
            Record record = new Record(line);
            String[] rec = line.split(",");
            record.timeAsString = rec[1];
            record.lat = NMEAParser.parseNmeaPosition(rec[3], rec[4]);
            record.lon = NMEAParser.parseNmeaPosition(rec[5], rec[6]);
            record.speedInKnots = Double.parseDouble(rec[7]);
            record.trackAngle = Double.parseDouble(rec[8]);
            record.dateAsString = rec[9];
            // hpos rec-8 - rec-9
    
            return record;
        }
        catch (Exception e) {
            return null;
        }

        /*
         * RMC - NMEA has its own version of essential gps pvt (position,
         * velocity, time) data. It is called RMC, The Recommended Minimum,
         * which will look similar to:
         *
         * $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
         *
         * Where: RMC Recommended Minimum sentence C 123519 Fix taken at
         * 12:35:19 UTC A Status A=active or V=Void. 4807.038,N Latitude 48 deg
         * 07.038' N 01131.000,E Longitude 11 deg 31.000' E 022.4 Speed over the
         * ground in knots 084.4 Track angle in degrees True 230394 Date - 23rd
         * of March 1994 003.1,W Magnetic Variation6A The checksum data, always
         * begins with *
         */

    }

    public Object getTimeAsString() {
        return this.timeAsString;
    }

    public NMEAFixQuality getFixQuality() {
        return this.fixQuality;
    }

    public int getSatellites() {
        return this.satellites;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public String[] getData() {
        return Arrays.copyOf(this.data, this.data.length);
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getSpeedInKnots() {
        return this.speedInKnots;
    }

    public double getTrackAngle() {
        return this.trackAngle;
    }

    public String getDateAsString() {
        return this.dateAsString;
    }

    public String toNMEAString() {
        StringBuilder sb = new StringBuilder();
        for (String d : this.data) {
            sb.append(d).append("\n");
        }
        return sb.toString();
    }

    public boolean isRMC() {
        return this.data != null && this.data.length == 1
                && this.data[0].startsWith("$GPRMC");
    }

    public boolean isGGA() {
        return this.data != null && this.data.length == 1
                && this.data[0].startsWith("$GPGGA");
    }

    public void setDateAsString(final String date) {
        this.dateAsString = date;
        this.time = -1;
    }

    public long getTime() {
        if (this.time < 0) {
            calculateTime();
        }
        return this.time;
    }

    private void calculateTime() {
        try {
            this.time = this.sdf.parse(
                    this.dateAsString + " " + this.timeAsString).getTime();
        } catch (ParseException e) {
            this.time = 0;
        }

    }

    public void join(final Record r) {
        if (this.isGGA() && r.isRMC()) {
            joinRMC(r);
        } else if (this.isRMC() && r.isGGA()) {
            joinGGA(r);
        }

    }

    private void joinRMC(final Record r) {
        if (this.isGGA() && r.isRMC()) {
            String oldData = this.data[0];
            this.data = new String[2];
            this.data[0] = oldData;
            this.data[1] = r.data[0];
            this.dateAsString = r.dateAsString;
            this.speedInKnots = r.speedInKnots;
            this.trackAngle = r.trackAngle;
        }

    }

    private void joinGGA(final Record r) {
        if (this.isRMC() && r.isGGA()) {
            String oldData = this.data[0];
            this.data = new String[2];
            this.data[0] = oldData;
            this.data[1] = r.data[0];
            this.lat = r.lat;
            this.lon = r.lon;
            this.fixQuality = r.fixQuality;
            this.satellites = r.satellites;
            this.altitude = r.altitude;
        }

    }

}

// REFS:
// http://aprs.gids.nl/nmea/
// http://www.gpsinformation.org/dale/nmea.htm

