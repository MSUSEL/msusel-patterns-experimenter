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

// $Id: ChannelFormat.java,v 1.11 2006/12/04 23:40:02 italobb Exp $

package de.nava.informa.core;


/**
 * Enums to describe which syntax is used by a channel description.</p>
 *
 * @author Niko Schmuck (niko@nava.de), Italo Borssatto
 */
public enum ChannelFormat {

  /** Convenient null value to make code more robust */
  UNKNOWN_CHANNEL_FORMAT("Unknown"),

  /** Syntax according to RSS 0.9 specification. */
  RSS_0_90("RSS 0.90"),

  /** Syntax according to RSS 0.91 specification. */
  RSS_0_91("RSS 0.91"),

  /** Syntax according to RSS 0.92 specification. */
  RSS_0_92("RSS 0.92"),

  /** Syntax according to RSS 0.93 specification. */
  RSS_0_93("RSS 0.93"),

  /** Syntax according to RSS 0.94 specification. */
  RSS_0_94("RSS 0.94"),

  /** Syntax according to RSS 1.0 specification. */
  RSS_1_0("RSS 1.0"),

  /** Syntax according to RSS 2.0 specification. */
  RSS_2_0("RSS 2.0"),

  /** Syntax according to the Atom 0.1 specification. */
  ATOM_0_1("Atom 0.1"),

  /** Syntax according to the Atom 0.2 specification. */
  ATOM_0_2("Atom 0.2"),

  /** Syntax according to the Atom 0.3 specification. */
  ATOM_0_3("Atom 0.3"),

  /** Syntax according to the Atom 1.0 specification. */
  ATOM_1_0("Atom 1.0");

  private String formatSpec;

  private ChannelFormat(String formatSpec) {
    this.formatSpec = formatSpec;
  }

  public String toString() {
    return formatSpec;
  }
}
