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
package com.itextpdf.text.pdf.codec;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Modified from original LZWCompressor to change interface to passing a
 * buffer of data to be compressed.
 * @since 5.0.2
 */
public class LZWCompressor
{
    /** base underlying code size of data being compressed 8 for TIFF, 1 to 8 for GIF **/
    int codeSize_;

    /** reserved clear code based on code size **/
    int clearCode_;

    /** reserved end of data code based on code size **/
    int endOfInfo_;

    /** current number bits output for each code **/
    int numBits_;

    /** limit at which current number of bits code size has to be increased **/
    int limit_;

    /** the prefix code which represents the predecessor string to current input point **/
    short prefix_;

    /** output destination for bit codes **/
    BitFile bf_;

    /** general purpose LZW string table **/
    LZWStringTable lzss_;

    /** modify the limits of the code values in LZW encoding due to TIFF bug / feature **/
    boolean tiffFudge_;

    /**
	 * @param out destination for compressed data
	 * @param codeSize the initial code size for the LZW compressor
	 * @param TIFF flag indicating that TIFF lzw fudge needs to be applied
	 * @exception IOException if underlying output stream error
	 **/
    public LZWCompressor(OutputStream out, int codeSize, boolean TIFF) throws IOException
    {
        bf_ = new BitFile(out, !TIFF);	// set flag for GIF as NOT tiff
        codeSize_  = codeSize;
        tiffFudge_ = TIFF;
        clearCode_ = 1 << codeSize_;
        endOfInfo_ = clearCode_ + 1;
        numBits_   = codeSize_ + 1;

        limit_ = (1 << numBits_) - 1;
        if (tiffFudge_)
            --limit_;

        prefix_ = (short)0xFFFF;
        lzss_ = new LZWStringTable();
        lzss_.ClearTable(codeSize_);
        bf_.writeBits(clearCode_, numBits_);
    }

    /**
	 * @param buf data to be compressed to output stream
	 * @exception IOException if underlying output stream error
	 **/
    public void compress(byte[] buf, int offset, int length)
        throws IOException
    {
        int idx;
        byte c;
        short index;

        int maxOffset = offset + length;
        for (idx = offset; idx < maxOffset; ++idx)
            {
                c = buf[idx];
                if ((index = lzss_.FindCharString(prefix_, c)) != -1)
                    prefix_ = index;
                else
                    {
                        bf_.writeBits(prefix_, numBits_);
                        if (lzss_.AddCharString(prefix_, c) > limit_)
                            {
                                if (numBits_ == 12)
                                    {
                                        bf_.writeBits(clearCode_, numBits_);
                                        lzss_.ClearTable(codeSize_);
                                        numBits_ = codeSize_ + 1;
                                    }
                                else
                                    ++numBits_;

                                limit_ = (1 << numBits_) - 1;
                                if (tiffFudge_)
                                    --limit_;
                            }
                        prefix_ = (short)((short)c & 0xFF);
                    }
            }
    }

    /**
	 * Indicate to compressor that no more data to go so write out
	 * any remaining buffered data.
	 *
	 * @exception IOException if underlying output stream error
	 **/
    public void flush() throws IOException
    {
        if (prefix_ != -1)
            bf_.writeBits(prefix_, numBits_);
		
        bf_.writeBits(endOfInfo_, numBits_);
        bf_.flush();
    }
}
