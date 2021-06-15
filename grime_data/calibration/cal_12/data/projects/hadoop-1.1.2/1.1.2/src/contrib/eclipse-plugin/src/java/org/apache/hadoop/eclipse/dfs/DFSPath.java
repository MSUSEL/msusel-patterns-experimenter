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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.eclipse.ErrorMessageDialog;
import org.apache.hadoop.eclipse.server.ConfProp;
import org.apache.hadoop.eclipse.server.HadoopServer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * DFS Path handling for DFS
 */
public abstract class DFSPath implements DFSContent {

  protected final DFSContentProvider provider;

  protected HadoopServer location;

  private DistributedFileSystem dfs = null;

  protected final Path path;

  protected final DFSPath parent;

  /**
   * For debugging purpose
   */
  static Logger log = Logger.getLogger(DFSPath.class.getName());

  /**
   * Create a path representation for the given location in the given viewer
   * 
   * @param location
   * @param path
   * @param viewer
   */
  public DFSPath(DFSContentProvider provider, HadoopServer location)
      throws IOException {

    this.provider = provider;
    this.location = location;
    this.path = new Path("/");
    this.parent = null;
  }

  /**
   * Create a sub-path representation for the given parent path
   * 
   * @param parent
   * @param path
   */
  protected DFSPath(DFSPath parent, Path path) {
    this.provider = parent.provider;
    this.location = parent.location;
    this.dfs = parent.dfs;
    this.parent = parent;
    this.path = path;
  }

  protected void dispose() {
    // Free the DFS connection
  }

  /* @inheritDoc */
  @Override
  public String toString() {
    if (path.equals("/")) {
      return location.getConfProp(ConfProp.FS_DEFAULT_URI);

    } else {
      return this.path.getName();
    }
  }

  /**
   * Does a recursive delete of the remote directory tree at this node.
   */
  public void delete() {
    try {
      getDFS().delete(this.path, true);

    } catch (IOException e) {
      e.printStackTrace();
      MessageDialog.openWarning(null, "Delete file",
          "Unable to delete file \"" + this.path + "\"\n" + e);
    }
  }

  public DFSPath getParent() {
    return parent;
  }

  public abstract void refresh();

  /**
   * Refresh the UI element for this content
   */
  public void doRefresh() {
    provider.refresh(this);
  }

  /**
   * Copy the DfsPath to the given local directory
   * 
   * @param directory the local directory
   */
  public abstract void downloadToLocalDirectory(IProgressMonitor monitor,
      File dir);

  public Path getPath() {
    return this.path;
  }

  /**
   * Gets a connection to the DFS
   * 
   * @return a connection to the DFS
   * @throws IOException
   */
  DistributedFileSystem getDFS() throws IOException {
    if (this.dfs == null) {
      FileSystem fs = location.getDFS();
      if (!(fs instanceof DistributedFileSystem)) {
        ErrorMessageDialog.display("DFS Browser",
            "The DFS Browser cannot browse anything else "
                + "but a Distributed File System!");
        throw new IOException("DFS Browser expects a DistributedFileSystem!");
      }
      this.dfs = (DistributedFileSystem) fs;
    }
    return this.dfs;
  }

  public abstract int computeDownloadWork();

}
