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
package org.ganttproject.impex.htmlpdf.fonts;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import net.sourceforge.ganttproject.GPLogger;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author bard Date: 07.01.2004
 */
public class FontMetricsStorage {
    public URI getFontMetricsURI(TTFFileExt ttfFile) {
        URI result = null;
        String fontName = ttfFile.getFile().getName();
        String resourceName = "font-metrics/" + fontName + ".xml";
        URL resourceUrl = getClass().getClassLoader().getResource(resourceName);

        try {
            result = resourceUrl == null ? 
            		null : 
            		new URI(URLEncoder.encode(resourceUrl.toString(), "utf-8"));
        } catch (URISyntaxException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
        } catch (UnsupportedEncodingException e) {
        	if (!GPLogger.log(e)) {
        		e.printStackTrace(System.err);
        	}
		}
        return result;
    }
}
