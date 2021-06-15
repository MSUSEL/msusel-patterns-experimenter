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
package org.geotools.data.dxf.entities;

import java.io.EOFException;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import java.io.IOException;

import org.geotools.data.GeometryType;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.header.DXFBlock;
import org.geotools.data.dxf.header.DXFBlockReference;
import org.geotools.data.dxf.header.DXFLayer;
import org.geotools.data.dxf.header.DXFLineType;
import org.geotools.data.dxf.parser.DXFCodeValuePair;
import org.geotools.data.dxf.parser.DXFGroupCode;
import org.geotools.data.dxf.parser.DXFParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @source $URL$
 */
public class DXFDimension extends DXFBlockReference {

    private static final Log log = LogFactory.getLog(DXFDimension.class);
    public double _angle = 0;//50
    public String _dimension = "<>";//1
    public DXFPoint _point_WCS = new DXFPoint();//10,20

    public DXFDimension(DXFDimension newDimension) {
        this(newDimension._angle, newDimension._dimension, newDimension._point_WCS._point.x, newDimension._point_WCS._point.y,
                newDimension._refBlock, newDimension._blockName, newDimension.getRefLayer(), 0, newDimension.getColor(), newDimension.getLineType());

        setType(newDimension.getType());
        setStartingLineNumber(newDimension.getStartingLineNumber());
        setUnivers(newDimension.getUnivers());
    }

    public DXFDimension(double a, String dim, double x, double y, DXFBlock refBlock, String nomBlock, DXFLayer l, int visibility, int c, DXFLineType lineType) {
        super(c, l, visibility, null, nomBlock, refBlock);
        _angle = a;
        _dimension = dim;
        _point_WCS = new DXFPoint(x, y, c, null, visibility, 1);
        setName("DXFDimension");
    }

    public static DXFDimension read(DXFLineNumberReader br, DXFUnivers univers) throws IOException {
        String dimension = "", nomBlock = "";
        DXFDimension d = null;
        DXFLayer l = null;
        DXFBlock refBlock = null;
        double angle = 0, x = 0, y = 0;
        int visibility = 0, c = -1;
        DXFLineType lineType = null;

        int sln = br.getLineNumber();
        log.debug(">>Enter at line: " + sln);

        DXFCodeValuePair cvp = null;
        DXFGroupCode gc = null;

        boolean doLoop = true;
        while (doLoop) {
            cvp = new DXFCodeValuePair();
            try {
                gc = cvp.read(br);
            } catch (DXFParseException ex) {
                throw new IOException("DXF parse error" + ex.getLocalizedMessage());
            } catch (EOFException e) {
                doLoop = false;
                break;
            }

            switch (gc) {
                case TYPE:
                    String type = cvp.getStringValue();
                    // geldt voor alle waarden van type
                    br.reset();
                    doLoop = false;
                    break;
                case LAYER_NAME: //"8"
                    l = univers.findLayer(cvp.getStringValue());
                    break;
                case TEXT: //"1"
                    dimension = cvp.getStringValue();
                    break;
                case ANGLE_1: //"50"
                    angle = cvp.getDoubleValue();
                    break;
                case NAME: //"2"
                    nomBlock = cvp.getStringValue();
                    break;
                case LINETYPE_NAME: //"6"
                    lineType = univers.findLType(cvp.getStringValue());
                    break;
                case X_1: //"10"
                    x = cvp.getDoubleValue();
                    break;
                case Y_1: //"20"
                    y = cvp.getDoubleValue();
                    break;
                case VISIBILITY: //"60"
                    visibility = cvp.getShortValue();
                    break;
                case COLOR: //"62"
                    c = cvp.getShortValue();
                    break;
                default:
                    break;
            }
        }

        d = new DXFDimension(angle, dimension, x, y, refBlock, nomBlock, l, visibility, c, lineType);
        d.setType(GeometryType.UNSUPPORTED);
        d.setStartingLineNumber(sln);
        d.setUnivers(univers);
        univers.addRefBlockForUpdate(d);

        log.debug(d.toString(dimension, angle, nomBlock, x, y, visibility, c));
        log.debug(">>Exit at line: " + br.getLineNumber());
        return d;
    }

    public void updateGeometry(){
        // not supported
    }


    public String toString(String dimension, double angle, String nomBlock, double x, double y, int visibility, int c) {
        StringBuffer s = new StringBuffer();
        s.append("DXFDimension [");
        s.append("dimension: ");
        s.append(dimension + ", ");
        s.append("angle: ");
        s.append(angle + ", ");
        s.append("nameBlock: ");
        s.append(nomBlock + ", ");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y + ", ");
        s.append("visibility: ");
        s.append(visibility + ", ");
        s.append("color: ");
        s.append(c);
        s.append("]");
        return s.toString();
    }

    @Override
    public DXFEntity translate(double x, double y) {
        _point_WCS._point.x += x;
        _point_WCS._point.y += y;
        return this;
    }

    @Override
    public DXFEntity clone() {
        return new DXFDimension(this);
    }
}
