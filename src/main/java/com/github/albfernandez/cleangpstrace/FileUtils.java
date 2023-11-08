package com.github.albfernandez.cleangpstrace;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.GZIPOutputStream;

public final class FileUtils {
	
	private FileUtils() {
		throw new AssertionError("No instances allowed");
	}
	
    public static OutputStream createOutputStream(File outputFile, boolean compress) throws IOException {
    	OutputStream os = new BufferedOutputStream(Files.newOutputStream(outputFile.toPath()));
    	if (compress) {
    		os = new GZIPOutputStream(os);
    	}
    	return os;
	}

}
