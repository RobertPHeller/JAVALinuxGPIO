/****************************************************************************
 *
 *  System        : 
 *  Module        : 
 *  Object Name   : $RCSfile$
 *  Revision      : $Revision$
 *  Date          : $Date$
 *  Author        : $Author$
 *  Created By    : Robert Heller
 *  Created       : Thu Jun 24 07:48:07 2021
 *  Last Modified : <210624.0856>
 *
 *  Description	
 *
 *  Notes
 *
 *  History
 *	
 ****************************************************************************
 *
 *    Copyright (C) 2021  Robert Heller D/B/A Deepwoods Software
 *			51 Locke Hill Road
 *			Wendell, MA 01379-9728
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * 
 *
 ****************************************************************************/

package com.deepsoft;
import java.io.*;
import java.util.*;
import java.lang.*;

public class LinuxGPIO {
    public enum Direction {IN, OUT, HIGH, LOW};
    private static final String EXPORT = "/sys/class/gpio/export";
    private static final String UNEXPORT = "/sys/class/gpio/unexport";
    private static final String DIRECTIONFMT = "/sys/class/gpio/gpio%d/direction";
    private static final String VALUEFMT = "/sys/class/gpio/gpio%d/value";
    private int pinnumber_;
    private Direction direction_;
    public LinuxGPIO(int pinnumber, Direction direction) {
        pinnumber_ = pinnumber;
        direction_ = direction;
        PrintStream exportFp = new PrintStream(new File(EXPORT));
        exportFp.printf("%d\n",pinnumber_);
        exportFp.close();
        dirfile = new File(String.format(DIRECTIONFMT,pinnumber_));
        while (true) {
            if (dirfile.canWrite()) break;
            Thread.sleep(50);
        }
        PrintStream dirFp = new PrintStream(dirfile);
        switch (direction_) {
        case IN:
            dirFp.printf("in\n");
            break;
        case OUT:
            dirFp.printf("out\n");
            break;
        case HIGH:
            dirFp.printf("high\n");
            break;
        case LOW:
            dirFp.printf("low\n");
            break;
        }
        dirFp.close();
    }
    public int Read() {
        FileInputStream in = new FileInputStream(String.format(VALUEFMT,pinnumber_));
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        return( int.ParseInt(d.readLine()) );
    }
    public void Write(int v) {
        out = new PrintStream(new File(String.format(VALUEFMT,pinnumber_)));
        out.printf("%d\n",v);
        out.close();
    }
    public Direction GetDirection() {
        FileInputStream in = new FileInputStream(String.format(DIRECTIONFMT,pinnumber_));
        BufferedReader d = new BufferedReader(new InputStreamReader(in));
        String dir = d.readLine();
        if (dir == "in") {
            return IN;
        } else if (dir == "out") {
            return OUT;
        } else if (dir == "high") {
            return HIGH;
        } else if (dir == "low") {
            return LOW;
        }
    }
}

        
