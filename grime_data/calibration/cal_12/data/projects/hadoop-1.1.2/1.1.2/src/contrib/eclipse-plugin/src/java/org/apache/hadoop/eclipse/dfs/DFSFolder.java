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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.hadoop.eclipse.server.HadoopServer;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Local representation of a folder in the DFS.
 * 
 * The constructor creates an empty representation of the folder and spawn a
 * thread that will fill
 */
public class DFSFolder extends DFSPath implements DFSContent {

  static Logger log = Logger.getLogger(DFSFolder.class.getName());

  private DFSContent[] children;

  protected DFSFolder(DFSContentProvider provider, HadoopServer location)
      throws IOException {

    super(provider, location);
  }

  private DFSFolder(DFSPath parent, Path path) {
    super(parent, path);
  }

  protected void loadDFSFolderChildren() throws IOException {
    List<DFSPath> list = new ArrayList<DFSPath>();

    for (FileStatus status : getDFS().listStatus(this.getPath())) {
      if (status.isDir()) {
        list.add(new DFSFolder(this, status.getPath()));
      } else {
        list.add(new DFSFile(this, status.getPath()));
      }
    }

    this.children = list.toArray(new DFSContent[list.size()]);
  }

  /**
   * Upload the given file or directory into this DfsFolder
   * 
   * @param file
   * @throws IOException
   */
  public void upload(IProgressMonitor monitor, final File file)
      throws IOException {

    if (file.isDirectory()) {
      Path filePath = new Path(this.path, file.getName());
      getDFS().mkdirs(filePath);
      DFSFolder newFolder = new DFSFolder(this, filePath);
      monitor.worked(1);
      for (File child : file.listFiles()) {
        if (monitor.isCanceled())
          return;
        newFolder.upload(monitor, child);
      }

    } else if (file.isFile()) {
      Path filePath = new Path(this.path, file.getName());
      DFSFile newFile = new DFSFile(this, filePath, file, monitor);

    } else {
      // XXX don't know what the file is?
    }
  }

  /* @inheritDoc */
  @Override
  public void downloadToLocalDirectory(IProgressMonitor monitor, File dir) {
    if (!dir.exists())
      dir.mkdirs();

    if (!dir.isDirectory()) {
      MessageDialog.openError(null, "Download to local file system",
          "Invalid directory location: \"" + dir + "\"");
      return;
    }

    File dfsPath = new File(this.getPath().toString());
    File destination = new File(dir, dfsPath.getName());

    if (!destination.exists()) {
      if (!destination.mkdir()) {
        MessageDialog.openError(null, "Download to local directory",
            "Unable to create directory " + destination.getAbsolutePath());
        return;
      }
    }

    // Download all DfsPath children
    for (Object childObj : getChildren()) {
      if (childObj instanceof DFSPath) {
        ((DFSPath) childObj).downloadToLocalDirectory(monitor, destination);
        monitor.worked(1);
      }
    }
  }

  /* @inheritDoc */
  @Override
  public int computeDownloadWork() {
    int work = 1;
    for (DFSContent child : getChildren()) {
      if (child instanceof DFSPath)
        work += ((DFSPath) child).computeDownloadWork();
    }

    return work;
  }

  /**
   * Create a new sub directory into this directory
   * 
   * @param folderName
   */
  public void mkdir(String folderName) {
    try {
      getDFS().mkdirs(new Path(this.path, folderName));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    doRefresh();
  }

  /*
   * Implementation of DFSContent
   */

  /* @inheritDoc */
  public boolean hasChildren() {
    if (this.children == null)
      return true;
    else
      return (this.children.length > 0);
  }

  /* @inheritDoc */
  public DFSContent[] getChildren() {
    if (children == null) {
      new Job("Connecting to DFS " + location) {
        @Override
        protected IStatus run(IProgressMonitor monitor) {
          try {
            loadDFSFolderChildren();
            return Status.OK_STATUS;

          } catch (IOException ioe) {
            children =
                new DFSContent[] { new DFSMessage("Error: "
                    + ioe.getLocalizedMessage()) };
            return Status.CANCEL_STATUS;

          } finally {
            // Under all circumstances, update the UI
            provider.refresh(DFSFolder.this);
          }
        }
      }.schedule();

      return new DFSContent[] { new DFSMessage("Listing folder content...") };
    }
    return this.children;
  }

  /* @inheritDoc */
  @Override
  public void refresh() {
    this.children = null;
    this.doRefresh();
  }

  /* @inheritDoc */
  @Override
  public String toString() {
    return String.format("%s (%s)", super.toString(),
        this.getChildren().length);
  }

}
