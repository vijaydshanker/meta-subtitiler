/*
 * Copyright (c) 2016
 * All Rights Reserved @ Codesode
 */

package com.codesode.subs;

import com.codesode.subs.core.*;
import com.codesode.subs.tool.*;
import org.apache.commons.cli.*;
import org.apache.commons.io.*;

import java.io.*;
import java.util.*;

/**
 * Launches subtitle programs after reading and sanitizing request calls
 *
 * @author Vijay Shanker Dubey
 */
public class CommandLauncher {

    private ConsolePrinter printer;

    private CommandLauncher(ConsolePrinter printer) {
        this.printer = printer;
    }

    public static void main(String[] args) {

        try {
            ConsolePrinter printer = new ConsolePrinter();
            new CommandLauncher(printer).execute(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void execute(String[] args) throws Exception {

        CommandOptions options = new CommandOptions(args);

        if (options.valid()) {

            if (options.helpRequested()) {
                options.writeHelp(null);
            } else {
                List<String> errors = validateRequest(options);
                if (errors != null && errors.size() > 0) {
                    errors.forEach(message -> printer.print(message));
                    options.writeHelp(null);
                } else {
                    processRequest(options);
                }
            }

        } else {
            options.writeHelp(null);
        }
    }

    private void processRequest(CommandOptions options) throws IOException {

        File inputFile = new File(options.inputFileName());
        int adjustTime = options.adjustTime();
        File outputFile = new File(inputFile.getParentFile(), options.outputFileName());

        new SyncSubtitle(inputFile).addSeconds(adjustTime)
                                   .writeTo(outputFile);
    }

    private List<String> validateRequest(CommandOptions options) {
        List<String> errors = new ArrayList<>();

        File inputFile = new File(options.inputFileName());
        if (inputFile.canRead()) {
            if (FilenameUtils.isExtension(inputFile.getName(), "srt")) {
                errors.add(String.format("'%s' is not a valid srt file.", inputFile));
            }
        } else {
            errors.add(String.format("'%s' file is not readable. It should be valid *.srt file.", inputFile));
        }

        String fileName = FilenameUtils.getBaseName(options.outputFileName());
        File outputFile = new File(inputFile.getParentFile(), fileName);
        if (!outputFile.canWrite()) {
            errors.add(String.format("'%s' output file name is not valid.", outputFile));
        }

        if (options.adjustTime() == 0) {
            errors.add(String.format("'%s' time adjustment is not possible.", outputFile));
        }

        return errors;
    }

    private class CommandOptions {

        private Option helpOption = Option.builder("h")
                                          .argName("Help Command")
                                          .desc("How to use.")
                                          .longOpt("help")
                                          .build();

        private Option inputFileOption = Option.builder("i")
                                               .argName("Input file")
                                               .desc("Input subtitle File")
                                               .hasArg(true)
                                               .longOpt("input")
                                               .numberOfArgs(1)
                                               .optionalArg(false)
                                               .required(true)
                                               .build();

        private Option outPutFileOption = Option.builder("o")
                                                .argName("Output file location")
                                                .desc("Time adjusted output file name. File will be created in the "
                                                              + "directory of input file.")
                                                .hasArg(true)
                                                .longOpt("output")
                                                .numberOfArgs(1)
                                                .optionalArg(false)
                                                .required(true)
                                                .build();

        private Option timeAdjustmentOption = Option.builder("a")
                                                    .argName("Time Adjustment")
                                                    .desc("Time(in seconds) to be adjusted. Negative value will " +
                                                                  "adjust " + "substitle time backward")
                                                    .hasArg(true)
                                                    .longOpt("output")
                                                    .numberOfArgs(1)
                                                    .optionalArg(false)
                                                    .required(true)
                                                    .build();

        private Options options;

        private CommandLine cline;

        private CommandOptions(String[] args) {
            options = new Options().addOption(helpOption)
                                   .addOption(inputFileOption)
                                   .addOption(outPutFileOption)
                                   .addOption(timeAdjustmentOption);
            make(args);
        }

        private void make(String[] args) {
            CommandLineParser lineParser = new DefaultParser();
            try {
                cline = lineParser.parse(options, args);
            } catch (ParseException e) {
                writeHelp(e.getMessage());
            }
        }

        private void writeHelp(String headerMessage) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("meta-subtitle", headerMessage, options, null);
        }

        private boolean valid() {
            return cline != null;
        }

        private String inputFileName() {
            return cline.getOptionValue(inputFileOption.getOpt());
        }

        private String outputFileName() {
            return cline.getOptionValue(outPutFileOption.getOpt());
        }

        private int adjustTime() {
            return Integer.valueOf(cline.getOptionValue(timeAdjustmentOption.getOpt()));
        }

        private boolean helpRequested() {
            return cline.hasOption(helpOption.getOpt());
        }
    }
}
