package com.github.albfernandez.cleangpstrace;

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
