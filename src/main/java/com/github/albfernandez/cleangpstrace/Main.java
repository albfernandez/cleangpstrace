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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class Main {

	public static void main(String[] args) throws ParseException, IOException {
		Options options = createOptions();
		
		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);
		String excludeAreas = line.getOptionValue("exclude-areas");
		String prefix = StringUtils.defaultString(line.getOptionValue("prefix"), "s");
		String outputDir = StringUtils.defaultString(line.getOptionValue("output-dir"), ".");
		String secondsToSplitParam = StringUtils.defaultString(line.getOptionValue("seconds-to-split"), "10");
		String inputFile = line.getArgs()[0];
		boolean skipSimplify = line.hasOption("skip-simplify");		
		int secondsToSplit = Integer.parseInt(secondsToSplitParam);
		
		
		
		Trace trace = Trace.load(new File(inputFile));		
				
		if (!StringUtils.isBlank(excludeAreas)){
			RemoveUnwantedZonesFilter remove = new RemoveUnwantedZonesFilter(excludeAreas);
			trace = remove.cleanTrace(trace);
		}
		
		List<Trace> traces = TraceFilters.splitTrace(trace, secondsToSplit);
		List<Trace> traces2 = new ArrayList<Trace>();
		if (skipSimplify) {
			traces2.addAll(traces);
		}
		else {
			for (Trace iTrace: traces) {
				traces2.add(TraceFilters.simplify(iTrace));
			}			
		}
		store(traces2, new File(outputDir), prefix);
		System.out.println("Done");
	}
	
	private static void store(List<Trace> traces2, File file, String prefix) throws FileNotFoundException, IOException {
		for (Trace trace: traces2) {
			File outputFile = new File(file, prefix + trace.getTimestampAsString() + ".txt");
			try (PrintStream ps = new PrintStream(new FileOutputStream(outputFile))){
				System.out.println("Writing " + outputFile.getName() + " ...");
				for (Record r: trace.getRecords()){
					for (String line: r.getData()){
						ps.println(line);
					}
				}
				
			}
		}
		
	}

	@SuppressWarnings("static-access")
	private static Options createOptions () {
		Options options = new Options();
		options.addOption(
				OptionBuilder.withLongOpt("skip-simplify")
				.withDescription("Skip simplification of traces" ).create());
		options.addOption(OptionBuilder.withLongOpt( "exclude-areas" )
                .withDescription( "use FILE (shapefile) to exclude areas" )
                .hasArg()
                .withArgName("FILE")
                .create() );
		options.addOption(OptionBuilder.withLongOpt( "output-dir" )
                .withDescription( "use DIR to ouput files" )
                .hasArg()
                .withArgName("DIR")
                .create() );
		options.addOption(OptionBuilder.withLongOpt( "prefix" )
                .withDescription( "use PREFIX for output files" )
                .hasArg()
                .withArgName("PREFIX")
                .create() );
		options.addOption(OptionBuilder.withLongOpt("seconds-to-split")
				.withDescription(" split traces after SECONDS without valid data")
				.hasArg()
				.withArgName("SECONDS")
				.create());
		return options;
	}

}
