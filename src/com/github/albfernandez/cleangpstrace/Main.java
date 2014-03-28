package com.github.albfernandez.cleangpstrace;

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
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class Main {

	public static void main(String[] args) throws ParseException, IOException {
		Options options = createOptions();
		// TODO Auto-generated method stub
		CommandLineParser parser = new GnuParser();
		CommandLine line = parser.parse(options, args);
		String excludeAreas = line.getOptionValue("exclude-areas");
		String prefix = StringUtils.defaultString(line.getOptionValue("prefix"), "s");
		String outputDir = StringUtils.defaultString(line.getOptionValue("output-dir"), ".");
		String inputFile = line.getArgs()[0];
		//System.out.println("excludeAreas="+excludeAreas);
		
		
		
		Trace trace = Trace.load(new File(inputFile));		
				
		if (!StringUtils.isBlank(excludeAreas)){
			RemoveUnwantedZonesFilter remove = new RemoveUnwantedZonesFilter(excludeAreas);
			trace = remove.cleanTrace(trace);
		}
		
		List<Trace> traces = TraceFilters.splitTrace(trace, 10);
		List<Trace> traces2 = new ArrayList<Trace>();
		if (!line.hasOption("skip-simplify")) {
			for (Trace iTrace: traces) {
				traces2.add(TraceFilters.simplify(iTrace));
			}
		}
		else {
			traces2.addAll(traces);
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
		options.addOption(new Option( "skip-simplify", "Skip simplification of traces" ));
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
		return options;
	}

}
