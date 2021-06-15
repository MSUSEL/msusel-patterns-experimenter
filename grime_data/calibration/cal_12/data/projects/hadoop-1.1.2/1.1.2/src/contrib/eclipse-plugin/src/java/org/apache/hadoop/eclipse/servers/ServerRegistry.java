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
package org.apache.hadoop.eclipse.servers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.eclipse.Activator;
import org.apache.hadoop.eclipse.server.HadoopServer;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Register of Hadoop locations.
 * 
 * Each location corresponds to a Hadoop {@link Configuration} stored as an
 * XML file in the workspace plug-in configuration directory:
 * <p>
 * <tt>
 * &lt;workspace-dir&gt;/.metadata/.plugins/org.apache.hadoop.eclipse/locations/*.xml
 * </tt>
 * 
 */
public class ServerRegistry {

  private static final ServerRegistry INSTANCE = new ServerRegistry();

  public static final int SERVER_ADDED = 0;

  public static final int SERVER_REMOVED = 1;

  public static final int SERVER_STATE_CHANGED = 2;

  private final File baseDir =
      Activator.getDefault().getStateLocation().toFile();

  private final File saveDir = new File(baseDir, "locations");

  private ServerRegistry() {
    if (saveDir.exists() && !saveDir.isDirectory())
      saveDir.delete();
    if (!saveDir.exists())
      saveDir.mkdirs();

    load();
  }

  private Map<String, HadoopServer> servers;

  private Set<IHadoopServerListener> listeners =
      new HashSet<IHadoopServerListener>();

  public static ServerRegistry getInstance() {
    return INSTANCE;
  }

  public synchronized Collection<HadoopServer> getServers() {
    return Collections.unmodifiableCollection(servers.values());
  }

  /**
   * Load all available locations from the workspace configuration directory.
   */
  private synchronized void load() {
    Map<String, HadoopServer> map = new TreeMap<String, HadoopServer>();
    for (File file : saveDir.listFiles()) {
      try {
        HadoopServer server = new HadoopServer(file);
        map.put(server.getLocationName(), server);

      } catch (Exception exn) {
        System.err.println(exn);
      }
    }
    this.servers = map;
  }

  private synchronized void store() {
    try {
      File dir = File.createTempFile("locations", "new", baseDir);
      dir.delete();
      dir.mkdirs();

      for (HadoopServer server : servers.values()) {
        server.storeSettingsToFile(new File(dir, server.getLocationName()
            + ".xml"));
      }

      FilenameFilter XMLFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
          String lower = name.toLowerCase();
          return lower.endsWith(".xml");
        }
      };

      File backup = new File(baseDir, "locations.backup");
      if (backup.exists()) {
        for (File file : backup.listFiles(XMLFilter))
          if (!file.delete())
            throw new IOException("Unable to delete backup location file: "
                + file);
        if (!backup.delete())
          throw new IOException(
              "Unable to delete backup location directory: " + backup);
      }

      saveDir.renameTo(backup);
      dir.renameTo(saveDir);

    } catch (IOException ioe) {
      ioe.printStackTrace();
      MessageDialog.openError(null,
          "Saving configuration of Hadoop locations failed", ioe.toString());
    }
  }

  public void dispose() {
    for (HadoopServer server : getServers()) {
      server.dispose();
    }
  }

  public synchronized HadoopServer getServer(String location) {
    return servers.get(location);
  }

  /*
   * HadoopServer map listeners
   */

  public void addListener(IHadoopServerListener l) {
    synchronized (listeners) {
      listeners.add(l);
    }
  }

  public void removeListener(IHadoopServerListener l) {
    synchronized (listeners) {
      listeners.remove(l);
    }
  }

  private void fireListeners(HadoopServer location, int kind) {
    synchronized (listeners) {
      for (IHadoopServerListener listener : listeners) {
        listener.serverChanged(location, kind);
      }
    }
  }

  public synchronized void removeServer(HadoopServer server) {
    this.servers.remove(server.getLocationName());
    store();
    fireListeners(server, SERVER_REMOVED);
  }

  public synchronized void addServer(HadoopServer server) {
    this.servers.put(server.getLocationName(), server);
    store();
    fireListeners(server, SERVER_ADDED);
  }

  /**
   * Update one Hadoop location
   * 
   * @param originalName the original location name (might have changed)
   * @param server the location
   */
  public synchronized void updateServer(String originalName,
      HadoopServer server) {

    // Update the map if the location name has changed
    if (!server.getLocationName().equals(originalName)) {
      servers.remove(originalName);
      servers.put(server.getLocationName(), server);
    }
    store();
    fireListeners(server, SERVER_STATE_CHANGED);
  }
}
