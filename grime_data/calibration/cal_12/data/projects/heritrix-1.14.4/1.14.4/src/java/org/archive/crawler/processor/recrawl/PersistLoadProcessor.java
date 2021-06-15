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
package org.archive.crawler.processor.recrawl;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;

import st.ata.util.AList;

import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.DatabaseException;

/**
 * Store CrawlURI attributes from latest fetch to persistent storage for
 * consultation by a later recrawl. 
 * 
 * @author gojomo
 * @version $Date: 2006-09-25 20:19:54 +0000 (Mon, 25 Sep 2006) $, $Revision: 4654 $
 */
public class PersistLoadProcessor extends PersistOnlineProcessor {
    private static final long serialVersionUID = -1917169316015093131L;
    private static final Logger logger =
        Logger.getLogger(PersistLoadProcessor.class.getName());
    
    /** file (log) or directory (state/env) from which to preload history **/
    public static final String ATTR_PRELOAD_SOURCE = "preload-source";

    /**
     * Usual constructor
     * 
     * @param name
     */
    public PersistLoadProcessor(String name) {
        super(name, "PersistLoadProcessor. Loads CrawlURI attributes " +
                "from a previous crawl for current consultation.");
        Type e;
        e = addElementToDefinition(new SimpleType(ATTR_PRELOAD_SOURCE,
                "Source for preloaded persist information. This can be " +
                "a URL or path to a persist log, or a path to an old " +
                "state directory.", ""));
        e.setOverrideable(false);
        e.setExpertSetting(false);
    }
    
    @Override
    protected StoredSortedMap<String,AList> initStore() {
        StoredSortedMap<String,AList> historyMap = super.initStore();
        
        // Preload, if a 'preload-source' file-path/URI/dir-path specified
        String preloadSource = 
            (String) getUncheckedAttribute(null, ATTR_PRELOAD_SOURCE);
        if (StringUtils.isNotBlank(preloadSource)) {
            try {
                int count = PersistProcessor.copyPersistSourceToHistoryMap(
                        getController().getDisk(), preloadSource, historyMap);
                logger.info("Loaded deduplication information for " + count + " previously fetched urls from " + preloadSource);
            } catch (IOException ioe) {
                logger.log(Level.WARNING, "Problem loading " + preloadSource + ", proceeding without deduplication! " + ioe);
            } catch(DatabaseException de) {
                logger.log(Level.WARNING, "Problem loading " + preloadSource + ", proceeding without deduplication! " + de);
            }
        }
        return historyMap;
    }

    @Override
    protected void innerProcess(CrawlURI curi) throws InterruptedException {
        if(shouldLoad(curi)) {
            AList prior = (AList) store.get(persistKeyFor(curi));
            if(prior!=null) {
                // merge in keys
                Iterator iter = prior.getKeys();
                curi.getAList().copyKeysFrom(iter, prior, false);
            }
        }
    }
}