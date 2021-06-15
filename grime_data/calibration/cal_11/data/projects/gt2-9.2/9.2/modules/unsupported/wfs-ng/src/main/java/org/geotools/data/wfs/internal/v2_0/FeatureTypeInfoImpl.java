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
package org.geotools.data.wfs.internal.v2_0;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.XMLConstants;

import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.WGS84BoundingBoxType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.OutputFormatListType;

import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class FeatureTypeInfoImpl implements FeatureTypeInfo {

    private final FeatureTypeType eType;

    public FeatureTypeInfoImpl(FeatureTypeType eType) {
        this.eType = eType;
    }

    @Override
    public String getTitle() {
        return eType.getTitle() == null || eType.getTitle().isEmpty() ? null : String.valueOf(eType
                .getTitle().get(0));
    }

    @Override
    public Set<String> getKeywords() {
        @SuppressWarnings("unchecked")
        List<KeywordsType> keywords = eType.getKeywords();
        Set<String> ret;
        if (keywords == null) {
            ret = Collections.emptySet();
        } else {
            ret = new HashSet<String>();
            for (KeywordsType k : keywords) {
                @SuppressWarnings("unchecked")
                List<LanguageStringType> keyword = k.getKeyword();
                for (LanguageStringType ls : keyword) {
                    ret.add(ls.getValue());
                }
            }
            ret.remove(null);
        }
        return ret;
    }

    @Override
    public String getDescription() {
        return eType.getAbstract() == null || eType.getAbstract().isEmpty() ? null : String
                .valueOf(eType.getAbstract().get(0));
    }

    @Override
    public String getName() {
        return eType.getName().getLocalPart();
    }

    @Override
    public URI getSchema() {
        String namespaceURI = eType.getName().getNamespaceURI();
        try {
            return XMLConstants.NULL_NS_URI.equals(namespaceURI) ? null : new URI(namespaceURI);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        ReferencedEnvelope wgs84Bounds = getWGS84BoundingBox();
        CoordinateReferenceSystem crs = getCRS();
        if (null == crs) {
            return wgs84Bounds;
        }

        ReferencedEnvelope nativeBounds;
        try {
            nativeBounds = wgs84Bounds.transform(crs, true);
        } catch (TransformException e) {
            Loggers.MODULE.log(Level.WARNING, "Can't transform bounds of " + getName() + " to "
                    + getDefaultSRS(), e);
            nativeBounds = new ReferencedEnvelope(crs);
        } catch (FactoryException e) {
            Loggers.MODULE.log(Level.WARNING, "Can't transform bounds of " + getName() + " to "
                    + getDefaultSRS(), e);
            nativeBounds = new ReferencedEnvelope(crs);
        }
        return nativeBounds;
    }

    @Override
    public String getDefaultSRS() {
        return eType.getDefaultCRS();
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        CoordinateReferenceSystem crs = null;
        String defaultSRS = eType.getDefaultCRS();
        if (null != defaultSRS) {
            try {
                crs = CRS.decode(defaultSRS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return crs;
    }

    @Override
    public ReferencedEnvelope getWGS84BoundingBox() {

        List<WGS84BoundingBoxType> bboxList = eType.getWGS84BoundingBox();
        if (bboxList != null && bboxList.size() > 0) {
            WGS84BoundingBoxType bboxType = bboxList.get(0);
            @SuppressWarnings("unchecked")
            List<Double> lowerCorner = bboxType.getLowerCorner();
            @SuppressWarnings("unchecked")
            List<Double> upperCorner = bboxType.getUpperCorner();
            double minLon = (Double) lowerCorner.get(0);
            double minLat = (Double) lowerCorner.get(1);
            double maxLon = (Double) upperCorner.get(0);
            double maxLat = (Double) upperCorner.get(1);

            ReferencedEnvelope latLonBounds = new ReferencedEnvelope(minLon, maxLon, minLat,
                    maxLat, DefaultGeographicCRS.WGS84);

            return latLonBounds;
        }
        return null;
    }

    @Override
    public List<String> getOtherSRS() {
        return eType.getOtherCRS();
    }

    @Override
    public Set<String> getOutputFormats() {
        final OutputFormatListType outputFormats = eType.getOutputFormats();
        if (null == outputFormats) {
            return Collections.emptySet();
        }

        List<String> ftypeDeclaredFormats = outputFormats.getFormat();
        if (null == ftypeDeclaredFormats || ftypeDeclaredFormats.isEmpty()) {
            return Collections.emptySet();
        }

        return new HashSet<String>(ftypeDeclaredFormats);
    }
}
