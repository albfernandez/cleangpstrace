package com.github.albfernandez.cleangpstrace;

public final class NMEAParser {

	private NMEAParser() {
		throw new AssertionError("");
	}
	public static double parseNmeaPosition(String pos, String dir) {
		// 4227.201334
		double position = Double.parseDouble(pos);
		double grados = Math.floor(position / 100);
		double minutos = (position - grados * 100) / 60;

		double ret = grados + minutos;
		if ("S".equalsIgnoreCase(dir) || "W".equalsIgnoreCase(dir)) {
			ret = -ret;
		}
		return ret;
	}
}
