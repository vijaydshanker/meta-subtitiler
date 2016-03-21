/**
 * All Rights Reserved
 */

package com.codesode.subs.data;

/**
 * Represents subtitle from a file
 *
 * @auther Vijay Shanker Dubey
 */
public class Subtitle {

    /** */
    private int id;

    /** */
    private String fromTime;

    /** */
    private String toTime;

    /** */
    private String text;

    public Subtitle(int id, String fromTime, String toTime, String text) {
        this.id = id;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public Subtitle setId(int id) {
        this.id = id;
        return this;
    }

    public String getFromTime() {
        return fromTime;
    }

    public Subtitle setFromTime(String fromTime) {
        this.fromTime = fromTime;
        return this;
    }

    public String getToTime() {
        return toTime;
    }

    public Subtitle setToTime(String toTime) {
        this.toTime = toTime;
        return this;
    }

    public String getText() {
        return text;
    }

    public Subtitle setText(String text) {
        this.text = text;
        return this;
    }
}
