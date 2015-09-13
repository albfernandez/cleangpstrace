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
public enum NMEAFixQuality {

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
	private NMEAFixQuality(int code) {
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
	public static NMEAFixQuality fromCode(String code) {
		if (code == null || !code.matches("^[0-9]+$")){
			return null;
		}
		return fromCode(Integer.parseInt(code));
	}
	public static NMEAFixQuality fromCode(int code) {
		for (NMEAFixQuality quality: NMEAFixQuality.values()){
			if (quality.getCode() == code){
				return quality;
			}
		}
		return null;
	}
}
/*
Fix quality: 
0 = invalid
1 = GPS fix (SPS)
2 = DGPS fix
3 = PPS fix
4 = Real Time Kinematic
5 = Float RTK
6 = estimated (dead reckoning) (2.3 feature)
7 = Manual input mode
8 = Simulation mode
*/