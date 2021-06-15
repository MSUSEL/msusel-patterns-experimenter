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

// $Id: ItemGuid.java,v 1.5 2006/01/03 00:30:39 niko_schmuck Exp $

package de.nava.informa.impl.hibernate;

import de.nava.informa.core.ItemGuidIF;
import de.nava.informa.core.ItemIF;

/**
 * Hibernate implementation of the ItemGuidIF interface.
 *
 * @author Michael Harhen
 */
public class ItemGuid implements ItemGuidIF {

  private static final long serialVersionUID = 8927598911643636005L;

  private long id = -1;
  private ItemIF item;
  private String location;
  private boolean permaLink;

  public ItemGuid() {
    this(null);
  }

  /**
   * Default constructor.
   */
  public ItemGuid(ItemIF item) {
    this(item, null, true);
  }

  public ItemGuid(ItemIF item, String location, boolean permaLink) {
    this.item = item;
    this.location = location;
    this.permaLink = permaLink;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  // --------------------------------------------------------------
  // implementation of ItemGuidIF interface
  // --------------------------------------------------------------

  public ItemIF getItem() {
    return item;
  }

  public void setItem(ItemIF item) {
    this.item = item;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public boolean isPermaLink() {
    return permaLink;
  }

  public void setPermaLink(boolean permaLink) {
    this.permaLink = permaLink;
  }

  /**
   * GUID is equal if location is equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ItemGuidIF)) return false;

    final ItemGuidIF itemGuid = (ItemGuidIF) o;

    if (location != null ? !location.equals(itemGuid.getLocation()) : itemGuid.getLocation() != null) return false;

    return true;
  }

  public int hashCode() {
    return (location != null ? location.hashCode() : 0);
  }

}
