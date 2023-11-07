package com.github.albfernandez.cleangpstrace;

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
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

public final class Main {

    private Main() {
        throw new AssertionError("No instances of this class are allowed");
    }

    public static void main(final String[] args) throws Exception {
        Options options = createOptions();

        CommandLineParser parser = new GnuParser();
        CommandLine line = parser.parse(options, args);
        String excludeAreas = line.getOptionValue("exclude-areas");
        String prefix = Objects.toString(line.getOptionValue("prefix"), "s");
        String outputDir = Objects.toString(line.getOptionValue("output-dir"), ".");
        String secondsToSplitParam = Objects.toString(line.getOptionValue("seconds-to-split"), "10");
        String inputFile = line.getArgs()[0];
        boolean skipSimplify = line.hasOption("skip-simplify");
        int secondsToSplit = Integer.parseInt(secondsToSplitParam);
        
        int weekEpoch = Integer.parseInt(line.getOptionValue("gps-week-rollover", "-1"));

        if (weekEpoch >= 0) {
            TimeConverterFactory.setConveter(new WeekNumberRolloverTimeConverter(weekEpoch));
        }
        
        Trace trace = Trace.load(new File(inputFile));

        if (!StringUtils.isBlank(excludeAreas)) {
            RemoveUnwantedZonesFilter remove = new RemoveUnwantedZonesFilter(
                    excludeAreas);
            trace = remove.cleanTrace(trace);
        }

        List<Trace> traces = TraceFilters.splitTrace(trace, secondsToSplit);
        List<Trace> traces2 = new ArrayList<>();
        if (skipSimplify) {
            traces2.addAll(traces);
        } else {
            for (Trace iTrace : traces) {
                traces2.add(TraceFilters.simplify(iTrace));
            }
        }
        store(traces2, new File(outputDir), prefix);
        System.out.println("Done");
    }

    private static void store(final List<Trace> traces2, final File outputDirectory,
            final String prefix) throws Exception {
        for (Trace trace : traces2) {
            exportToNMEA(trace, outputDirectory, prefix);
            exportToGPX(trace, outputDirectory, prefix);
           
        }

    }

    private static void exportToGPX(Trace trace, File outputDirectory,  String prefix)  throws Exception {
        File outputFile = new File(outputDirectory, prefix + trace.getTimestampAsString() + ".gpx");
        GPXExporter exporter = new GPXExporter(trace);
        exporter.export(outputFile);
        
    }

    private static void exportToNMEA(Trace trace, File outputDirectory, String prefix) throws IOException { 
        File outputFile = new File(outputDirectory, prefix
                + trace.getTimestampAsString() + ".txt");
        try (PrintStream ps = new PrintStream(outputFile,
                StandardCharsets.US_ASCII.displayName())) {
            System.out.println("Writing " + outputFile.getName() + " ...");
            for (Record r : trace.getRecords()) {
                 ps.print(r.toNMEAString());                    
            }

        }
        
    }

    @SuppressWarnings("static-access")
    private static Options createOptions() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("skip-simplify")
                .withDescription("Skip simplification of traces").create());
        options.addOption(OptionBuilder.withLongOpt("exclude-areas")
                .withDescription("use FILE (shapefile) to exclude areas")
                .hasArg().withArgName("FILE").create());
        options.addOption(OptionBuilder.withLongOpt("output-dir")
                .withDescription("use DIR to ouput files").hasArg()
                .withArgName("DIR").create());
        options.addOption(OptionBuilder.withLongOpt("prefix")
                .withDescription("use PREFIX for output files").hasArg()
                .withArgName("PREFIX").create());
        options.addOption(OptionBuilder
                .withLongOpt("seconds-to-split")
                .withDescription(
                        " split traces after SECONDS without valid data")
                .hasArg().withArgName("SECONDS").create());
        options.addOption(OptionBuilder
                .withLongOpt("gps-week-rollover")
                .withDescription("GPS week rollover period we're in (-1: current). Try to fix data and 'move' to correct time ")
                .hasArg().withArgName("EPOCH").create());                
        return options;
    }

}
