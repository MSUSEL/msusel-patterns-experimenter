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
package org.archive.crawler.settings;

import java.io.File;

import javax.management.Attribute;

import org.archive.crawler.datamodel.CrawlOrder;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.ServerCache;
import org.archive.crawler.settings.Constraint.FailedCheck;
import org.archive.net.UURIFactory;
import org.archive.util.TmpDirTestCase;

/** Set up a couple of settings to test different functions of the settings
 * framework.
 *
 * @author John Erik Halse
 */
public abstract class SettingsFrameworkTestCase extends TmpDirTestCase implements
        ValueErrorHandler {
    private File orderFile;
    private File settingsDir;
    private CrawlerSettings globalSettings;
    private CrawlerSettings perDomainSettings;
    private CrawlerSettings perHostSettings;
    protected XMLSettingsHandler settingsHandler;
    private CrawlURI unMatchedURI;
    private CrawlURI matchDomainURI;
    private CrawlURI matchHostURI;

    /*
     * @see TmpDirTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        cleanUpOldFiles("SETTINGS"); // preemptive cleanup just in case
        orderFile = new File(getTmpDir(), "SETTINGS_order.xml");
        String settingsDirName = "SETTINGS_per_host_settings";
        settingsDir = new File(orderFile, settingsDirName);
        settingsHandler = new XMLSettingsHandler(orderFile);
        settingsHandler.initialize();
        settingsHandler.getOrder().setAttribute(
          new Attribute(CrawlOrder.ATTR_SETTINGS_DIRECTORY, settingsDirName));

        globalSettings = settingsHandler.getSettingsObject(null);
        perDomainSettings = settingsHandler.getOrCreateSettingsObject("archive.org");
        perHostSettings = settingsHandler.getOrCreateSettingsObject("www.archive.org");

        new ServerCache(getSettingsHandler());

        unMatchedURI = new CrawlURI(
            UURIFactory.getInstance("http://localhost.com/index.html"));

        matchDomainURI = new CrawlURI(
            UURIFactory.getInstance("http://audio.archive.org/index.html"));

        matchHostURI = new CrawlURI(
            UURIFactory.getInstance("http://www.archive.org/index.html"));

        // Write legit email and url so we avoid warnings if tests are reading
        // and writing order files.
        MapType httpHeaders = (MapType)globalSettings.
            getModule(CrawlOrder.ATTR_NAME).
                getAttribute(CrawlOrder.ATTR_HTTP_HEADERS);
        httpHeaders.setAttribute(globalSettings,
            new Attribute(CrawlOrder.ATTR_USER_AGENT,
                "unittest (+http://testing.one.two.three)"));
        httpHeaders.setAttribute(globalSettings,
                new Attribute(CrawlOrder.ATTR_FROM,
                    "unittestingtesting@one.two.three"));
    }

    /*
     * @see TmpDirTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        cleanUpOldFiles("SETTINGS");
    }

    /**
     * @return global settings
     */
    public CrawlerSettings getGlobalSettings() {
        return globalSettings;
    }

    /**
     * @return per domain settings
     */
    public CrawlerSettings getPerDomainSettings() {
        return perDomainSettings;
    }

    /**
     * @return per host settings
     */
    public CrawlerSettings getPerHostSettings() {
        return perHostSettings;
    }

    /**
     * @return settings handler
     */
    public XMLSettingsHandler getSettingsHandler() {
        return settingsHandler;
    }

    /**
     * @return the order file
     */
    public File getOrderFile() {
        return orderFile;
    }

    /**
     * @return the settings directory
     */
    public File getSettingsDir() {
        return settingsDir;
    }

    /**
     * @return a uri matching the domain settings
     */
    public CrawlURI getMatchDomainURI() {
        return matchDomainURI;
    }

    /**
     * @return a uri matching the per host settings
     */
    public CrawlURI getMatchHostURI() {
        return matchHostURI;
    }

    /**
     * @return a uri that doesn't match any settings object except globals.
     */
    public CrawlURI getUnMatchedURI() {
        return unMatchedURI;
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.ValueErrorHandler#handleValueError(org.archive.crawler.settings.Constraint.FailedCheck)
     */
    public void handleValueError(FailedCheck error) {
        // TODO Auto-generated method stub
    }

}
