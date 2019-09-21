package com.github.albfernandez.cleangpstrace;

/*
 (C) Copyright 2014-2015 Alberto Fernández <infjaf@gmail.com>

 This file is part of cleangpstrace.

 cleangpstrace is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 cleangpstrace is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with cleangpstrace.  If not, see <http://www.gnu.org/licenses/>.
 */
public enum NMEAFixQuality {
    /*
     * Fix quality: 0 = invalid 1 = GPS fix (SPS) 2 = DGPS fix 3 = PPS fix 4 = Real
     * Time Kinematic 5 = Float RTK 6 = estimated (dead reckoning) (2.3 feature) 7 =
     * Manual input mode 8 = Simulation mode
     */
    INVALID(0),
    GPS_FIX(1),
    DGPS_FIX(2),
    PPS_FIX(3),
    REAL_TIME_KINEMATIC(4),
    FLOAT_RTK(5),
    ESTIMATED(6),
    MANUAL_INPUT_MODE(7),
    SIMULATION_MODE(8);

    private int code = 0;

    private NMEAFixQuality(final int theCode) {
        this.code = theCode;
    }

    public int getCode() {
        return this.code;
    }

    public static NMEAFixQuality fromCode(final String code) {
        if (code == null || !code.matches("^[0-9]+$")) {
            return null;
        }
        return fromCode(Integer.parseInt(code));
    }

    public static NMEAFixQuality fromCode(final int code) {
        for (NMEAFixQuality quality : NMEAFixQuality.values()) {
            if (quality.getCode() == code) {
                return quality;
            }
        }
        return null;
    }
}
