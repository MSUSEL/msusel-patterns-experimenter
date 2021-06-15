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
package org.geotools.renderer3d.terrainblock;

import org.geotools.renderer3d.provider.texture.impl.TextureProvider;
import org.geotools.renderer3d.utils.quadtree.NodeDataFactory;
import org.geotools.renderer3d.utils.quadtree.QuadTreeNode;

/**
 * Creates terrain blocks for areas specified by quad tree nodes.
 *
 * @author Hans H�ggstr�m
 */
public final class TerrainBlockFactory
        implements NodeDataFactory<TerrainBlock>
{

    //======================================================================
    // Private Fields

    //======================================================================
    // Private Fields
    private final int myNumberOfGridsPerSide;

    private final TextureProvider myTextureProvider;
    private final int myTextureSize;

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Constructors

    /**
     * @param numberOfGridsPerSide number of grid cells along the side of a TerrainBlock.
     * @param textureProvider      something that supplies the textures for terrain blocks.
     * @param textureSize          number of pixels per side for the texture.
     */
    public TerrainBlockFactory( final int numberOfGridsPerSide,
                                final TextureProvider textureProvider,
                                final int textureSize )
    {
        myNumberOfGridsPerSide = numberOfGridsPerSide;
        myTextureProvider = textureProvider;
        myTextureSize = textureSize;

    }

    //----------------------------------------------------------------------
    // NodeDataFactory Implementation

    //----------------------------------------------------------------------
    // NodeDataFactory ImplementationK}}

    public TerrainBlock createNodeDataObject( final QuadTreeNode<TerrainBlock> node )
    {
        final TerrainBlockImpl terrainBlock = new TerrainBlockImpl( node,
                                                                    myNumberOfGridsPerSide,
                                                                    myTextureSize,
                                                                    myTextureProvider );

        return terrainBlock;
    }


    public TerrainBlock reuseNodeDataObject( final QuadTreeNode<TerrainBlock> node,
                                             final TerrainBlock existingTerrainBlock )
    {
        if ( existingTerrainBlock == null )
        {
            return createNodeDataObject( node );
        }
        else
        {

            existingTerrainBlock.updateDerivedData();

            return existingTerrainBlock;
        }
    }

    public void onDataObjectUnused( final TerrainBlock nodeData )
    {
        // DEBUG
        if ( nodeData != null )
        {
            nodeData.clearPicture();
        }
    }

}
