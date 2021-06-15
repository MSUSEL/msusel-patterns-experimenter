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
package org.apache.hadoop.eclipse;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

/**
 * Icons manager
 */
public class ImageLibrary {

  private final Bundle bundle = Activator.getDefault().getBundle();

  /**
   * Singleton instance
   */
  private static volatile ImageLibrary instance = null;

  private ISharedImages sharedImages =
      PlatformUI.getWorkbench().getSharedImages();

  /**
   * Where resources (icons, images...) are available in the Bundle
   */
  private static final String RESOURCE_DIR = "resources/";

  /**
   * Public access to image descriptors
   * 
   * @param name
   * @return the image descriptor
   */
  public static ImageDescriptor get(String name) {
    return getInstance().getImageDescriptorByName(name);
  }

  /**
   * Public access to images
   * 
   * @param name
   * @return the image
   */
  public static Image getImage(String name) {
    return getInstance().getImageByName(name);
  }

  /**
   * Singleton access
   * 
   * @return the Image library
   */
  public static ImageLibrary getInstance() {
    if (instance == null) {
      synchronized (ImageLibrary.class) {
        if (instance == null)
          instance = new ImageLibrary();
      }
    }
    return instance;
  }

  /**
   * Map of registered resources (ImageDescriptor and Image)
   */
  private Map<String, ImageDescriptor> descMap =
      new HashMap<String, ImageDescriptor>();

  private Map<String, Image> imageMap = new HashMap<String, Image>();

  /**
   * Image library constructor: put image definitions here.
   */
  private ImageLibrary() {
    /*
     * Servers view
     */
    newImage("server.view.location.entry", "Elephant-24x24.png");
    newImage("server.view.job.entry", "job.gif");
    newImage("server.view.action.location.new", "location-new-16x16.png");
    newImage("server.view.action.location.edit", "location-edit-16x16.png");
    newSharedImage("server.view.action.delete",
        ISharedImages.IMG_TOOL_DELETE);

    /*
     * DFS Browser
     */
    newImage("dfs.browser.root.entry", "files.gif");
    newImage("dfs.browser.location.entry", "Elephant-16x16.png");
    newSharedImage("dfs.browser.folder.entry", ISharedImages.IMG_OBJ_FOLDER);
    newSharedImage("dfs.browser.file.entry", ISharedImages.IMG_OBJ_FILE);
    // DFS files in editor
    newSharedImage("dfs.file.editor", ISharedImages.IMG_OBJ_FILE);
    // Actions
    newImage("dfs.browser.action.mkdir", "new-folder.png");
    newImage("dfs.browser.action.download", "download.png");
    newImage("dfs.browser.action.upload_files", "upload.png");
    newImage("dfs.browser.action.upload_dir", "upload.png");
    newSharedImage("dfs.browser.action.delete",
        ISharedImages.IMG_TOOL_DELETE);
    newImage("dfs.browser.action.refresh", "refresh.png");

    /*
     * Wizards
     */
    newImage("wizard.mapper.new", "mapwiz.png");
    newImage("wizard.reducer.new", "reducewiz.png");
    newImage("wizard.driver.new", "driverwiz.png");
    newImage("wizard.mapreduce.project.new", "projwiz.png");
  }

  /**
   * Accessor to images
   * 
   * @param name
   * @return
   */
  private ImageDescriptor getImageDescriptorByName(String name) {
    return this.descMap.get(name);
  }

  /**
   * Accessor to images
   * 
   * @param name
   * @return
   */
  private Image getImageByName(String name) {
    return this.imageMap.get(name);
  }

  /**
   * Access to platform shared images
   * 
   * @param name
   * @return
   */
  private ImageDescriptor getSharedByName(String name) {
    return sharedImages.getImageDescriptor(name);
  }

  /**
   * Load and register a new image. If the image resource does not exist or
   * fails to load, a default "error" resource is supplied.
   * 
   * @param name name of the image
   * @param filename name of the file containing the image
   * @return whether the image has correctly been loaded
   */
  private boolean newImage(String name, String filename) {
    ImageDescriptor id;
    boolean success;

    try {
      URL fileURL =
          FileLocator.find(bundle, new Path(RESOURCE_DIR + filename), null);
      id = ImageDescriptor.createFromURL(FileLocator.toFileURL(fileURL));
      success = true;

    } catch (Exception e) {

      e.printStackTrace();
      id = ImageDescriptor.getMissingImageDescriptor();
      // id = getSharedByName(ISharedImages.IMG_OBJS_ERROR_TSK);
      success = false;
    }

    descMap.put(name, id);
    imageMap.put(name, id.createImage(true));

    return success;
  }

  /**
   * Register an image from the workspace shared image pool. If the image
   * resource does not exist or fails to load, a default "error" resource is
   * supplied.
   * 
   * @param name name of the image
   * @param sharedName name of the shared image ({@link ISharedImages})
   * @return whether the image has correctly been loaded
   */
  private boolean newSharedImage(String name, String sharedName) {
    boolean success = true;
    ImageDescriptor id = getSharedByName(sharedName);

    if (id == null) {
      id = ImageDescriptor.getMissingImageDescriptor();
      // id = getSharedByName(ISharedImages.IMG_OBJS_ERROR_TSK);
      success = false;
    }

    descMap.put(name, id);
    imageMap.put(name, id.createImage(true));

    return success;
  }

  /**
   * Register an image from the workspace shared image pool. If the image
   * resource does not exist or fails to load, a default "error" resource is
   * supplied.
   * 
   * @param name name of the image
   * @param sharedName name of the shared image ({@link ISharedImages})
   * @return whether the image has correctly been loaded
   */
  private boolean newPluginImage(String name, String pluginId,
      String filename) {

    boolean success = true;
    ImageDescriptor id =
        AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, filename);

    if (id == null) {
      id = ImageDescriptor.getMissingImageDescriptor();
      // id = getSharedByName(ISharedImages.IMG_OBJS_ERROR_TSK);
      success = false;
    }

    descMap.put(name, id);
    imageMap.put(name, id.createImage(true));

    return success;
  }
}
