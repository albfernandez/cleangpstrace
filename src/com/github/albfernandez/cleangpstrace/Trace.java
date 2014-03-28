package com.github.albfernandez.cleangpstrace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.StringUtils;

public class Trace {
	private List<Record> records = new ArrayList<>();
	public Trace() {
		super();
	}
	public static Trace load(File file) throws IOException {
		if (file.getName().toLowerCase().endsWith(".gz")){
			try (InputStream is = new GZIPInputStream(new FileInputStream(file))){
				return load(is);
			}
		}
		try (InputStream is = new FileInputStream(file)){
			return load(is);
		}
	}
	public static Trace load(InputStream is) throws IOException {
		try (Reader reader = new InputStreamReader(is)){
			return load(reader);
		}
	}
	public static Trace load(Reader reader1) throws IOException {
		Trace trace = new Trace();
		try (BufferedReader reader = new BufferedReader(reader1)){
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!StringUtils.isBlank(line)){
					Record r = Record.createRecord(line);					
					trace.addRecord(r);
				}
			}
		}
		trace.joinRecords();
		trace.removeUnorderedPoints();
		return trace;
	}
	private void removeUnorderedPoints() {
		List<Record> newRecords = new ArrayList<>();
		long lastTime = 0;
		for (Record r : records){
			long recordTime = r.getTime();
			if (recordTime > lastTime) {
				lastTime = recordTime;
				newRecords.add(r);
			}
		}
		
		this.records = newRecords;
	}
	private void joinRecords() {
		List<Record> newRecords = new ArrayList<>();		
		for (Record r : records) {
			if (r.isGGA() || r.isRMC()){
				if (!newRecords.isEmpty()){
					Record last = newRecords.get(newRecords.size()-1);
					if (r.getTimeAsString().equals(last.getTimeAsString())){
						last.join(r);
					}
					else {
						newRecords.add(r);
					}
				}
				else {
					newRecords.add(r);
				}
			}
		}
		String lastValidDate = null;
		for (Record r: newRecords) {
			String recordDate = r.getDateAsString();
			if (!StringUtils.isBlank(recordDate)){
				lastValidDate = recordDate;
			}
			else if (!StringUtils.isBlank(lastValidDate)){
				r.setDateAsString(lastValidDate);
			}
		}
		
		
		this.records = newRecords;
		
	}
	
	
	
	public void addRecord(Record r){
		if (r != null) {
			this.records.add(r);
		}
	}
	public List<Record> getRecords() {
		return records;
	}
	public void setRecords(List<Record> records) {
		this.records = records;
	}
	public String getTimestampAsString() {
		if (records.isEmpty()){
			return "";
		}
		long time = records.get(0).getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(new Date(time));

	}
	
	
	
}
