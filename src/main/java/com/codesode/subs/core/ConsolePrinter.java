/*
 * Copyright (c) 2016
 * All Rights Reserved @ Codesode
 */

package com.codesode.subs.core;

import java.io.*;

/**
 * Prints content on the console. Basically as a wrapper to System.out class.
 *
 * @author Vijay Shanker Dubey
 */
public class ConsolePrinter {

    private PrintStream console = System.out;

    public void print(String message) {
        console.println(message);
    }
}
