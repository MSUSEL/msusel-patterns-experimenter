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
package org.geotools.data.dxf.parser;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Chris
 *
 *
 *
 * @source $URL$
 */
public class DXFCodeValuePair {
    private static final Log log = LogFactory.getLog(DXFCodeValuePair.class);

    private DXFGroupCode gc;
    private short shortValue = 0;
    private int intValue = 0;
    private String stringValue = null;
    private boolean booleanValue = false;
    private double doubleValue = 0.0;
    private long longValue = 0l;
    private String handleHexValue = null;
    private String idHexValue = null;
    private String binHexValue = null;

    public DXFGroupCode read(DXFLineNumberReader br) throws IOException, DXFParseException {

        br.mark();
        // 1ste regel van paar
        String regel = br.readLine();

        int gcInt;
        try {
            gcInt = Integer.parseInt(regel);
        } catch (NumberFormatException nfe) {
            throw new DXFParseException(br, "Unknown Group Code: " + regel);
        }

        gc = DXFGroupCode.getGroupCode(gcInt);

        // 2de regel van paar
        regel = br.readLine();

        switch (gc.toType()) {
            case STRING:
                stringValue = regel;
                break;
            case HANDLEHEX:
                handleHexValue = regel;
                break;
            case IDHEX:
                idHexValue = regel;
                break;
            case BINHEX:
                binHexValue = regel;
                break;
            case SHORT:
                shortValue = Short.parseShort(regel);
                break;
            case INTEGER:
                intValue = Integer.parseInt(regel);
                break;
            case LONG:
                longValue = Long.parseLong(regel);
                break;
            case BOOLEAN:
                booleanValue = Boolean.parseBoolean(regel);
                break;
            case DOUBLE:
                doubleValue = Double.parseDouble(regel);
                break;
            default:
                throw new DXFParseException(br, "Unknown value type for Group Code: " + gc);
        }


        return gc;
    }

    public DXFGroupCode getGc() {
        return gc;
    }

    public short getShortValue() {
        if (!DXFValueType.SHORT.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return shortValue;
    }

    public int getIntValue() {
        if (!DXFValueType.INTEGER.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return intValue;
    }

    public String getStringValue() {
        if (!DXFValueType.STRING.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return stringValue;
    }

    public boolean isBooleanValue() {
        if (!DXFValueType.BOOLEAN.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return booleanValue;
    }

    public double getDoubleValue() {
        if (!DXFValueType.DOUBLE.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return doubleValue;
    }

    public long getLongValue() {
        if (!DXFValueType.LONG.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return longValue;
    }

    public String getHandleHexValue() {
        if (!DXFValueType.HANDLEHEX.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return handleHexValue;
    }

    public String getIdHexValue() {
        if (!DXFValueType.IDHEX.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return idHexValue;
    }

    public String getBinHexValue() {
        if (!DXFValueType.BINHEX.equals(gc.toType())) {
            throw new Error("Wrong Value Type for Group Code!");
        }
        return binHexValue;
    }

}
