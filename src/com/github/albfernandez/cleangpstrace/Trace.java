package com.github.albfernandez.cleangpstrace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Trace {
	private List<Record> records = new ArrayList<>();
	private Trace() {
		super();
	}
	public static Trace load(File file) throws IOException {
		try (Reader reader = new FileReader(file)){
			return load(reader);
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
//				write(line);
			}
		}
		// TODO
		return trace;
	}
	
	private void addRecord(Record r){
		if (r != null) {
			this.records.add(r);
		}
	}
	
}
