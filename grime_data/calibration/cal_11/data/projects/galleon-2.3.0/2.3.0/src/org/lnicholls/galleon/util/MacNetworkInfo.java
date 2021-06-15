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

public class MacNetworkInfo extends NetInfo {
	private static final Logger log = Logger.getLogger(MacNetworkInfo.class.getName());
	
    public static final String IPCONFIG_COMMAND = "ifconfig";
    
    public MacNetworkInfo(String address)
    {
    	super(address);
    }

    public void parse() {
        String ipConfigResponse = null;
        try {
            ipConfigResponse = runConsoleCommand(IPCONFIG_COMMAND);
        } catch (IOException e) {
        }
        
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(ipConfigResponse, "\n");
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