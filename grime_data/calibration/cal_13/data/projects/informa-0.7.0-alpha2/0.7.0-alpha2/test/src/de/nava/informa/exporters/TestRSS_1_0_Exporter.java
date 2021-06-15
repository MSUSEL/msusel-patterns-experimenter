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
//
// Informa -- RSS Library for Java
// Copyright (c) 2002 by Niko Schmuck
//
// Niko Schmuck
// http://sourceforge.net/projects/informa
// mailto:niko_schmuck@users.sourceforge.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE. If the license is not included with this distribution,
// you may find a copy at the FSF web site at 'www.gnu.org' or 'www.fsf.org',
// or you may write to the Free Software Foundation, 675 Mass Ave, Cambridge, 
// MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//


// $Id: TestRSS_1_0_Exporter.java,v 1.7 2004/05/13 22:55:18 niko_schmuck Exp $

package de.nava.informa.exporters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import de.nava.informa.core.ChannelExporterIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.Channel;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.impl.basic.Item;
import de.nava.informa.parsers.FeedParser;
import de.nava.informa.utils.InformaTestCase;

public class TestRSS_1_0_Exporter extends InformaTestCase {

  public TestRSS_1_0_Exporter(String name) {
    super("TestRSS_1_0_Exporter", name);
  }

  public void testExportChannel()
    throws IOException, MalformedURLException, ParseException {

    // TODO: refactor this into more reusable test case !!!
    
    String ch_title = "The Great Demo Channel";
    String ch_desc = "Just a very simple short description.";
    
    // create dummy channel
    ChannelIF channel = new Channel(ch_title);
    channel.setDescription(ch_desc);
    channel.setSite(new URL("http://nava.de"));
    channel.setLocation(new URL("http://nava.de/news.rdf"));
    ItemIF itemA = new Item("Bugo", "All about it!",
                            new URL("http://nava.de/huhu2002"));
    itemA.setFound(new Date());
    channel.addItem(itemA);
    // TODO: what about markup here ???
    ItemIF itemB = new Item("SoCool",
                            "????**$12 @??? # <strong>should</strong> work",
                            new URL("http://nava.de/very/nested/98"));
    itemB.setFound(new Date());
    channel.addItem(itemB);
    assertEquals(2, channel.getItems().size());
    // export this channel to file (encoding: utf-8)
    String basename = "export-rss10.xml";
    String exp_file = getOutputDir() + FS + basename;
    ChannelExporterIF exporter = new RSS_1_0_Exporter(exp_file);
    exporter.write(channel);

    // clean channel object
    channel = null;
    
    // read in again
    File inpFile = new File(exp_file);
    channel = FeedParser.parse(new ChannelBuilder(), inpFile);

    assertEquals(ch_title, channel.getTitle());
    assertEquals(ch_desc, channel.getDescription());
    
    Collection items = channel.getItems();
    assertEquals(2, items.size());
    Iterator it = items.iterator();
    ItemIF item = (ItemIF) it.next();
    if (item.equals(itemA)) {
      assertEquals(item, itemA);
      item = (ItemIF) it.next();
      assertEquals(item, itemB);
    } else {
      assertEquals(item, itemB);
      item = (ItemIF) it.next();
      assertEquals(item, itemA);
    }
  }
  
}
