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
package org.apache.hadoop.filecache;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.TrackerDistributedCacheManager.CacheStatus;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobLocalizer;

/**
 * Helper class of {@link TrackerDistributedCacheManager} that represents
 * the cached files of a single job.
 * 
 * <b>This class is internal to Hadoop, and should not be treated as a public
 * interface.</b>
 */
public class TaskDistributedCacheManager {
  private final TrackerDistributedCacheManager distributedCacheManager;
  private final List<CacheFile> cacheFiles = new ArrayList<CacheFile>();
  private final List<String> classPaths = new ArrayList<String>();
  
  private boolean setupCalled = false;

  /**
   * Struct representing a single cached file.
   * There are four permutations (archive, file) and
   * (don't put in classpath, do put in classpath).
   */
  static class CacheFile {
    /** URI as in the configuration */
    final URI uri;
    enum FileType {
      REGULAR,
      ARCHIVE
    }
    boolean isPublic = true;
    /** Whether to decompress */
    final FileType type;
    final long timestamp;
    /** Whether this is to be added to the classpath */
    final boolean shouldBeAddedToClassPath;
    boolean localized = false;
    /** The owner of the localized file. Relevant only on the tasktrackers */
    final String owner;
    private CacheStatus status;

    CacheFile(URI uri, FileType type, boolean isPublic, long timestamp, 
                      boolean classPath) throws IOException {
      this.uri = uri;
      this.type = type;
      this.isPublic = isPublic;
      this.timestamp = timestamp;
      this.shouldBeAddedToClassPath = classPath;
      this.owner = 
          TrackerDistributedCacheManager.getLocalizedCacheOwner(isPublic);
    }

    /**
     * Set the status for this cache file.
     * @param status
     */
    public void setStatus(CacheStatus status) {
      this.status = status;
    }
    
    /**
     * Get the status for this cache file.
     * @return the status object
     */
    public CacheStatus getStatus() {
      return status;
    }
    /**
     * Converts the scheme used by DistributedCache to serialize what files to
     * cache in the configuration into CacheFile objects that represent those 
     * files.
     */
    private static List<CacheFile> makeCacheFiles(URI[] uris, 
        long[] timestamps, boolean cacheVisibilities[], Path[] paths, 
        FileType type) throws IOException {
      List<CacheFile> ret = new ArrayList<CacheFile>();
      if (uris != null) {
        if (uris.length != timestamps.length) {
          throw new IllegalArgumentException("Mismatched uris and timestamps.");
        }
        Map<String, Path> classPaths = new HashMap<String, Path>();
        if (paths != null) {
          for (Path p : paths) {
            classPaths.put(p.toUri().getPath().toString(), p);
            }
        }
        for (int i = 0; i < uris.length; ++i) {
          URI u = uris[i];
          boolean isClassPath = (null != classPaths.get(u.getPath()));
          ret.add(new CacheFile(u, type, cacheVisibilities[i],
              timestamps[i], isClassPath));
        }
      }
      return ret;
    }
    
    boolean getLocalized() {
      return localized;
    }
    
    void setLocalized(boolean val) {
      localized = val;
    }
  }

  TaskDistributedCacheManager(
      TrackerDistributedCacheManager distributedCacheManager,
      Configuration taskConf) throws IOException {
    this.distributedCacheManager = distributedCacheManager;
    
    this.cacheFiles.addAll(
        CacheFile.makeCacheFiles(DistributedCache.getCacheFiles(taskConf),
            DistributedCache.getFileTimestamps(taskConf),
            TrackerDistributedCacheManager.getFileVisibilities(taskConf),
            DistributedCache.getFileClassPaths(taskConf),
            CacheFile.FileType.REGULAR));
    this.cacheFiles.addAll(
        CacheFile.makeCacheFiles(DistributedCache.getCacheArchives(taskConf),
          DistributedCache.getArchiveTimestamps(taskConf),
          TrackerDistributedCacheManager.getArchiveVisibilities(taskConf),
          DistributedCache.getArchiveClassPaths(taskConf), 
          CacheFile.FileType.ARCHIVE));
  }

