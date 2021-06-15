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
package org.archive.crawler.postprocessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;
import org.archive.util.IoUtils;

/**
 * Processor module which uses 'df -k', where available and with
 * the expected output format (on Linux), to monitor available 
 * disk space and pause the crawl if free space on  monitored 
 * filesystems falls below certain thresholds.
 */
public class LowDiskPauseProcessor extends Processor {

    private static final long serialVersionUID = 3338337700768396302L;

    /**
     * Logger.
     */
    private static final Logger logger =
        Logger.getLogger(LowDiskPauseProcessor.class.getName());

    /**
     * List of mounts to monitor; should match "Mounted on" column of 'df' output
     */
    public static final String ATTR_MONITOR_MOUNTS = "monitor-mounts";
    public static final String DEFAULT_MONITOR_MOUNTS = "";
    
    /**
     * Space available level below which a crawl-pause should be triggered.
     */
    public static final String ATTR_PAUSE_THRESHOLD = "pause-threshold-kb";
    public static final int DEFAULT_PAUSE_THRESHOLD = 500 * 1024; // 500MB
    
    /**
     * Amount of content received between each recheck of free space
     */
    public static final String ATTR_RECHECK_THRESHOLD = "recheck-threshold-kb";
    public static final int DEFAULT_RECHECK_THRESHOLD = 200 * 1024; // 200MB
    
    protected int contentSinceCheck = 0;
    
    public static final Pattern VALID_DF_OUTPUT = 
        Pattern.compile("(?s)^Filesystem\\s+1K-blocks\\s+Used\\s+Available\\s+Use%\\s+Mounted on\\n.*");
    public static final Pattern AVAILABLE_EXTRACTOR = 
        Pattern.compile("(?m)\\s(\\d+)\\s+\\d+%\\s+(\\S+)$");
    
    /**
     * @param name Name of this writer.
     */
    public LowDiskPauseProcessor(String name) {
        super(name, "LowDiskPause processor");
        Type e = addElementToDefinition(
            new SimpleType(ATTR_MONITOR_MOUNTS, 
                    "Space-delimited list of filessystem mounts whose " +
                    "'available' space should be monitored via 'df' " +
                    "(if available).",
                DEFAULT_MONITOR_MOUNTS));
        e.setOverrideable(false);
        e = addElementToDefinition(
            new SimpleType(ATTR_PAUSE_THRESHOLD, 
                    "When available space on any monitored mounts falls " +
                    "below this threshold, the crawl will be paused. ",
                    new Integer(DEFAULT_PAUSE_THRESHOLD)));
        e = addElementToDefinition(
            new SimpleType(ATTR_RECHECK_THRESHOLD, 
                    "Available space via 'df' is rechecked after every " +
                    "increment of this much content (uncompressed) is " +
                    "observed. ",
                    new Integer(DEFAULT_RECHECK_THRESHOLD)));
        e.setOverrideable(false);
    } 
    
    /**
     * Notes a CrawlURI's content size in its running tally. If the 
     * recheck increment of content has passed through since the last
     * available-space check, checks available space and pauses the 
     * crawl if any monitored mounts are below the configured threshold. 
     * 
     * @param curi CrawlURI to process.
     */
    protected void innerProcess(CrawlURI curi) {
        contentSinceCheck += curi.getContentSize();
        synchronized (this) {
            if (contentSinceCheck/1024 > ((Integer) getUncheckedAttribute(null,
                    ATTR_RECHECK_THRESHOLD)).intValue()) {
                checkAvailableSpace(curi);
                contentSinceCheck = 0;
            }
        }
    }


    /**
     * Probe via 'df' to see if monitored mounts have fallen
     * below the pause available threshold. If so, request a 
     * crawl pause. 
     * @param curi Current context.
     */
    private void checkAvailableSpace(CrawlURI curi) {
        try {
            String df = IoUtils.readFullyAsString(Runtime.getRuntime().exec(
                    "df -k").getInputStream());
            Matcher matcher = VALID_DF_OUTPUT.matcher(df);
            if(!matcher.matches()) {
                logger.severe("'df -k' output unacceptable for low-disk checking");
                return;
            }
            List monitoredMounts = Arrays.asList(((String) getUncheckedAttribute(null,
                    ATTR_MONITOR_MOUNTS)).split("\\s*"));
            matcher = AVAILABLE_EXTRACTOR.matcher(df);
            while (matcher.find()) {
                String mount = matcher.group(2);
                if (monitoredMounts.contains(mount)) {
                    long availKilobytes = Long.parseLong(matcher.group(1));
                    int thresholdKilobytes = ((Integer) getUncheckedAttribute(
                            null, ATTR_PAUSE_THRESHOLD)).intValue();
                    if (availKilobytes < thresholdKilobytes ) {
                        getController().requestCrawlPause();
                        logger.log(Level.SEVERE, "Low Disk Pause",
                                availKilobytes + "K available on " + mount
                                        + " (below threshold "
                                        + thresholdKilobytes + "K)");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            curi.addLocalizedError(this.getName(), e,
                    "problem checking available space via 'df'");
        }
    }
}
