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
package com.itextpdf.text.pdf;

/** An interface that can be used to retrieve the position of cells in <CODE>PdfPTable</CODE>.
 *
 * @author Paulo Soares
 */
public interface PdfPTableEvent {
    
    /** This method is called at the end of the table rendering. The text or graphics are added to
     * one of the 4 <CODE>PdfContentByte</CODE> contained in
     * <CODE>canvases</CODE>.<br>
     * The indexes to <CODE>canvases</CODE> are:<p>
     * <ul>
     * <li><CODE>PdfPTable.BASECANVAS</CODE> - the original <CODE>PdfContentByte</CODE>. Anything placed here
     * will be under the table.
     * <li><CODE>PdfPTable.BACKGROUNDCANVAS</CODE> - the layer where the background goes to.
     * <li><CODE>PdfPTable.LINECANVAS</CODE> - the layer where the lines go to.
     * <li><CODE>PdfPTable.TEXTCANVAS</CODE> - the layer where the text go to. Anything placed here
     * will be over the table.
     * </ul>
     * The layers are placed in sequence on top of each other.
     * <p>
     * The <CODE>widths</CODE> and <CODE>heights</CODE> have the coordinates of the cells.<br>
     * The size of the <CODE>widths</CODE> array is the number of rows.
     * Each sub-array in <CODE>widths</CODE> corresponds to the x column border positions where
     * the first element is the x coordinate of the left table border and the last
     * element is the x coordinate of the right table border. 
     * If colspan is not used all the sub-arrays in <CODE>widths</CODE>
     * are the same.<br>
     * For the <CODE>heights</CODE> the first element is the y coordinate of the top table border and the last
     * element is the y coordinate of the bottom table border.
     * @param table the <CODE>PdfPTable</CODE> in use
     * @param widths an array of arrays with the cells' x positions. It has the length of the number
     * of rows
     * @param heights an array with the cells' y positions. It has a length of the number
     * of rows + 1
     * @param headerRows the number of rows defined for the header.
     * @param rowStart the first row number after the header
     * @param canvases an array of <CODE>PdfContentByte</CODE>
     */    
    public void tableLayout(PdfPTable table, float widths[][], float heights[], int headerRows, int rowStart, PdfContentByte[] canvases);

}

