package com.github.albfernandez.cleangpstrace;

import java.math.BigDecimal;
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
import java.util.ArrayList;
import java.util.List;

public final class TraceFilters {
    private TraceFilters() {
        throw new AssertionError("No instances of this class are allowed");
    }

    public static Trace removeUnwantedZones(final Trace trace) {
        return trace;
    }

    public static List<Trace> splitTrace(final Trace trace, final int secondsToSplit) {
        List<Trace> list = new ArrayList<>();
        long millisToSplit = secondsToSplit * 1000L;
        long lastTime = 0;
        Trace currentTrace = new Trace();
        for (Record r : trace.getRecords()) {
            if (r.getTime() - lastTime > millisToSplit) {
                currentTrace = new Trace();
                list.add(currentTrace);
            }
            currentTrace.addRecord(r);
            lastTime = r.getTime();
        }
        
        return cleanShortTraces(list);
    }

    private static List<Trace> cleanShortTraces(List<Trace> list) {
		List<Trace> newList = new ArrayList<>();
		for (Trace t: list) {
			if (isValid(t)) {
				newList.add(t);
			}
		}
		return newList;
		
	}

	private static boolean isValid(Trace t) {
		if (t.getRecords().size() >= 600) {
			return true;
		}
		double minLon = Double.POSITIVE_INFINITY;
		double maxLon = Double.NEGATIVE_INFINITY;
		double minLat = Double.POSITIVE_INFINITY;
		double maxLat = Double.NEGATIVE_INFINITY;
		for (Record r: t.getRecords()) {
			maxLat = Math.max(maxLat, r.getLat());
			minLat = Math.min(minLat, r.getLat());
			maxLon = Math.max(maxLon, r.getLon());
			minLon = Math.min(minLon, r.getLon());
		}
		System.out.println (maxLat + " " + minLat + " " + maxLon + " " + minLon + 
				" " + (maxLat - minLat) + " " + (maxLon - minLon) );
		return maxLat - minLat > 0.001 || maxLon - minLon > 0.001;
	}

	public static Trace simplify(final Trace trace) {
        return simplify(trace, 1);
    }

    public static Trace simplify(final Trace trace, final double margin) {
        Trace newTrace = new Trace();
        double lastDirection = Integer.MAX_VALUE;

        for (Record r : trace.getRecords()) {
            BigDecimal angle = r.getTrackAngle();
            double n = 0.0;
            if (angle != null) {
                n = angle.doubleValue();
            }
            double diff = Math.abs(n - lastDirection);
            if (diff > margin) {
                lastDirection = n;
                newTrace.addRecord(r);
            }
        }
        return trace;
    }

}
