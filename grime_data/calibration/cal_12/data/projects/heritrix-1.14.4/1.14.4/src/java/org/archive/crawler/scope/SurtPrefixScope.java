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
package org.archive.crawler.scope;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.deciderules.DecidingScope;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;
import org.archive.util.SurtPrefixSet;

/**
 * A specialized CrawlScope suitable for the most common crawl needs.
 * 
 * Roughly, as with other existing CrawlScope variants, SurtPrefixScope's logic
 * is that a URI is included if:
 * <pre>
 *  ( isSeed(uri) || focusFilter.accepts(uri) ) ||
 *     transitiveFilter.accepts(uri) ) && ! excludeFilter.accepts(uri)
 * </pre>
 * Specifically, SurtPrefixScope uses a SurtFilter to test for focus-inclusion.
 * 
 * @author gojomo
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingScope}.
 */
public class SurtPrefixScope extends RefinedScope {

    private static final long serialVersionUID = 2652008287322770123L;

    public static final String ATTR_SURTS_SOURCE_FILE = "surts-source-file";
    public static final String ATTR_SEEDS_AS_SURT_PREFIXES = "seeds-as-surt-prefixes";
    public static final String ATTR_SURTS_DUMP_FILE = "surts-dump-file";
    
    private static final Boolean DEFAULT_SEEDS_AS_SURT_PREFIXES = new Boolean(true);

    /**
     * Whether the 'via' of CrawlURIs should also be checked
     * to see if it is prefixed by the set of SURT prefixes
     */
    public static final String 
        ATTR_ALSO_CHECK_VIA = "also-check-via";
    public static final Boolean
        DEFAULT_ALSO_CHECK_VIA = Boolean.FALSE;
    
    SurtPrefixSet surtPrefixes = null;

    public SurtPrefixScope(String name) {
        super(name);
        setDescription(
                "SurtPrefixScope: A scope for crawls limited to regions of " +
                "the web defined by a set of SURT prefixes *Deprecated* " +
                "Use DecidingScope instead. (The SURT form of " +
                "a URI has its hostname reordered to ease sorting and "
                + "grouping by domain hierarchies.)");
        addElementToDefinition(
                new SimpleType(ATTR_SURTS_SOURCE_FILE, 
                		"Source file from which to infer SURT prefixes. Any URLs " +
                        "in file will be converted to the implied SURT prefix, and " +
                        "literal SURT prefixes may be listed on lines beginning " +
                        "with a '+' character.", 
                        ""));
        addElementToDefinition(
                new SimpleType(ATTR_SEEDS_AS_SURT_PREFIXES, 
                        "Should seeds also be interpreted as SURT prefixes.", 
                        DEFAULT_SEEDS_AS_SURT_PREFIXES));
        
        Type t = addElementToDefinition(
                new SimpleType(ATTR_SURTS_DUMP_FILE, 
                        "Dump file to save SURT prefixes actually used.", 
                        ""));
        t.setExpertSetting(true);
        t = addElementToDefinition(new SimpleType(ATTR_ALSO_CHECK_VIA,
                "Whether to also rule URI in-scope if a " +
                "URI's 'via' URI (the URI from which it was discovered) " +
                "in SURT form begins with any of the established prefixes. " +
                "For example, can be used to accept URIs that are 'one hop " +
                "off' URIs fitting the SURT prefixes. Default is false.",
                DEFAULT_ALSO_CHECK_VIA));
        t.setOverrideable(false);
        t.setExpertSetting(true);

    }

    
    /* (non-Javadoc)
     * @see org.archive.crawler.framework.CrawlScope#initialize(org.archive.crawler.framework.CrawlController)
     */
    public void initialize(CrawlController controller) {
        super.initialize(controller);
        readPrefixes();
    }
    
    /**
     * Check if a URI is part of this scope.
     * 
     * @param object
     *            An instance of UURI or of CandidateURI.
     * @return True if focus filter accepts passed object.
     */
    protected synchronized boolean focusAccepts(Object object) {
        // TODO: eliminate duplication wrt/SurtPrefixedDecideRule.evaluate
        if (surtPrefixes == null) {
            readPrefixes();
        }
        if ( (object instanceof CandidateURI) && 
                ((Boolean) getUncheckedAttribute(null, ATTR_ALSO_CHECK_VIA))
                    .booleanValue()) {
            if(focusAccepts(((CandidateURI)object).getVia())) {
                return true;
            }
        }
        String candidateSurt = SurtPrefixSet.getCandidateSurt(object);
        if(candidateSurt == null) {
            return false; 
        }
        return surtPrefixes.containsPrefixOf(candidateSurt);
    }
    
    private void readPrefixes() {
        surtPrefixes = new SurtPrefixSet(); 
        FileReader fr = null;
        
        // read SURTs from file, if appropriate 
        String sourcePath = (String) getUncheckedAttribute(null,
                ATTR_SURTS_SOURCE_FILE);
        if(sourcePath.length()>0) {
            File source = new File(sourcePath);
            if (!source.isAbsolute()) {
                source = new File(getSettingsHandler().getOrder()
                        .getController().getDisk(), sourcePath);
            }
            try {
                fr = new FileReader(source);
                try {
                    surtPrefixes.importFromMixed(fr,true);
                } finally {
                    fr.close();
                }
        
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } 
        }
        
        // interpret seeds as surts, if appropriate
        boolean deduceFromSeeds = 
            ((Boolean) getUncheckedAttribute(null, ATTR_SEEDS_AS_SURT_PREFIXES))
            .booleanValue();
        try {
            fr = new FileReader(getSeedfile());
            try {
                surtPrefixes.importFromMixed(fr,deduceFromSeeds);
            } finally {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }  

        // dump surts to file, if appropriate
        String dumpPath = (String) getUncheckedAttribute(null,
                ATTR_SURTS_DUMP_FILE);
        if(dumpPath.length()>0) {
            File dump = new File(dumpPath);
            if (!dump.isAbsolute()) {
                dump = new File(getSettingsHandler().getOrder()
                        .getController().getDisk(), dumpPath);
            }
            try {
                OutputStreamWriter fw = new OutputStreamWriter(
                        new FileOutputStream(dump),"UTF-8");
                try {
                    surtPrefixes.exportTo(fw);
                } finally {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Re-read prefixes after an update. 
     * 
     * @see org.archive.crawler.framework.CrawlScope#kickUpdate()
     */
    public synchronized void kickUpdate() {
        super.kickUpdate();
        // TODO: make conditional on file having actually changed,
        // perhaps by remembering mod-time
        readPrefixes();
    }
}
