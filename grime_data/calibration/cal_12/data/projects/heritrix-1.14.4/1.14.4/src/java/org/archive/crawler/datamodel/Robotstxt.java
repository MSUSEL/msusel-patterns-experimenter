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
package org.archive.crawler.datamodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing and representing 'robots.txt' format 
 * directives, into a list of named user-agents and map from user-agents 
 * to RobotsDirectives. 
 */
public class Robotstxt implements Serializable {
    static final long serialVersionUID = 7025386509301303890L;
    
    // all user agents contained in this robots.txt
    // may be thinned of irrelevant entries
    LinkedList<String> userAgents = new LinkedList<String>();
    // map user-agents to directives
    Map<String,RobotsDirectives> agentsToDirectives = 
        new HashMap<String,RobotsDirectives>();
    // 
    boolean hasErrors = false;
    
    static RobotsDirectives NO_DIRECTIVES = new RobotsDirectives();
    
    public Robotstxt(BufferedReader reader) throws IOException {
        String read;
        // current is the disallowed paths for the preceding User-Agent(s)
        RobotsDirectives current = null;
        // whether a non-'User-Agent' directive has been encountered
        boolean hasDirectivesYet = false; 
        String catchall = null;
        while (reader != null) {
            do {
                read = reader.readLine();
                // Skip comments & blanks
            } while ((read != null) && ((read = read.trim()).startsWith("#") ||
                read.length() == 0));
            if (read == null) {
                reader.close();
                reader = null;
            } else {
                // remove any html markup
                read = read.replaceAll("<[^>]+>","");
                int commentIndex = read.indexOf("#");
                if (commentIndex > -1) {
                    // Strip trailing comment
                    read = read.substring(0, commentIndex);
                }
                read = read.trim();
                if (read.matches("(?i)^User-agent:.*")) {
                    String ua = read.substring(11).trim().toLowerCase();
                    if (current == null || hasDirectivesYet ) {
                        // only create new rules-list if necessary
                        // otherwise share with previous user-agent
                        current = new RobotsDirectives();
                        hasDirectivesYet = false; 
                    }
                    if (ua.equals("*")) {
                        ua = "";
                        catchall = ua;
                    } else {
                        userAgents.addLast(ua);
                    }
                    agentsToDirectives.put(ua, current);
                    continue;
                }
                if (read.matches("(?i)Disallow:.*")) {
                    if (current == null) {
                        // buggy robots.txt
                        hasErrors = true;
                        continue;
                    }
                    String path = read.substring(9).trim();
                    // tolerate common error of ending path with '*' character
                    // (not allowed by original spec; redundant but harmless with 
                    // Google's wildcarding extensions -- which we don't yet fully
                    // support). 
                    if(path.endsWith("*")) {
                        path = path.substring(0,path.length()-1); 
                    }
                    current.addDisallow(path);
                    hasDirectivesYet = true; 
                    continue;
                }
                if (read.matches("(?i)Crawl-delay:.*")) {
                    if (current == null) {
                        // buggy robots.txt
                        hasErrors = true;
                        continue;
                    }
                    // consider a crawl-delay, even though we don't 
                    // yet understand it, as sufficient to end a 
                    // grouping of User-Agent lines
                    hasDirectivesYet = true;
                    String val = read.substring(12).trim();
                    val = val.split("[^\\d\\.]+")[0];
                    try {
                        current.setCrawlDelay(Float.parseFloat(val));
                    } catch (NumberFormatException nfe) {
                        // ignore
                    }
                    continue;
                }
                if (read.matches("(?i)Allow:.*")) {
                    if (current == null) {
                        // buggy robots.txt
                        hasErrors = true;
                        continue;
                    }
                    String path = read.substring(6).trim();
                    // tolerate common error of ending path with '*' character
                    // (not allowed by original spec; redundant but harmless with 
                    // Google's wildcarding extensions -- which we don't yet fully
                    // support). 
                    if(path.endsWith("*")) {
                        path = path.substring(0,path.length()-1); 
                    }
                    current.addAllow(path);
                    hasDirectivesYet = true;
                    continue;
                }
                // unknown line; do nothing for now
            }
        }

        if (catchall != null) {
            userAgents.addLast(catchall);
        }
    }


    /**
     * Does this policy effectively allow everything? (No 
     * disallows or timing (crawl-delay) directives?)
     * @return
     */
    public boolean allowsAll() {
        // TODO: refine so directives that are all empty are also 
        // recognized as allowing all
        return agentsToDirectives.isEmpty();
    }
    
    public List<String> getUserAgents() {
        return userAgents;
    }

    public RobotsDirectives getDirectivesFor(String ua) {
        // find matching ua
        for(String uaListed : userAgents) {
            if(ua.indexOf(uaListed)>-1) {
                return agentsToDirectives.get(uaListed);
            }
        }
        // no applicable user-agents, so empty directives
        return NO_DIRECTIVES; 
    }
}
