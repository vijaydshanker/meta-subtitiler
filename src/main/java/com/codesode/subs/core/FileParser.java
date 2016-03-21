/*
 * Copyright (c) 2016
 * All Rights Reserved @ Codesode
 */

package com.codesode.subs.core;

import com.codesode.subs.data.*;
import org.apache.commons.io.*;
import org.apache.commons.lang.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Parse subtitle file with extension (srt) and prepare object model.
 *
 * @auther Vijay Shanker Dubey
 */
public class FileParser {

    private Pattern timeStampPattern = Pattern.compile(
            "(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2}," + "\\d{3})");

    /**
     * @param sourceFile
     * @return
     * @throws IOException
     */
    public List<Subtitle> loadSubtitles(String sourceFile) throws IOException {

        List<Subtitle> subtitlesContainer = new ArrayList<Subtitle>();

        Iterator<String> iterator = readLines(sourceFile).iterator();

        while (iterator.hasNext()) {
            String line = iterator.next();

            if (StringUtils.isNumeric(line)) {

                //id of the subtitle
                int id = Integer.parseInt(line);

                //next line should be time info
                line = iterator.next();

                String fromTime = "";
                String toTime = "";
                Matcher matcher = timeStampPattern.matcher(line);
                if (matcher.find()) {
                    fromTime = matcher.group(1);
                    toTime = matcher.group(2);
                    //move to next line
                    line = iterator.next();
                }

                //till the next empty line should be subtitle text
                String text = "";
                while (StringUtils.isNotBlank(line)) {
                    text = text.concat(line) + "\n";
                    //move to next line
                    line = iterator.next();
                }

                Subtitle subtitle = new Subtitle(id, fromTime, toTime, text);
                subtitlesContainer.add(subtitle);
            }

        }

        return subtitlesContainer;
    }

    private List<String> readLines(String sourceFile) throws IOException {

        File file = new File(sourceFile);

        FileReader reader = new FileReader(file);

        List<String> lines = IOUtils.readLines(reader);

        return lines;
    }
}
