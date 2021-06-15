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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.eclipse.server.HadoopServer;
import org.apache.hadoop.eclipse.servers.IHadoopServerListener;
import org.apache.hadoop.eclipse.servers.ServerRegistry;
import org.apache.hadoop.fs.FileSystem;

/**
 * Representation of the root element containing all DFS servers. This
 * content registers an observer on Hadoop servers so as to update itself
 * when servers are updated.
 */
public class DFSLocationsRoot implements DFSContent, IHadoopServerListener {

  /**
   * 
   */
  private final DFSContentProvider provider;

  private Map<HadoopServer, DFSLocation> map =
      new HashMap<HadoopServer, DFSLocation>();

  /**
   * Register a listeners to track DFS locations updates
   * 
   * @param provider the content provider this content is the root of
   */
  DFSLocationsRoot(DFSContentProvider provider) {
    this.provider = provider;
    ServerRegistry.getInstance().addListener(this);
    this.refresh();
  }

  /*
   * Implementation of IHadoopServerListener
   */

  /* @inheritDoc */
  public synchronized void serverChanged(final HadoopServer location,
      final int type) {

    switch (type) {
      case ServerRegistry.SERVER_STATE_CHANGED: {
        this.provider.refresh(map.get(location));
        break;
      }

      case ServerRegistry.SERVER_ADDED: {
        DFSLocation dfsLoc = new DFSLocation(provider, location);
        map.put(location, dfsLoc);
        this.provider.refresh(this);
        break;
      }

      case ServerRegistry.SERVER_REMOVED: {
        map.remove(location);
        this.provider.refresh(this);
        break;
      }
    }
  }

  /**
   * Recompute the map of Hadoop locations
   */
  private synchronized void reloadLocations() {
    map.clear();
    for (HadoopServer location : ServerRegistry.getInstance().getServers())
      map.put(location, new DFSLocation(provider, location));
  }

  /* @inheritDoc */
  @Override
  public String toString() {
    return "DFS Locations";
  }

  /*
   * Implementation of DFSContent
   */

  /* @inheritDoc */
  public synchronized DFSContent[] getChildren() {
    return this.map.values().toArray(new DFSContent[this.map.size()]);
  }

  /* @inheritDoc */
  public boolean hasChildren() {
    return (this.map.size() > 0);
  }

  /* @inheritDoc */
  public void refresh() {
    reloadLocations();
    this.provider.refresh(this);
  }

  /*
   * Actions
   */

  public void disconnect() {
    Thread closeThread = new Thread() {
      /* @inheritDoc */
      @Override
      public void run() {
        try {
          System.out.printf("Closing all opened File Systems...\n");
          FileSystem.closeAll();
          System.out.printf("File Systems closed\n");

        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      }
    };

    // Wait 5 seconds for the connections to be closed
    closeThread.start();
    try {
      closeThread.join(5000);

    } catch (InterruptedException ie) {
      // Ignore
    }
  }

}
