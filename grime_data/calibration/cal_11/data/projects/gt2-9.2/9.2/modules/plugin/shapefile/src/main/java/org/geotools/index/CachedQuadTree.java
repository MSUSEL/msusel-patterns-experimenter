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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.index;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.index.quadtree.Node;
import org.geotools.index.quadtree.QuadTree;
import org.geotools.index.quadtree.StoreException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * An experimental QIX in memory index cache. It loads the tree into a packed
 * memory structure that 
 * @author Andrea Aime - OpenGeo
 *
 *
 *
 * @source $URL$
 */
public class CachedQuadTree {
    static final DataDefinition DATA_DEFINITION = new DataDefinition("US-ASCII");
    static {
        DATA_DEFINITION.addField(Integer.class);
        DATA_DEFINITION.addField(Long.class);
    };
    
    MemoryNode root;
    Indices offsets;
    
    public CachedQuadTree(QuadTree tree) throws IOException {
    	offsets = new Indices();
        this.root = cloneAndTranslate(tree.getRoot(), tree.getIndexfile());
    }
    
    public Envelope getBounds() {
        return new Envelope(root.minx, root.maxx, root.miny, root.maxy);
    }

    private MemoryNode cloneAndTranslate(Node node, IndexFile indexfile) throws IOException {
    	// copy the shape ids and clean up
    	node.pack();
        int[] shapeIds = node.getShapesId();
        int start = -1;
        int end = -1;
        if(shapeIds != null && shapeIds.length > 0) {
        	start = offsets.size();
            // turn the shape ids into offsets so that we won't need to open the index file anymore
            for (int i = 0; i < shapeIds.length; i++) {
                offsets.add(indexfile.getOffsetInBytes(shapeIds[i]));
            }
            end = offsets.size();
        }
        node.clean();
        
        // recurse and then clean up the subnodes as well
        MemoryNode mem = new MemoryNode(node.getBounds(), start, end, node.getNumSubNodes());
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            mem.subnodes[i] =  cloneAndTranslate(node.getSubNode(i), indexfile);
        }
        node.clearSubNodes();
        
        return mem;
    }
    

    public CloseableIterator<Data> search(final Envelope bounds) throws StoreException {
        final Indices indices = new Indices();
        collectIndices(indices, root, bounds);
        indices.sort();
        final Data data = new Data(DATA_DEFINITION);
        return new CloseableIterator<Data>() {
            boolean read = true;
            int idx = 0;

            public void remove() {
                throw new UnsupportedOperationException();
            }
            
            public Data next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                
                read = true;
                return data;
            }
            
            public boolean hasNext() {
                if(!read) {
                    return true;
                }
                
                if(idx >= indices.size()) {
                    return false;
                }
                
                try {
                  data.clear();
                  data.addValue(0);
                  data.addValue((long) indices.get(idx));
                  idx++;
                  read = false;
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
                
                return true;
            }
            
            public void close() throws IOException {
                indices.clear();
            }
        };
    }
    
    void collectIndices(Indices indices, MemoryNode node, Envelope bounds) throws StoreException {
        if(!node.intersects(bounds)) {
            return;
        }
        
        if(node.start > -1 && node.end >= node.start) {
        	for(int i = node.start; i < node.end; i++) {
        		indices.add(offsets.get(i));
        	}
        }
        
        for (MemoryNode child : node.subnodes) {
            collectIndices(indices, child, bounds);
        }
    }

    /**
     * An efficient wrapper around an array of integers
     */
    class Indices {
        /**
         * The current coordinate
         */
        int curr;
        
        /**
         * The ordinates holder
         */
        int[] indices;
        
        public Indices() {
            indices = new int[100];
            curr = -1;
        }
        
        /**
         * The number of coordinates
         * @return
         */
        int size() {
            return curr + 1;
        }
        
        /**
         * Adds a coordinate to this list
         * @param x
         * @param y
         */
        void add(int index) {
            curr++;
            if((curr * 2 + 1) >= indices.length) {
                int newSize = indices.length * 3 / 2;
                if(newSize < 10) {
                    newSize = 10;
                }
                int[] resized = new int[newSize];
                System.arraycopy(indices, 0, resized, 0, indices.length);
                indices = resized;
            }
            indices[curr] = index;
        }
        
        /**
         * Resets the indices
         */
        void clear() {
            curr = -1;
        }
        
        int get(int position) {
            return indices[position];
        }
        
        void sort() {
            Arrays.sort(indices, 0, curr + 1);
        }
    }

    static class MemoryNode {
    	float minx, miny, maxx, maxy;
    	int start;
    	int end;
    	MemoryNode[] subnodes;
		
    	public MemoryNode(Envelope envelope, int start, int end, int numSubnodes) {
			this.minx = (float) envelope.getMinX();
			this.miny = (float) envelope.getMinY();
			this.maxx = (float) envelope.getMaxX();
			this.maxy = (float) envelope.getMaxY();
			this.start = start;
			this.end = end;
			this.subnodes = new MemoryNode[numSubnodes];
		}

		public boolean intersects(Envelope bounds) {
			// TODO: optimize this one
			return new Envelope(minx, maxx, miny, maxy).intersects(bounds);
		}
    	
    }

}
