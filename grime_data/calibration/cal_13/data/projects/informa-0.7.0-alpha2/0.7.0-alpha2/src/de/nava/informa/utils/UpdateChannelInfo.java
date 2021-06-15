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
// Copyright (c) 2002-2003 by Niko Schmuck
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


// $Id: UpdateChannelInfo.java,v 1.5 2004/07/01 08:12:58 spyromus Exp $

package de.nava.informa.utils;

/**
 * Metadata class containing properties about channel updates.
 *
 * @author Niko Schmuck
 */
public class UpdateChannelInfo {

  private int nrProblemsOccurred;
  private int deactivateAfterErrors;
  private boolean formatDetected = false;
  private Exception lastException;
  private long lastUpdatedTimestamp = -1;

  /**
   * Constructor.
   *
   * @param deactivateAfterErrors Number of errors after which a channel should
   *        not any longer be parsed periodically. Specify 0, if you do not
   *        want auto-deactivation.
   */
  public UpdateChannelInfo(int deactivateAfterErrors) {
    this.deactivateAfterErrors = deactivateAfterErrors;
    this.reset();
  }

  /**
   * @return Number of times a problem occurred while parsing a channel.
   */
  public int getNrProblemsOccurred() {
    return nrProblemsOccurred;
  }

  /**
   * @return Number of errors after a channel should be automatically
   *         deactivated.
   */
  public int getDeactivateAfterErrors() {
    return deactivateAfterErrors;
  }

  /**
   * @return The last exception that was captured when a channel problem
   *         occurred. May return null in the case no problem was reported yet.
   */
  public Exception getLastException() {
    return lastException;
  }

  /**
   * Resets the state, as if no problems ever occurred.
   */
  public void reset() {
    this.nrProblemsOccurred = 0;
    this.lastException = null;
  }

  /**
   * Increases the number of times a channel could not be read.
   *
   * @param e The Exception causing the channel parse problem.
   */
  public synchronized void increaseProblemsOccurred(Exception e) {
    this.lastException = e;
    this.nrProblemsOccurred++;
  }

  /**
   * Should the channel be deactivated?
   *
   * @return true in the case auto-deactivation is on and more errors have been
   *         reported than the given threshold.
   */
  public boolean shouldDeactivate() {
    if (deactivateAfterErrors < 1) {
      return false;
    }
    return (nrProblemsOccurred >= deactivateAfterErrors);
  }

  /**
   * Has the feed been grabbed for the first time, and successfully parsed?
   *
   * @return true if format has been detected, and ChannelIF.getFormat
   *         would return a valid format.
   */
  public boolean getFormatDetected() {
    return formatDetected;
  }

  public void setFormatDetected(boolean formatDetected) {
    this.formatDetected = formatDetected;
  }

  // ===========================================================================
  
  public String toString() {
    StringBuffer sb = new StringBuffer(42);
    sb.append("nr probs occurred: ").append(nrProblemsOccurred)
      .append(", deactivate after ").append(deactivateAfterErrors)
      .append(", format detected: ").append(formatDetected);
    if (lastException != null) {
      sb.append(", last exception: ").append(lastException.getMessage());
    }
    return sb.toString();
  }

  public long getLastUpdatedTimestamp() {
    return lastUpdatedTimestamp;
  }

  public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
    this.lastUpdatedTimestamp = lastUpdatedTimestamp;
  }
}
