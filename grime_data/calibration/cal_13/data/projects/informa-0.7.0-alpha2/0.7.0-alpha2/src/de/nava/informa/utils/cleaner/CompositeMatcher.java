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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// $Id: CompositeMatcher.java,v 1.2 2006/12/04 23:43:28 italobb Exp $
//

package de.nava.informa.utils.cleaner;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;

import java.util.List;
import java.util.Vector;

/**
 * Composite matcher follows Composite pattern to combine several matchers
 * and present them as single instance. It uses simple rule to make a decision.
 * If at least one matcher matches the item composite object also returns match.
 *
 * @author Aleksey Gureev (spyromus@noizeramp.com)
 */
class CompositeMatcher implements CleanerMatcherIF {
  private List<CleanerMatcherIF> matchers = new Vector<CleanerMatcherIF>();

  /**
   * Invoked by cleaning engine to check given item in given channel for matching some rule.
   *
   * @param item    item to check.
   * @param channel channel where the item is.
   * @return TRUE if item matches the rule.
   */
  public boolean isMatching(ItemIF item, ChannelIF channel) {
    boolean matching = false;

    final int size = matchers.size();
    for (int i = 0; i < size && !matching; i++) {
      CleanerMatcherIF matcher = (CleanerMatcherIF) matchers.get(i);
      matching = matcher.isMatching(item, channel);
    }

    return matching;
  }

  /**
   * Adds new matcher to the list.
   *
   * @param m matcher object.
   */
  public void add(CleanerMatcherIF m) {
    if (!matchers.contains(m)) {
      matchers.add(m);
    }
  }

  /**
   * Removes matcher from the list.
   *
   * @param m matcher object.
   */
  public void remove(CleanerMatcherIF m) {
    matchers.remove(m);
  }
}
