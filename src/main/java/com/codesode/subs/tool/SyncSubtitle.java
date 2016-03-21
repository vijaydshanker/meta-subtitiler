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

    public SyncSubtitle(String fileName) throws IOException {
        this.subtitleLines = new FileParser().loadSubtitles(fileName);
    }

    public static void main(String[] args) {
        String fileName = "C:\\Users\\HCL\\Downloads\\the.hateful.eight.(2015).eng.1cd.(6456389)\\The.Hateful.Eight" +
                ".2015.DVDScr.XVID.AC3.HQ.Hive-CM8.srt";

        try {
            new SyncSubtitle(fileName).addSeconds(-1).writeTo(new File("test.srt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SyncSubtitle addSeconds(int secondsToAdd) {

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

    private void writeTo(File file) throws IOException {

        //make sure file exists or new file is created
        FileUtils.touch(file);

        List<String> lines = new LinkedList<String>();

        for (Subtitle subtitle : this.subtitleLines) {
            lines.add(String.valueOf(subtitle.getId()));
            lines.add(subtitle.getFromTime() + " --> " + subtitle.getToTime());
            lines.add(subtitle.getText());
        }

        FileUtils.writeLines(file, lines);
    }
}