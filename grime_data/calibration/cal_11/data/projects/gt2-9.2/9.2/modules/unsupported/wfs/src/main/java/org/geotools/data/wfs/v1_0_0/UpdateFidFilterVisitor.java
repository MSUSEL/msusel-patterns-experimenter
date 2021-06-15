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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
/**
 * Changes all the Fids in FidFilters to be translated to the concrete fid.
 * <p>
 *  WFS has a difficult situation where FIDs are not assigned until after a commit.
 *  So in order to make it easier for the end programmer this visitor will convert any fids
 *  that were returned to client programs before the commit to the final FID after the commit
 *  was made.
 *  </p>
 *  <p>
 *  This visitor will copy the provided filter and update any feature identifiers according
 *  to the provided mapping.
 *  
 * @author Jesse
 */
class UpdateFidFilterVisitor extends DuplicatingFilterVisitor {
	/** Map of BEFORE to AFTER */
	private Map<String,String> fidMap;

	/**
	 * Update a filter based on the provided map.
	 *  
	 * @param fidMap Map of before (keys) to after (values) used to update feature identifiers
	 */
	public UpdateFidFilterVisitor(Map<String,String> fidMap) {
		this.fidMap=fidMap;
	}
	@Override
	public Object visit(Id filter, Object extraData) {
	    Set<FeatureId> fidSet = new HashSet<FeatureId>();
	    for( Identifier identifier : filter.getIdentifiers() ){
	        String target = (String) identifier.getID();
	        String fid = getFinalFid( target );
	        fidSet.add( ff.featureId( fid ) );
	    }
	    return ff.id( fidSet );
	}

    /**
     * Returns the final version of the FID.  If a commit has changed the FID the new fid will be returned otherwise the same fid will be returned.  
     * 
     * @return the final version of the FID
     */
	public synchronized String getFinalFid(String fid) {
		String finalFid=(String) fidMap.get(fid);
		if( finalFid==null ){
			return fid;
		}
		return finalFid;
	}

}
