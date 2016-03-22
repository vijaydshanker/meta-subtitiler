/*
 * Copyright (c) 2016
 * All Rights Reserved @ Codesode
 */

/**
 * All Rights Reserved
 */
package com.codesode.subs.tool;


import com.codesode.subs.core.*;
import com.codesode.subs.data.*;
import org.apache.commons.io.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;

/**
 * @author Vijay Shanker Dubey
 */
public class SyncSubtitle {

    private List<Subtitle> subtitleLines;

    public SyncSubtitle(File fileName) throws IOException {
        this.subtitleLines = new FileParser().loadSubtitles(fileName);
    }

    public SyncSubtitle addSeconds(int secondsToAdd) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

        for (Subtitle subtitle : this.subtitleLines) {

            TemporalAccessor fromTimeAccessor = formatter.parse(subtitle.getFromTime());
            String newFromTime = LocalTime.from(fromTimeAccessor).plusSeconds(secondsToAdd).format(formatter);

            TemporalAccessor toTimeAccessor = formatter.parse(subtitle.getToTime());
            String newToTime = LocalTime.from(toTimeAccessor).plusSeconds(secondsToAdd).format(formatter);

            //update subtitle
            subtitle.setFromTime(newFromTime).setToTime(newToTime);
        }

        return this;
    }

    public void writeTo(File file) throws IOException {

        //make sure file exists or new file is created
        FileUtils.touch(file);

        List<String> lines = new LinkedList<>();

        for (Subtitle subtitle : this.subtitleLines) {
            lines.add(String.valueOf(subtitle.getId()));
            lines.add(subtitle.getFromTime() + " --> " + subtitle.getToTime());
            lines.add(subtitle.getText());
        }

        FileUtils.writeLines(file, lines);
    }
}