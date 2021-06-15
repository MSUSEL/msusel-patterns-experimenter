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


// $Id: ChannelIndexer.java,v 1.8 2006/12/04 23:43:28 italobb Exp $

package de.nava.informa.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;

/**
 * Utility class which allows to generate respective maintain a
 * fulltext index for news items.
 *
 * @author Niko Schmuck (niko@nava.de)
 */
public final class ChannelIndexer {

  private static Log logger = LogFactory.getLog(ChannelIndexer.class);

  private String indexDir;
  private int nrOfIndexedItems;
  private Analyzer analyzer;
  
  /**
   * Constructor which allows to specify the index directory.
   * For the full-text indexing process the lucene
   * {@link org.apache.lucene.analysis.standard.StandardAnalyzer}
   * is used.
   *
   * @param indexDir - The directory in which the index files are stored.
   */
  public ChannelIndexer(String indexDir) {
    this.indexDir = indexDir;
    this.nrOfIndexedItems = 0;
    this.analyzer = new StandardAnalyzer();
  }
  
  /**
   * Index all news items contained in the given channels.
   *
   * @param createNewIndex - wether a new index should be generated or
   *        an existant one should be taken into account
   * @param channels - a collection of ChannelIF objects
   */
  public void indexChannels(boolean createNewIndex, Collection channels)
    throws java.io.IOException {

    Collection<ItemIF> items = new ArrayList<ItemIF>();
    Iterator itC = channels.iterator();
    while (itC.hasNext()) {
      ChannelIF channel = (ChannelIF) itC.next();
      if (logger.isDebugEnabled()) {
        logger.debug("Searching channel " + channel + " for items.");
      }
      items.addAll(channel.getItems());
    }
    if (!items.isEmpty()) {
      indexItems(createNewIndex, items);
    } else {
      logger.info("No items found for indexing.");
    }
  }

  /**
   * Index all given news items.
   *
   * @param createNewIndex - Wether a new index should be generated or
   *        an existant one should be taken into account.
   * @param items - A collection of ItemIF objects.
   */
  public void indexItems(boolean createNewIndex, Collection<ItemIF> items)
    throws java.io.IOException {
    
    logger.info("Start writing index.");
    IndexWriter writer = new IndexWriter(indexDir, analyzer, createNewIndex);
    Iterator<ItemIF> itI = items.iterator();
    while (itI.hasNext()) {
      ItemIF item = itI.next();
      if (logger.isDebugEnabled()) {
        logger.debug("Add item " + item + " to index.");
      }
      writer.addDocument(ItemDocument.makeDocument(item));
    }
    writer.optimize();
    nrOfIndexedItems = writer.docCount();
    writer.close();
    logger.info("Finished writing index.");
  }

  /**
   * Returns the number of documents that were in the index last time
   * the index operation was performed.
   *
   * Note: Use only directly after the indexing process, otherwise the
   * return value may be wrong.
   */
  public int getNrOfIndexedItems() {
    return nrOfIndexedItems;
  }

  public void setIndexDir(String indexDir) {
    this.indexDir = indexDir;
  }

  public String getIndexDir() {
    return indexDir;
  }
  
}
