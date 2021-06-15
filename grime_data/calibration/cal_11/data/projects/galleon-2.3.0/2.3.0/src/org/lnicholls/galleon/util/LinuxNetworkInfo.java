/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.lnicholls.galleon.util;

/*
 * Original source from: http://forum.java.sun.com/thread.jsp?forum=4&thread=245711
 */

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class LinuxNetworkInfo extends NetInfo {
	private static final Logger log = Logger.getLogger(LinuxNetworkInfo.class.getName());
	
	public static final String IPCONFIG_COMMAND = "ifconfig";
    
    public LinuxNetworkInfo(String address)
    {
    	super(address);
    }

    public void parse() {
        String ipConfigResponse = null;
        try {
            ipConfigResponse = runConsoleCommand(IPCONFIG_COMMAND);
        } catch (IOException e) {
        }
        
        String response = "Link encap 10Mbps Ethernet  HWaddr 00:00:C0:90:B3:42\n"
        	+ "inet addr 192.168.0.3 Bcast 172.16.1.255 Mask 255.255.255.1\n"
        	+ "UP BROADCAST RUNNING  MTU 1500  Metric 0\n"
        	+ "RX packets 3136 errors 217 dropped 7 overrun 26\n"
        	+ "TX packets 1752 errors 25 dropped 0 overrun 0";

        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(ipConfigResponse, "\n");
        //java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(response, "\n");
        String address = null;
        
		Pattern macPattern = Pattern.compile("HWaddr (.*)");
		Pattern subnetPattern = Pattern.compile("inet addr (.*) Bcast (.*) Mask (.*)");
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken().trim();
            
            Matcher m = macPattern.matcher(line);
    		if (m.find() && m.groupCount() == 1) {
    			mPhysicalAddress = m.group(1).trim();
    		}            
    		else
    		{
	    		m = subnetPattern.matcher(line);
	    		if (m.find() && m.groupCount() == 3) {
	    			if (m.group(1).trim().equals(getAddress()))
	    			{
	    				address = m.group(1).trim();
	    				mSubnetMask = m.group(3).trim();
	    			}
	    		}
    		}
            
            if (address!=null && mPhysicalAddress!=null && mSubnetMask!=null)
            {
            	if (address.equals(getAddress()))
                {
                	break;
                }
            }
        }        
    }
}