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
package com.itextpdf.text.pdf.fonts.cmaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * This class represents a CMap file.
 *
 * @author Ben Litchfield (ben@benlitchfield.com)
 * @since	2.1.4
 */
public class CMap
{
    private List<CodespaceRange> codeSpaceRanges = new ArrayList<CodespaceRange>();
    private Map<Integer, String> singleByteMappings = new HashMap<Integer, String>();
    private Map<Integer, String> doubleByteMappings = new HashMap<Integer, String>();

    /**
     * Creates a new instance of CMap.
     */
    public CMap()
    {
        //default constructor
    }

    /**
     * This will tell if this cmap has any one byte mappings.
     *
     * @return true If there are any one byte mappings, false otherwise.
     */
    public boolean hasOneByteMappings()
    {
        return !singleByteMappings.isEmpty();
    }

    /**
     * This will tell if this cmap has any two byte mappings.
     *
     * @return true If there are any two byte mappings, false otherwise.
     */
    public boolean hasTwoByteMappings()
    {
        return !doubleByteMappings.isEmpty();
    }

    /**
     * This will perform a lookup into the map.
     *
     * @param code The code used to lookup.
     * @param offset The offset into the byte array.
     * @param length The length of the data we are getting.
     *
     * @return The string that matches the lookup.
     */
    public String lookup( byte[] code, int offset, int length )
    {

        String result = null;
        Integer key = null;
        if( length == 1 )
        {

            key = new Integer( code[offset] & 0xff );
            result = singleByteMappings.get( key );
        }
        else if( length == 2 )
        {
            int intKey = code[offset] & 0xff;
            intKey <<= 8;
            intKey += code[offset+1] & 0xff;
            key = new Integer( intKey );

            result = doubleByteMappings.get( key );
        }

        return result;
    }

    /**
     * This will add a mapping.
     *
     * @param src The src to the mapping.
     * @param dest The dest to the mapping.
     *
     * @throws IOException if the src is invalid.
     */
    public void addMapping( byte[] src, String dest ) throws IOException
    {
        if( src.length == 1 )
        {
            singleByteMappings.put( new Integer( src[0] & 0xff ), dest );
        }
        else if( src.length == 2 )
        {
            int intSrc = src[0]&0xFF;
            intSrc <<= 8;
            intSrc |= src[1]&0xFF;
            doubleByteMappings.put( new Integer( intSrc), dest );
        }
        else
        {
            throw new IOException(MessageLocalization.getComposedMessage("mapping.code.should.be.1.or.two.bytes.and.not.1", src.length));
        }
    }


    /**
     * This will add a codespace range.
     *
     * @param range A single codespace range.
     */
    public void addCodespaceRange( CodespaceRange range )
    {
        codeSpaceRanges.add( range );
    }

    /**
     * Getter for property codeSpaceRanges.
     *
     * @return Value of property codeSpaceRanges.
     */
    public List<CodespaceRange> getCodeSpaceRanges()
    {
        return codeSpaceRanges;
    }

}