  /**
   * Retrieve public distributed cache files into the local cache and updates
   * the task configuration (which has been passed in via the constructor).
   * The private distributed cache is just looked at and the paths where the
   * files/archives should go to is decided here. The actual localization is
   * done by {@link JobLocalizer}.
   * 
   * It is the caller's responsibility to re-write the task configuration XML
   * file, if necessary.
   */
  public void setupCache(Configuration taskConf, String publicCacheSubdir,
      String privateCacheSubdir) throws IOException {
    setupCalled = true;
    ArrayList<Path> localArchives = new ArrayList<Path>();
    ArrayList<Path> localFiles = new ArrayList<Path>();

    for (CacheFile cacheFile : cacheFiles) {
      URI uri = cacheFile.uri;
      FileSystem fileSystem = FileSystem.get(uri, taskConf);
      FileStatus fileStatus = fileSystem.getFileStatus(new Path(uri.getPath()));
      Path p;
      if (cacheFile.isPublic) {
        p = distributedCacheManager.getLocalCache(uri, taskConf,
            publicCacheSubdir, fileStatus, 
            cacheFile.type == CacheFile.FileType.ARCHIVE,
            cacheFile.timestamp, cacheFile.isPublic, cacheFile);
      } else {
        p = distributedCacheManager.getLocalCache(uri, taskConf,
            privateCacheSubdir, fileStatus, 
            cacheFile.type == CacheFile.FileType.ARCHIVE,
            cacheFile.timestamp, cacheFile.isPublic, cacheFile);
      }
      cacheFile.setLocalized(true);

      if (cacheFile.type == CacheFile.FileType.ARCHIVE) {
        localArchives.add(p);
      } else {
        localFiles.add(p);
      }
      if (cacheFile.shouldBeAddedToClassPath) {
        classPaths.add(p.toString());
      }
    }

    // Update the configuration object with localized data.
    if (!localArchives.isEmpty()) {
      DistributedCache.addLocalArchives(taskConf, 
        stringifyPathList(localArchives));
    }
    if (!localFiles.isEmpty()) {
      DistributedCache.addLocalFiles(taskConf, stringifyPathList(localFiles));
    }

  }

  /*
   * This method is called from unit tests.
   */
  List<CacheFile> getCacheFiles() {
    return cacheFiles;
  }
  
  private static String stringifyPathList(List<Path> p){
    if (p == null || p.isEmpty()) {
      return null;
    }
    StringBuilder str = new StringBuilder(p.get(0).toString());
    for (int i = 1; i < p.size(); i++){
      str.append(",");
      str.append(p.get(i).toString());
    }
    return str.toString();
  }

  /** 
   * Retrieves class paths (as local references) to add. 
   * Should be called after setup().
   * 
   */
  public List<String> getClassPaths() throws IOException {
    if (!setupCalled) {
      throw new IllegalStateException(
          "getClassPaths() should be called after setup()");
    }
    return classPaths;
  }
  
  /**
   * Releases the cached files/archives, so that space
   * can be reclaimed by the {@link TrackerDistributedCacheManager}.
   */
  public void release() throws IOException {
    for (CacheFile c : cacheFiles) {
      if (c.getLocalized() && c.status != null) {
        distributedCacheManager.releaseCache(c.status);
      }
    }
  }

  public void setSizes(long[] sizes) throws IOException {
    int i = 0;
    for (CacheFile c: cacheFiles) {
      if (!c.isPublic && c.status != null) {
        distributedCacheManager.setSize(c.status, sizes[i]);
      }
      i++;
    }
  }

  /**
   * Creates a class loader that includes the designated
   * files and archives.
   */
  public ClassLoader makeClassLoader(final ClassLoader parent)
      throws MalformedURLException {
    final URL[] urls = new URL[classPaths.size()];
    for (int i = 0; i < classPaths.size(); ++i) {
      urls[i] = new File(classPaths.get(i)).toURI().toURL();
    }
    return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
      @Override
      public ClassLoader run() {
        return new URLClassLoader(urls, parent);
      }
    });
  }
}
