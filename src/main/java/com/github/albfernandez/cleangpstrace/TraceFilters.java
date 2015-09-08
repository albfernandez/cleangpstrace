package com.github.albfernandez.cleangpstrace;

import java.util.ArrayList;
import java.util.List;

public class TraceFilters {
	
	public static Trace removeUnwantedZones(Trace trace){
		
		return trace;
	}
	public static List<Trace> splitTrace(Trace trace, int secondsToSplit){
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
	public static Trace simplify(Trace trace) {
		return simplify (trace, 1);
	}
	public static Trace simplify(Trace trace, double margin){
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
