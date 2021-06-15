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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.metadata;
import javax.imageio.metadata.IIOMetadataFormat;

/**
 * Base Class used to set a node which is child of a parent having
 * {@link IIOMetadataFormat#CHILD_POLICY_CHOICE} as a policy.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 */
abstract class ChildChoice<T extends MetadataAccessor> extends MetadataAccessor {
    /**
     * A list of all the available children.
     */
    private final String[] availableChildPaths;

    /**
     * The selected child.
     */
    private int selectedChild;

    /**
     * The chosen child.
     */
    private T child;

    /**
     * Main constructor. It need to be initialized with the metadata object, the
     * parent path to which a child should be attached and, finally, the
     * available choices.
     * 
     * @param metadata
     *                the {@link ImageMetadata} inner object.
     * @param parentPath
     *                the path to which children need to be appended.
     * @param choices
     *                a {@code String}'s array of supported child paths.
     */
    protected ChildChoice(final SpatioTemporalMetadata metadata,
            final String parentPath, final String[] choices) {
        super(metadata, parentPath, null);
        this.availableChildPaths = choices;
    }

    protected ChildChoice(final MetadataAccessor parentNode,
            final String parentPath, final String[] choices) {
        super(parentNode, parentPath, null);
        this.availableChildPaths = choices;
    }

    /**
     * Returns the chosen child if this has been initialized. Throws an
     * {@code IllegalStateException} in case the child has not been initialized
     * yet.
     */
    public T getChild() throws IllegalStateException {
        if (child == null) {
            throw new IllegalStateException(
                    "Child node has not been initialized yet");
        }
        return child;
    }

    /**
     * Creates a new child and returns it. In case the node has already been
     * initialized, throws an {@code IllegalStateException}.
     * 
     * @param choice
     *                the index of the requested choice.
     * @return the added child.
     * @throws IllegalStateException
     *                 in case the child has already been initialized
     * @throws IndexOutOfBoundsException
     *                 in case the specified choice is out of range.
     */
    public T addChild(final int choice) throws IndexOutOfBoundsException,
            IllegalStateException {
        if (choice < 0 || choice >= availableChildPaths.length) {
            throw new IndexOutOfBoundsException(choice + " out of range");
        }
        if (child != null) {
            throw new IllegalStateException("Child node already initialized!");
        }
        child = newChild(availableChildPaths[choice]);
        selectedChild = choice;
        return child;
    }

    /**
     * Creates a new child at the specified index.
     */
    protected abstract T newChild(String choice);

    /**
     * Returns the selected child index.
     * 
     * @return selectedChild {link int}
     */
    public int getSelectedChoice() {
        return selectedChild;
    }
}
