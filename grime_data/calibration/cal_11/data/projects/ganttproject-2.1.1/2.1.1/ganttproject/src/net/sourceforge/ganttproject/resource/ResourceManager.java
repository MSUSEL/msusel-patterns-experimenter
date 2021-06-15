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
/*
 * ResourceManager.java
 *
 * Created on 27. Mai 2003, 08:08
 */

package net.sourceforge.ganttproject.resource;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.ganttproject.CustomPropertyManager;
import net.sourceforge.ganttproject.undo.GPUndoManager;

/**
 * This interface is used to isolate the implementation of a resource manager
 * from the application. The interface is defined against an abstract class the
 * ProjectResource class. Normally only one instance of the Resourcemanager
 * should be instantiated.
 * 
 * @author barmeier
 */
public interface ResourceManager {
    public ProjectResource create(String name, int i);

    /**
     * Adds the resource to the internal list of available resources.
     * 
     * @param resource
     *            The resource that should be added to the list.
     */
    public void add(ProjectResource resource);

    /**
     * Retrieves an ancestor of ProjectResource identified by an identity value.
     * 
     * @param id
     *            The id is an integer value that is unique for every resource.
     * @return Ancestor of ProjectResource containing the requested resource.
     * @see ProjectResource
     */
    public ProjectResource getById(int id);

    /**
     * Removes the resource.
     * 
     * @param resource
     *            The resource to remove.
     */
    public void remove(ProjectResource resource);

    /**
     * Removes the resource by its id.
     * 
     * @param Id
     *            Id of the resource to remove.
     */

    public void remove(ProjectResource resource, GPUndoManager myUndoManager);

    //public void removeById(int Id);

    /**
     * Retrieves a list of all resources available.
     * 
     * @return ArrayList filled with ProjectResource ancestors.
     * @see ProjectResource
     */
    public List getResources();
    
    public ProjectResource[] getResourcesArray();

    /**
     * Loads resources from the InputStreamReader. All resources already stored
     * in the Resourcemanager are lost and will be replace with the resources
     * loaded from the stream.
     * 
     * @return The ArrayLisr returned contains all ProjectResource ancestor that
     *         were read from the InputStreamReader.
     * @param source
     *            The InputStreamReader from which the data will be read. The
     *            format and kind of data read is subject of the class
     *            implementing this interface.
     */
    // public ArrayList load (InputStream source);
    /**
     * Writes all resources stored in the OutputStreamWriter. The format and
     * kind of data written in the stream are subject of the class that
     * implements this interface.
     * 
     * @param target
     *            Stream to write the data to.
     */
    public void save(OutputStream target);

    /** Removes all resources from the manager. */
    public void clear();

    /**
     * Adds a new view of this manager
     * 
     * @param view
     */
    public void addView(ResourceView view);

    public Map importData(ResourceManager resourceManager);

	public CustomPropertyManager getCustomPropertyManager();

}
