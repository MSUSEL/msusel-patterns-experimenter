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
package org.geotools.process.raster;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 *
 * @author Daniele Romagnoli, GeoSolutions
 *
 *
 * @source $URL$
 */
public class BaseCoverageAlgebraProcess {
    
    static final String MISMATCHING_ENVELOPE_MESSAGE = "coverageA and coverageB should share the same Envelope";
    
    static final String MISMATCHING_GRID_MESSAGE = "coverageA and coverageB should have the same gridRange";
    
    static final String MISMATCHING_CRS_MESSAGE = "coverageA and coverageB should share the same CoordinateReferenceSystem";
    
    private BaseCoverageAlgebraProcess() {
        
    }

    public static void checkCompatibleCoverages(GridCoverage2D coverageA, GridCoverage2D coverageB) throws ProcessException {
        if (coverageA == null || coverageB == null){
            String coveragesNull = coverageA == null ? (coverageB == null ? "coverageA and coverageB" : "coverageA") : "coverageB";  
            throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, coveragesNull));
        }
        
        // 
        // checking same CRS
        // 
        CoordinateReferenceSystem crsA = coverageA.getCoordinateReferenceSystem();
        CoordinateReferenceSystem crsB = coverageB.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(crsA, crsB)){
            MathTransform mathTransform = null;
            try {
                mathTransform = CRS.findMathTransform(crsA, crsB);
            } catch (FactoryException e) {
                throw new ProcessException("Exceptions occurred while looking for a mathTransform between the 2 coverage's CRSs", e );
            }
            if (mathTransform != null && !mathTransform.isIdentity()){
                throw new ProcessException(MISMATCHING_CRS_MESSAGE);
            }
        }
        
        // 
        // checking same Envelope and grid range
        // 
        Envelope envA = coverageA.getEnvelope();
        Envelope envB = coverageB.getEnvelope();
        if (!envA.equals(envB)) {
            throw new ProcessException(MISMATCHING_ENVELOPE_MESSAGE);
        }
        
        GridEnvelope gridRangeA = coverageA.getGridGeometry().getGridRange();
        GridEnvelope gridRangeB = coverageA.getGridGeometry().getGridRange();
        if (gridRangeA.getSpan(0) != gridRangeB.getSpan(0)
                || gridRangeA.getSpan(1) != gridRangeB.getSpan(1)) {
            throw new ProcessException(MISMATCHING_GRID_MESSAGE);
        }
    }

}
