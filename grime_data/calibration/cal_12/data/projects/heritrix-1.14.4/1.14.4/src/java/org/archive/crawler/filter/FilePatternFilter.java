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
package org.archive.crawler.filter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;

import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.settings.ComplexType;
import org.archive.crawler.settings.MapType;
import org.archive.crawler.settings.SimpleType;

/**
 * Compares suffix of a passed CrawlURI, UURI, or String against a regular
 * expression pattern accepting matches.
 *
 * @author Igor Ranitovic
 * @deprecated As of release 1.10.0.  Replaced by
 * {@link MatchesFilePatternDecideRule}.
 */
public class FilePatternFilter extends URIRegExpFilter {

    private static final long serialVersionUID = -4019256104085004651L;

    private static final Logger logger =
        Logger.getLogger(FilePatternFilter.class.getName());
    public static final String ATTR_USE_DEFAULT = "use-default-patterns";
    public static final String IMAGES_PATTERNS = ".*(?i)(\\.(bmp|gif|jpe?g" +
        "|png|tiff?))$";
    public static final String AUDIO_PATTERNS = ".*(?i)(\\.(mid|mp2|mp3|mp4" +
        "|wav))$";
    public static final String VIDEO_PATTERNS = ".*(?i)(\\.(avi|mov|mpeg|ram" +
        "|rm|smil|wmv))$";
    public static final String MISC_PATTERNS = ".*(?i)(\\.(doc|pdf|ppt|swf))$";
    public static final String ALL_DEFAULT_PATTERNS = ".*(?i)(\\.(bmp|gif" +
        "|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|rm|smil|wmv" +
        "|doc|pdf|ppt|swf))$";

    public static final String ALL = "All";
    public static final String IMAGES = "Images";
    public static final String AUDIO = "Audio";
    public static final String VIDEO = "Video";
    public static final String MISC = "Miscellaneous";
    public static final String CUSTOM = "Custom";

    /**
     * @param name
     */
    public FilePatternFilter(String name) {
        super(name);
        setDescription("A URI path suffix filter *Deprecated* Use" +
        		"DecidingFilter and MatchesFilePatternDecideRule instead. " +
            "All URLs that end with the specified pattern(s) will be added " +
            "to the scope's focus. Default file patterns are:\n.avi, .bmp, " +
            ".doc, .gif, .jp(e)g, .mid, .mov, .mp2, .mp3, .mp4, .mpeg, " +
            ".pdf, .png, .ppt, .ram, .rm,.smil, .swf, .tif(f), .wav, .wmv\n" +
            "It is also possible to specifiy custom regular expressions " +
            "for this filter, turning it into (effectively) a generic " +
            "regular expression filter.");

        String[] options = new String[] {ALL, IMAGES, AUDIO, VIDEO, MISC,
                                            CUSTOM};

        addElementToDefinition(
            new SimpleType(ATTR_USE_DEFAULT, "URLs that match selected file " +
                "patterns will be crawled. Default file patterns are:\n" +
                "Images: .bmp, .gif, .jp(e)g, .png, .tif(f)\nAudio: .mid, " +
                ".mp2, .mp3, .mp4, .wav\nVideo: .avi, .mov, .mpeg, .ram, " +
                ".rm, .smil, .wmv\nMiscellaneous: .doc, .pdf, .ppt, .swf\n" +
                "All: All above patterns\nChoose 'Custom' to specify your own" +
                " pattern. These default patterns are case insensitive.",
                "All", options));

        addElementToDefinition(
            new SimpleType(ATTR_REGEXP, "Custom java regular expression.+n " +
                    "This regular expression will be used instead of the " +
                    "supplied pattern groups for matching.\nAn example " +
                    "of such a regular expression (Miscellaneous):\n" +
                    ".*(?i)(\\.(doc|pdf|ppt|swf))$\n" +
                    "Any arbitrary reg.expr. is valid though and will be " +
                    "applied to the URI.", ""));


    }

    /**
     * @see org.archive.crawler.filter.URIRegExpFilter#getRegexp(java.lang.Object)
     */
    protected String getRegexp(Object o) {
        try {
            String patternType = (String)getAttribute(o, ATTR_USE_DEFAULT);

            if (patternType.equals(ALL)) {
                return ALL_DEFAULT_PATTERNS;
            } else if (patternType.equals(IMAGES)) {
                return IMAGES_PATTERNS;
            }else if (patternType.equals(AUDIO)) {
                return AUDIO_PATTERNS;
            }else if(patternType.equals(VIDEO)) {
                return VIDEO_PATTERNS;
            }else if(patternType.equals(MISC)) {
                return MISC_PATTERNS;
            }else if(patternType.equals(CUSTOM)) {
                return (String) getAttribute(o, ATTR_REGEXP);
            }else {
                assert false : "Unrecognized pattern type " + patternType +
                               ". Should never happened!";
            }

        } catch (AttributeNotFoundException e) {
            logger.log(Level.SEVERE,"necessary setting missing",e);
        }
        // Basically the filter is inactive if this occurs (The caller
        // returns 'false' when regexp is null).
        return null;  
    }

    /**
     * @see org.archive.crawler.framework.Filter#accepts(java.lang.Object)
     */
    public boolean accepts(Object o) {
        CrawlURI curi = (o instanceof CrawlURI) ? (CrawlURI) o : null;

        // Skip the evaluation if the filter is disabled.
        // Since this filter is primarily used with seed and focus filters
        // it has to return false when disabled -- unlike Filter's accepts
        // method.
        try {
            if (!((Boolean) getAttribute(ATTR_ENABLED, curi)).booleanValue()) {
                return false;
            }
        } catch (AttributeNotFoundException e) {
            logger.severe(e.getMessage());
        }

        boolean accept = returnTrueIfMatches(curi) == innerAccepts(o);

        if (accept && logger.isLoggable(Level.FINEST)) {
            // Log if filter returns true
            ComplexType p = this.getParent();
            if (p instanceof MapType) {
                p = p.getParent();
            }
            String msg = this.toString() + " belonging to " + p.toString()
                         + " accepted " + o.toString();
            logger.finest(msg);
        }

        return accept;
    }

}
