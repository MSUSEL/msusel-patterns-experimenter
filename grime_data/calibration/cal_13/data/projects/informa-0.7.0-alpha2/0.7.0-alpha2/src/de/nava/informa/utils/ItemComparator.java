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


// $Id: ItemComparator.java,v 1.8 2006/12/04 23:43:27 italobb Exp $

package de.nava.informa.utils;

import java.util.Comparator;

import de.nava.informa.core.ItemIF;

/**
 * Custom comparator for ItemIF objects, which takes the date the news
 * item was found into account.
 *
 * @author Niko Schmuck (niko@nava.de)
 */
public class ItemComparator implements Comparator<ItemIF> {

  private boolean reverseOrder;
  private boolean useFoundDate;

  /**
   * Default constructor using ascending dates (oldes first) and using
   * the date specified by the item (as opposed to the date the item
   * was found by retrieving).
   */
  public ItemComparator() {
    this(false, false);
  }

  public ItemComparator(boolean reverseOrder) {
    this(reverseOrder, false);
  }

  public ItemComparator(boolean reverseOrder, boolean useFoundDate) {
    this.reverseOrder = reverseOrder;
    this.useFoundDate = useFoundDate;
  }
  
  public int compare(ItemIF item1, ItemIF item2) {
    int cmp = 0;
    if (useFoundDate) {
      if (item1.getFound() != null && item2.getFound() != null) {
        cmp = item1.getFound().compareTo(item2.getFound());
      }
    } else {
      if (item1.getDate() != null && item2.getDate() != null) {
        cmp = item1.getDate().compareTo(item2.getDate());
      }
    }
    if (reverseOrder) {
      return -1 * cmp;
    } else {
      return cmp;
    }
  }

  public boolean getReverseOrder() {
    return reverseOrder;
  }

  public void setReverseOrder(boolean reverseOrder) {
    this.reverseOrder = reverseOrder;
  }
  
  public boolean getUseFoundDate() {
    return useFoundDate;
  }

  public void setUseFoundDate(boolean useFoundDate) {
    this.useFoundDate = useFoundDate;
  }

}
