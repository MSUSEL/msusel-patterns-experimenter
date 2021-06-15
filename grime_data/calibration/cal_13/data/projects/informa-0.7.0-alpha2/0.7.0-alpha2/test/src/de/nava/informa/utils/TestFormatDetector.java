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


// $Id: TestFormatDetector.java,v 1.18 2005/09/27 22:17:10 niko_schmuck Exp $

package de.nava.informa.utils;

import java.io.File;

import de.nava.informa.core.ChannelFormat;
import de.nava.informa.core.UnsupportedFormatException;

public class TestFormatDetector extends InformaTestCase {

  public TestFormatDetector(String name) {
    super("TestFormatDetector", name);
  }

  public void testFormatRSS091xmlhack() throws Exception {
    File inpFile = new File(getDataDir(), "xmlhack-0.91.xml");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_0_91, actualFormat);
  }
  
  public void testFormatRSS091aeden() throws Exception {
    File inpFile = new File(getDataDir(), "aeden.rss");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_0_91, actualFormat);
  }
  
  public void testFormatRSS092gratefuldead() throws Exception {
    File inpFile = new File(getDataDir(), "gratefulDead-rss-0.92.xml");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_0_92, actualFormat);
  }
  
  public void testFormatRSS10xmlhack() throws Exception {
    File inpFile = new File(getDataDir(), "xmlhack-1.0.xml");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_1_0, actualFormat);
  }
  
  public void testFormatRSS10slashdot() throws Exception {
    File inpFile = new File(getDataDir(), "slashdot.rdf");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_1_0, actualFormat);
  }

  public void testFormatRSS10googleweblog() throws Exception {
    File inpFile = new File(getDataDir(), "google-weblog.rdf");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_1_0, actualFormat);
  }

  public void testFormatRSS10w3csynd() throws Exception {
    File inpFile = new File(getDataDir(), "w3c-synd.rdf");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_1_0, actualFormat);
  }

  public void testFormatRSS20A() throws Exception {
    File inpFile = new File(getDataDir(), "informa-projnews.xml");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_2_0, actualFormat);
  }

  public void testFormatRSS20B() throws Exception {
    File inpFile = new File(getDataDir(), "snipsnap-org.rss");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.RSS_2_0, actualFormat);
  }

  public void testUnsupportedFormat() throws Exception {
    File inpFile = new File(getDataDir(), "dir-xml-rpc-com.opml");
    try {
      FormatDetector.getFormat(inpFile.toURL());
      fail("This format is not yet supported ...");
    } catch (UnsupportedFormatException e) {
      assertTrue(true);
    }
  }

  public void testAtom0_3() throws Exception {
    File inpFile = new File(getDataDir(), "atom0-3.xml");
    ChannelFormat actualFormat = FormatDetector.getFormat(inpFile.toURL());
    assertEquals(ChannelFormat.ATOM_0_3, actualFormat);
  }
}
