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

// $Id: InformaUtils.java,v 1.10 2006/01/03 00:30:39 niko_schmuck Exp $

package de.nava.informa.utils;

import de.nava.informa.core.*;
import de.nava.informa.core.ChannelIF;

/**
 * InformaUtils class contain helper methods for various channel operations.
 */
public final class InformaUtils {

   /**
    * Copies all the top level properties from <code>sourceChan</code> into
    * this <code>updChan</code>. Convience method to update basic channel
    * properties (like title and description) in one call.
    *
    * <b>Important:</b> Location and Format is unchanged by this operation.
    *
    * TODO: Tricky. There is a bug here that has to be fixed. Channel Properties
    * which are actually pointers to other objects (e.g. Cloud) cannot be simply
    * copied because during channel and item parsing, we first parse the stream
    * into a basic (e.g. impl.basic.Cloud) object, which then gets subsequently copied
    * by this method into a persistent (e.g. impl.hibernate.Cloud) object.
    *
    * I think the solution to this is to pass a ChannelBuilder into this method
    * which is used to create the right type of duplicates of the objects instead
    * of just copying the references. For now I will be skipping those. A similar
    * kind of problem probably exists when copying item properties.
    *
    */
   public static void copyChannelProperties(ChannelIF sourceChan, ChannelIF updChan) {
     updChan.setTitle(sourceChan.getTitle());
     updChan.setDescription(sourceChan.getDescription());
     updChan.setSite(sourceChan.getSite());
     updChan.setCreator(sourceChan.getCreator());
     updChan.setCopyright(sourceChan.getCopyright());
     updChan.setPublisher(sourceChan.getPublisher());
     updChan.setLanguage(sourceChan.getLanguage());
     updChan.setRating(sourceChan.getRating());
     updChan.setGenerator(sourceChan.getGenerator());
     updChan.setDocs(sourceChan.getDocs());
     updChan.setTtl(sourceChan.getTtl());
     updChan.setLastBuildDate(sourceChan.getLastBuildDate());
     updChan.setUpdateBase(sourceChan.getUpdateBase());
     updChan.setUpdateFrequency(sourceChan.getUpdateFrequency());
     updChan.setUpdatePeriod(sourceChan.getUpdatePeriod());
     updChan.setPubDate(sourceChan.getPubDate());
     updChan.setFormat(sourceChan.getFormat());

//   updChan.setTextInput(sourceChan.getTextInput());
//   updChan.setCloud(sourceChan.getCloud());
//   updChan.setImage(sourceChan.getImage());
   }

  /**
   * Analogous function to copy all the properties of an item to another one.
   *
   * @param src - Source ItemIF
   * @param dest - Destination ItemIF
   */
  public static void copyItemProperties(ItemIF src, ItemIF dest)
   {
     dest.setTitle(src.getTitle());
     dest.setDescription(src.getDescription());
     dest.setLink(src.getLink());
     dest.setCreator(src.getCreator());
     dest.setSubject(src.getSubject());
     dest.setDate(src.getDate());
     dest.setFound(src.getFound());
     dest.setUnRead(src.getUnRead());
   }

}
