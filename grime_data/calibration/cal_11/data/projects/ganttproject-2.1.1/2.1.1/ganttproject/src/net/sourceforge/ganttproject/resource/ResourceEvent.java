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
 * Created on 17.07.2003
 *
 */
package net.sourceforge.ganttproject.resource;

import java.util.EventObject;

/**
 * @author bard
 */
public class ResourceEvent extends EventObject {
    /**
     * @param source
     */
    public ResourceEvent(ResourceManager mgr, ProjectResource resource) {
        super(mgr);
        myManager = mgr;
        myResource = resource;
        myResources = new ProjectResource[] { myResource };
    }

    public ResourceEvent(ResourceManager mgr, ProjectResource[] resources) {
        super(mgr);
        myManager = mgr;
        myResources = resources;
        myResource = resources.length > 0 ? resources[0] : null;
    }

    public ResourceManager getManager() {
        return myManager;
    }

    public ProjectResource getResource() {
        return myResource;
    }

    public ProjectResource[] getResources() {
        return myResources;
    }

    private ProjectResource[] myResources;

    private ResourceManager myManager;

    private ProjectResource myResource;

}
