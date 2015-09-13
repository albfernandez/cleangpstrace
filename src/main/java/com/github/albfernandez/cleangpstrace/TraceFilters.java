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
import java.util.ArrayList;
import java.util.List;

public final class TraceFilters {
	
	public static Trace removeUnwantedZones(final Trace trace){
		return trace;
	}
	public static List<Trace> splitTrace(final Trace trace, final int secondsToSplit){
		List<Trace> list = new ArrayList<>();
		long millisToSplit = secondsToSplit * 1000;
		long lastTime = 0;
		Trace currentTrace = new Trace();
		for (Record r: trace.getRecords()){
			if (r.getTime() - lastTime > millisToSplit){
				currentTrace = new Trace();
				list.add(currentTrace);
			}
			currentTrace.addRecord(r);
			lastTime = r.getTime();
		}
		
		return list;
	}
	public static Trace simplify(final Trace trace) {
		return simplify (trace, 1);
	}
	public static Trace simplify(final Trace trace, final double margin){
		Trace newTrace = new Trace();
		double lastDirection = 1000;
		
		for (Record r : trace.getRecords()){
			double n = r.getTrackAngle();
			double diff = Math.abs(n - lastDirection);
			if (diff > margin) {
				lastDirection = n;
				newTrace.addRecord(r);
			}
		}		
		return trace;
	}

}
