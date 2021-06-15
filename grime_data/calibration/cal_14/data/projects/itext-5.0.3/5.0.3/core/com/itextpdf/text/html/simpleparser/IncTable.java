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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author  psoares
 */
public class IncTable implements Element {
    private HashMap<String, String> props = new HashMap<String, String>();
    private ArrayList<ArrayList<PdfPCell>> rows = new ArrayList<ArrayList<PdfPCell>>();
    private ArrayList<PdfPCell> cols;
    /** Creates a new instance of IncTable */
    public IncTable(HashMap<String, String> props) {
        this.props.putAll(props);
    }

    public void addCol(PdfPCell cell) {
        if (cols == null)
            cols = new ArrayList<PdfPCell>();
        cols.add(cell);
    }

    public void addCols(ArrayList<PdfPCell> ncols) {
        if (cols == null)
            cols = new ArrayList<PdfPCell>(ncols);
        else
            cols.addAll(ncols);
    }

    public void endRow() {
        if (cols != null) {
            Collections.reverse(cols);
            rows.add(cols);
            cols = null;
        }
    }

    public ArrayList<ArrayList<PdfPCell>> getRows() {
        return rows;
    }

    public PdfPTable buildTable() {
        if (rows.isEmpty())
            return new PdfPTable(1);
        int ncol = 0;
        for (PdfPCell pc : rows.get(0)) {
            ncol += pc.getColspan();
        }
        PdfPTable table = new PdfPTable(ncol);
        String width = props.get("width");
        if (width == null)
            table.setWidthPercentage(100);
        else {
            if (width.endsWith("%"))
                table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
            else {
                table.setTotalWidth(Float.parseFloat(width));
                table.setLockedWidth(true);
            }
        }
        for (ArrayList<PdfPCell> col : rows) {
            for (PdfPCell pc : col) {
                table.addCell(pc);
            }
        }
        return table;
    }

    /**
     * @since 5.0.1
     */
    public ArrayList<Chunk> getChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @since 5.0.1
     */
    public boolean isContent() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean isNestable() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean process(ElementListener listener) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public int type() {
        // TODO Auto-generated method stub
        return 0;
    }

}
