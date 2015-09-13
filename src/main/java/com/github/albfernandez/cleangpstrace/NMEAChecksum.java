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
public class NMEAChecksum {
	
	public static boolean isValidCheckSum(String line) {
		if ('*' != line.charAt(line.length() -3)){
			return false;
		}
		String checksum = line.substring(line.length() -2);
		String checkString = line.substring(1, line.length() -3);
		String calculated = calculateCheckSum(checkString);
		return calculated.equals(checksum);
	}
	public static String calculateCheckSum(String checkString) {
		char value = 0;
		for(char c: checkString.toCharArray()) {
			value ^= c;
		}
		// TODO Auto-generated method stub
		return toHexString(value);
	}
	public static String toHexString(char c) {
		String tmp = Integer.toHexString(c);
		if (tmp.length() < 2){
			tmp = "0" + tmp;
		}
		return tmp;
	}
}
