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
 * Copyright (C) 2005 Leon Nicholls
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * 
 * See the file "COPYING" for more details.
 */

import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import org.apache.log4j.Logger;

import com.yahoo.search.ImageSearchRequest;
import com.yahoo.search.ImageSearchResult;
import com.yahoo.search.ImageSearchResults;
import com.yahoo.search.SearchClient;

public class Yahoo {

    private static final Logger log = Logger.getLogger(Yahoo.class.getName());

    /*
     * The following is required by the Yahoo.com search developer network. DO NOT USE THESE WITH ANY OTHER PROJECT
     * SINCE THEY HAVE BEEN REGISTERED WITH YAHOO.COM FOR THE GALLEON PROJECT. Obtain your own key by registering at:
     * http://developer.yahoo.net/
     */

    private final static String APPLICATION_ID = "galleonhme";

    private final static int DAILY_SEARCH_LIMIT = 5000;

    public static List getImages(String key) {
        ArrayList results = new ArrayList();
        if ((System.currentTimeMillis() - mTime < 1000 * 60 * 60 * 24)) {
            if (mCounter <= 0) {
                // Not allowed to exceed daily limit
                log.error("Exceeded daily search limit for: " + key);
                return results;
            }
        } else {
            mTime = System.currentTimeMillis();
            mCounter = DAILY_SEARCH_LIMIT;
        }

        mCounter--;

        try {
            SearchClient searchClient = new SearchClient(APPLICATION_ID);
            ImageSearchRequest imageSearchRequest = new ImageSearchRequest(key);
            imageSearchRequest.setResults(50);
            imageSearchRequest.setAdultOk(false);
            ImageSearchResults imageSearchResults = searchClient.imageSearch(imageSearchRequest);

            ImageSearchResult[] list = imageSearchResults.listResults();
            for (int i = 0; i < list.length; i++) {
                ImageSearchResult imageSearchResult = list[i];
                if ((imageSearchResult.getFileFormat().equals("gif") || imageSearchResult.getFileFormat().equals("jpg")
                        || imageSearchResult.getFileFormat().equals("jpeg")
                        || imageSearchResult.getFileFormat().equals("png")) &&
                        (imageSearchResult.getWidth().intValue()>20 && imageSearchResult.getHeight().intValue()>20) && 
                        (imageSearchResult.getWidth().intValue()<=1024 && imageSearchResult.getHeight().intValue()<=768)) {
                    URL url = new URL(imageSearchResult.getUrl());
                    results.add(new NameValue(url.getHost(), imageSearchResult.getUrl()));
                }
            }
        } catch (Exception ex) {
            Tools.logException(Yahoo.class, ex, "Could not get images for: " + key);
        }

        return results;
    }

    private static long mTime = System.currentTimeMillis();

    private static int mCounter = DAILY_SEARCH_LIMIT;
}