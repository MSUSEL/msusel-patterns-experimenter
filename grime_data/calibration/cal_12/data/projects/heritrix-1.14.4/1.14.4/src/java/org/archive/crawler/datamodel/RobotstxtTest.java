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
import java.io.StringReader;

import junit.framework.TestCase;

public class RobotstxtTest extends TestCase {
    public void testParseRobots() throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader("BLAH"));
        Robotstxt r = new Robotstxt(reader);
        assertFalse(r.hasErrors);
        assertTrue(r.getUserAgents().size() == 0);
        // Parse archive robots.txt with heritrix agent.
        String agent = "archive.org_bot";
        reader = new BufferedReader(
            new StringReader("User-agent: " + agent + "\n" +
            "Disallow: /cgi-bin/\n" +
            "Disallow: /details/software\n"));
        r = new Robotstxt(reader);
        assertFalse(r.hasErrors);
        assertTrue(r.getUserAgents().size() == 1);
        assertTrue(r.agentsToDirectives.size() == 1);
        assertEquals(r.getUserAgents().get(0), agent);
        // Parse archive robots.txt with star agent.
        agent = "*";
        reader = new BufferedReader(
            new StringReader("User-agent: " + agent + "\n" +
            "Disallow: /cgi-bin/\n" +
            "Disallow: /details/software\n"));
        r = new Robotstxt(reader);
        assertFalse(r.hasErrors);
        assertTrue(r.getUserAgents().size() == 1);
        assertTrue(r.agentsToDirectives.size() == 1);
        assertEquals(r.getUserAgents().get(0), "");
    }
    
    Robotstxt sampleRobots1() throws IOException {
        BufferedReader reader = new BufferedReader(
            new StringReader(
                "User-agent: *\n" +
                "Disallow: /cgi-bin/\n" +
                "Disallow: /details/software\n" +
                "\n"+
                "User-agent: denybot\n" +
                "Disallow: /\n" +
                "\n"+
                "User-agent: allowbot1\n" +
                "Disallow: \n" +
                "\n"+
                "User-agent: allowbot2\n" +
                "Disallow: /foo\n" +
                "Allow: /\n"+
                "\n"+
                "User-agent: delaybot\n" +
                "Disallow: /\n" +
                "Crawl-Delay: 20\n"+
                "Allow: /images/\n"
            ));
        return new Robotstxt(reader); 
    }
    
    public void testDirectives() throws IOException {
        Robotstxt r = sampleRobots1();
        // bot allowed with empty disallows
        assertTrue(r.getDirectivesFor("Mozilla allowbot1 99.9").allows("/path"));
        assertTrue(r.getDirectivesFor("Mozilla allowbot1 99.9").allows("/"));
        // bot allowed with explicit allow
        assertTrue(r.getDirectivesFor("Mozilla allowbot2 99.9").allows("/path"));
        assertTrue(r.getDirectivesFor("Mozilla allowbot2 99.9").allows("/"));
        assertTrue(r.getDirectivesFor("Mozilla allowbot2 99.9").allows("/foo"));
        // bot denied with blanket deny
        assertFalse(r.getDirectivesFor("Mozilla denybot 99.9").allows("/path"));
        assertFalse(r.getDirectivesFor("Mozilla denybot 99.9").allows("/"));
        // unnamed bot with mixed catchall allow/deny
        assertTrue(r.getDirectivesFor("Mozilla anonbot 99.9").allows("/path"));
        assertFalse(r.getDirectivesFor("Mozilla anonbot 99.9").allows("/cgi-bin/foo.pl"));
        // no crawl-delay
        assertEquals(-1f,r.getDirectivesFor("Mozilla denybot 99.9").getCrawlDelay());
        // with crawl-delay 
        assertEquals(20f,r.getDirectivesFor("Mozilla delaybot 99.9").getCrawlDelay());
    }
    
    Robotstxt htmlMarkupRobots() throws IOException {
        BufferedReader reader = new BufferedReader(
            new StringReader(
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\"><HTML>\n"
                +"<HEAD>\n"
                +"<TITLE>/robots.txt</TITLE>\n"
                +"<HEAD>\n"
                +"<BODY>\n"
                +"User-agent: *<BR>\n"
                +"Disallow: /<BR>\n"
                +"Crawl-Delay: 30<BR>\n"
                +"\n"
                +"</BODY>\n"
                +"</HTML>\n"
            ));
        return new Robotstxt(reader); 
    }
    
    /**
     * Test handling of a robots.txt with extraneous HTML markup
     * @throws IOException
     */
    public void testHtmlMarkupRobots() throws IOException {
        Robotstxt r = htmlMarkupRobots();
        assertFalse(r.getDirectivesFor("anybot").allows("/index.html"));
        assertEquals(30f,r.getDirectivesFor("anybot").getCrawlDelay());
    }
}
