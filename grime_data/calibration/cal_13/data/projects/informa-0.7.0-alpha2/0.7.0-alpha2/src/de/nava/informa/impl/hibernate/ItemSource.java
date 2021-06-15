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


// $Id: ItemSource.java,v 1.5 2006/01/03 00:30:39 niko_schmuck Exp $

package de.nava.informa.impl.hibernate;

import java.util.Date;
import de.nava.informa.core.ItemIF;
import de.nava.informa.core.ItemSourceIF;

/**
 * Hibernate implementation of the ItemSourceIF interface.
 *
 * @author Michael Harhen
 */
public class ItemSource implements ItemSourceIF {

  private static final long serialVersionUID = 4087530754063431947L;

  private long id = -1;
  private ItemIF item;
  private String name;
  private String location;
  private Date timestamp;

  public ItemSource() {
    this(null);
  }

  /**
   * Default constructor.
   */
  public ItemSource(ItemIF item) {
    this(item, null, null, null);
  }

  public ItemSource(ItemIF item, String name, String location, Date timestamp) {
    this.item = item;
    this.name = name;
    this.location = location;
    this.timestamp = timestamp;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  // --------------------------------------------------------------
  // implementation of ItemSourceIF interface
  // --------------------------------------------------------------

  public ItemIF getItem() {
    return item;
  }

  public void setItem(ItemIF item) {
    this.item = item;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ItemSourceIF)) return false;

    final ItemSourceIF i = (ItemSourceIF) o;

    if (location != null ? !location.equals(i.getLocation()) : i.getLocation() != null) return false;
    if (name != null ? !name.equals(i.getName()) : i.getName() != null) return false;
    if (timestamp != null ? !timestamp.equals(i.getTimestamp()) : i.getTimestamp() != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = (name != null ? name.hashCode() : 0);
    result = 29 * result + (location != null ? location.hashCode() : 0);
    result = 29 * result + (timestamp != null ? timestamp.hashCode() : 0);
    return result;
  }

}
