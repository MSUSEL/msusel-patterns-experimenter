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

import junit.framework.TestCase;

/**
 * Tests FilePatternFilter default pattern (all default file extension) and
 * separate subgroups patterns such as images, audio, video, and
 * miscellaneous groups.
 *
 * @author Igor Ranitovic
 * @deprecated  The tested class is deprecated, so this test 
 * will eventually go away
 */
public class FilePatternFilterTest extends TestCase {
        FilePatternFilter filter = new FilePatternFilter("File Pattern Filter");

    /**
     * Tests FilePatternFilter default pattern (all default file extension) and
     * separate subgroups patterns such as images, audio, video, and
     * miscellaneous groups.
     *
     */
    public final void testPatterns() {

       String stringURI = "http://foo.boo/moo.avi";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.bmp";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.doc";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));

       stringURI = "http://foo.boo/moo.gif";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.jpg";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.jpeg";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.mid";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));

       stringURI = "http://foo.boo/moo.mov";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.mp2";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));

       stringURI = "http://foo.boo/moo.mp3";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));

       stringURI = "http://foo.boo/moo.mp4";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));

       stringURI = "http://foo.boo/moo.mpeg";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.pdf";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));

       stringURI = "http://foo.boo/moo.png";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.ppt";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));

       stringURI = "http://foo.boo/moo.ram";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.rm";
       assertTrue(filter.accepts(stringURI));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));

       stringURI = "http://foo.boo/moo.smil";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.swf";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.MISC_PATTERNS));

       stringURI = "http://foo.boo/moo.tif";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.tiff";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.IMAGES_PATTERNS));

       stringURI = "http://foo.boo/moo.wav";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.AUDIO_PATTERNS));

       stringURI = "http://foo.boo/moo.wmv";
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));
       stringURI.toUpperCase();
       assertTrue(filter.accepts(stringURI));
       assertTrue(stringURI.matches(FilePatternFilter.VIDEO_PATTERNS));

       stringURI = "http://foo.boo/moo.asf";
       assertFalse(filter.accepts(stringURI));
       assertFalse(stringURI.matches(FilePatternFilter.MISC_PATTERNS));
       stringURI.toUpperCase();
       assertFalse(filter.accepts(stringURI));
       assertFalse(stringURI.matches(FilePatternFilter.MISC_PATTERNS));

    }
}
