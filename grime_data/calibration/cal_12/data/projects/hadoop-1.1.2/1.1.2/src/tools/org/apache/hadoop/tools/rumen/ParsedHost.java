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
package org.apache.hadoop.tools.rumen;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class ParsedHost {
  private final String rackName;
  private final String nodeName;

  /**
   * TODO the following only works for /rack/host format. Change to support
   * arbitrary level of network names.
   */
  private static final Pattern splitPattern = Pattern
      .compile("/([^/]+)/([^/]+)");

  /**
   * TODO handle arbitrary level of network names.
   */
  static int numberOfDistances() {
    return 3;
  }

  String nameComponent(int i) throws IllegalArgumentException {
    switch (i) {
    case 0:
      return rackName;

    case 1:
      return nodeName;

    default:
      throw new IllegalArgumentException(
          "Host location component index out of range.");
    }
  }

  @Override
  public int hashCode() {
    return rackName.hashCode() * 17 + nodeName.hashCode();
  }

  public static ParsedHost parse(String name) {
    // separate out the node name
    Matcher matcher = splitPattern.matcher(name);

    if (!matcher.matches())
      return null;

    return new ParsedHost(matcher.group(1), matcher.group(2));
  }

  public ParsedHost(LoggedLocation loc) {
    List<String> coordinates = loc.getLayers();

    rackName = coordinates.get(0);
    nodeName = coordinates.get(1);
  }

  LoggedLocation makeLoggedLocation() {
    LoggedLocation result = new LoggedLocation();

    List<String> coordinates = new ArrayList<String>();

    coordinates.add(rackName);
    coordinates.add(nodeName);

    result.setLayers(coordinates);

    return result;
  }
  
  String getNodeName() {
    return nodeName;
  }
  
  String getRackName() {
    return rackName;
  }

  // expects the broadest name first
  ParsedHost(String rackName, String nodeName) {
    this.rackName = rackName;
    this.nodeName = nodeName;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ParsedHost)) {
      return false;
    }
    ParsedHost host = (ParsedHost) other;
    return (nodeName.equals(host.nodeName) && rackName.equals(host.rackName));
  }

  int distance(ParsedHost other) {
    if (nodeName.equals(other.nodeName)) {
      return 0;
    }

    if (rackName.equals(other.rackName)) {
      return 1;
    }

    return 2;
  }
}
