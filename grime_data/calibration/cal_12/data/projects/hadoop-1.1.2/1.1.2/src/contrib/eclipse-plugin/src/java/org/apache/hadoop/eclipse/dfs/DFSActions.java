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
package org.apache.hadoop.eclipse.dfs;

public enum DFSActions {

  DELETE("Delete"), REFRESH("Refresh"), DOWNLOAD("Download from DFS..."), OPEN(
      "View"), MKDIR("Create new directory..."), UPLOAD_FILES(
      "Upload files to DFS..."), UPLOAD_DIR("Upload directory to DFS..."), RECONNECT(
      "Reconnect"), DISCONNECT("Disconnect");

  final String title;

  final String id;

  private static final String PREFIX = "dfs.browser.action.";

  public static DFSActions getById(String def) {
    if (!def.startsWith(PREFIX))
      return null;
    return valueOf(def.substring(PREFIX.length()).toUpperCase());
  }

  DFSActions(String title) {
    this.title = title;
    this.id = PREFIX + this.name().toLowerCase();
  }
}
