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
package com.itextpdf.text.pdf.codec.wmf;

import java.util.ArrayList;
import java.util.Stack;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfContentByte;

public class MetaState {

    public static final int TA_NOUPDATECP = 0;
    public static final int TA_UPDATECP = 1;
    public static final int TA_LEFT = 0;
    public static final int TA_RIGHT = 2;
    public static final int TA_CENTER = 6;
    public static final int TA_TOP = 0;
    public static final int TA_BOTTOM = 8;
    public static final int TA_BASELINE = 24;

    public static final int TRANSPARENT = 1;
    public static final int OPAQUE = 2;

    public static final int ALTERNATE = 1;
    public static final int WINDING = 2;

    public Stack<MetaState> savedStates;
    public ArrayList<MetaObject> MetaObjects;
    public Point currentPoint;
    public MetaPen currentPen;
    public MetaBrush currentBrush;
    public MetaFont currentFont;
    public BaseColor currentBackgroundColor = BaseColor.WHITE;
    public BaseColor currentTextColor = BaseColor.BLACK;
    public int backgroundMode = OPAQUE;
    public int polyFillMode = ALTERNATE;
    public int lineJoin = 1;
    public int textAlign;
    public int offsetWx;
    public int offsetWy;
    public int extentWx;
    public int extentWy;
    public float scalingX;
    public float scalingY;


    /** Creates new MetaState */
    public MetaState() {
        savedStates = new Stack<MetaState>();
        MetaObjects = new ArrayList<MetaObject>();
        currentPoint = new Point(0, 0);
        currentPen = new MetaPen();
        currentBrush = new MetaBrush();
        currentFont = new MetaFont();
    }

    public MetaState(MetaState state) {
        setMetaState(state);
    }

    public void setMetaState(MetaState state) {
        savedStates = state.savedStates;
        MetaObjects = state.MetaObjects;
        currentPoint = state.currentPoint;
        currentPen = state.currentPen;
        currentBrush = state.currentBrush;
        currentFont = state.currentFont;
        currentBackgroundColor = state.currentBackgroundColor;
        currentTextColor = state.currentTextColor;
        backgroundMode = state.backgroundMode;
        polyFillMode = state.polyFillMode;
        textAlign = state.textAlign;
        lineJoin = state.lineJoin;
        offsetWx = state.offsetWx;
        offsetWy = state.offsetWy;
        extentWx = state.extentWx;
        extentWy = state.extentWy;
        scalingX = state.scalingX;
        scalingY = state.scalingY;
    }

    public void addMetaObject(MetaObject object) {
        for (int k = 0; k < MetaObjects.size(); ++k) {
            if (MetaObjects.get(k) == null) {
                MetaObjects.set(k, object);
                return;
            }
        }
        MetaObjects.add(object);
    }

    public void selectMetaObject(int index, PdfContentByte cb) {
        MetaObject obj = MetaObjects.get(index);
        if (obj == null)
            return;
        int style;
        switch (obj.getType()) {
            case MetaObject.META_BRUSH:
                currentBrush = (MetaBrush)obj;
                style = currentBrush.getStyle();
                if (style == MetaBrush.BS_SOLID) {
                    BaseColor color = currentBrush.getColor();
                    cb.setColorFill(color);
                }
                else if (style == MetaBrush.BS_HATCHED) {
                    BaseColor color = currentBackgroundColor;
                    cb.setColorFill(color);
                }
                break;
            case MetaObject.META_PEN:
            {
                currentPen = (MetaPen)obj;
                style = currentPen.getStyle();
                if (style != MetaPen.PS_NULL) {
                    BaseColor color = currentPen.getColor();
                    cb.setColorStroke(color);
                    cb.setLineWidth(Math.abs(currentPen.getPenWidth() * scalingX / extentWx));
                    switch (style) {
                        case MetaPen.PS_DASH:
                            cb.setLineDash(18, 6, 0);
                            break;
                        case MetaPen.PS_DASHDOT:
                            cb.setLiteral("[9 6 3 6]0 d\n");
                            break;
                        case MetaPen.PS_DASHDOTDOT:
                            cb.setLiteral("[9 3 3 3 3 3]0 d\n");
                            break;
                        case MetaPen.PS_DOT:
                            cb.setLineDash(3, 0);
                            break;
                        default:
                            cb.setLineDash(0);
                            break;
                    }
                }
                break;
            }
            case MetaObject.META_FONT:
            {
                currentFont = (MetaFont)obj;
                break;
            }
        }
    }

    public void deleteMetaObject(int index) {
        MetaObjects.set(index, null);
    }

    public void saveState(PdfContentByte cb) {
        cb.saveState();
        MetaState state = new MetaState(this);
        savedStates.push(state);
    }

    public void restoreState(int index, PdfContentByte cb) {
        int pops;
        if (index < 0)
            pops = Math.min(-index, savedStates.size());
        else
            pops = Math.max(savedStates.size() - index, 0);
        if (pops == 0)
            return;
        MetaState state = null;
        while (pops-- != 0) {
            cb.restoreState();
            state = savedStates.pop();
        }
        setMetaState(state);
    }

    public void cleanup(PdfContentByte cb) {
        int k = savedStates.size();
        while (k-- > 0)
            cb.restoreState();
    }

    public float transformX(int x) {
        return ((float)x - offsetWx) * scalingX / extentWx;
    }

    public float transformY(int y) {
        return (1f - ((float)y - offsetWy) / extentWy) * scalingY;
    }

    public void setScalingX(float scalingX) {
        this.scalingX = scalingX;
    }

    public void setScalingY(float scalingY) {
        this.scalingY = scalingY;
    }

    public void setOffsetWx(int offsetWx) {
        this.offsetWx = offsetWx;
    }

    public void setOffsetWy(int offsetWy) {
        this.offsetWy = offsetWy;
    }

    public void setExtentWx(int extentWx) {
        this.extentWx = extentWx;
    }

    public void setExtentWy(int extentWy) {
        this.extentWy = extentWy;
    }

    public float transformAngle(float angle) {
        float ta = scalingY < 0 ? -angle : angle;
        return (float)(scalingX < 0 ? Math.PI - ta : ta);
    }

    public void setCurrentPoint(Point p) {
        currentPoint = p;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public MetaBrush getCurrentBrush() {
        return currentBrush;
    }

    public MetaPen getCurrentPen() {
        return currentPen;
    }

    public MetaFont getCurrentFont() {
        return currentFont;
    }

    /** Getter for property currentBackgroundColor.
     * @return Value of property currentBackgroundColor.
     */
    public BaseColor getCurrentBackgroundColor() {
        return currentBackgroundColor;
    }

    /** Setter for property currentBackgroundColor.
     * @param currentBackgroundColor New value of property currentBackgroundColor.
     */
    public void setCurrentBackgroundColor(BaseColor currentBackgroundColor) {
        this.currentBackgroundColor = currentBackgroundColor;
    }

    /** Getter for property currentTextColor.
     * @return Value of property currentTextColor.
     */
    public BaseColor getCurrentTextColor() {
        return currentTextColor;
    }

    /** Setter for property currentTextColor.
     * @param currentTextColor New value of property currentTextColor.
     */
    public void setCurrentTextColor(BaseColor currentTextColor) {
        this.currentTextColor = currentTextColor;
    }

    /** Getter for property backgroundMode.
     * @return Value of property backgroundMode.
     */
    public int getBackgroundMode() {
        return backgroundMode;
    }

    /** Setter for property backgroundMode.
     * @param backgroundMode New value of property backgroundMode.
     */
    public void setBackgroundMode(int backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    /** Getter for property textAlign.
     * @return Value of property textAlign.
     */
    public int getTextAlign() {
        return textAlign;
    }

    /** Setter for property textAlign.
     * @param textAlign New value of property textAlign.
     */
    public void setTextAlign(int textAlign) {
        this.textAlign = textAlign;
    }

    /** Getter for property polyFillMode.
     * @return Value of property polyFillMode.
     */
    public int getPolyFillMode() {
        return polyFillMode;
    }

    /** Setter for property polyFillMode.
     * @param polyFillMode New value of property polyFillMode.
     */
    public void setPolyFillMode(int polyFillMode) {
        this.polyFillMode = polyFillMode;
    }

    public void setLineJoinRectangle(PdfContentByte cb) {
        if (lineJoin != 0) {
            lineJoin = 0;
            cb.setLineJoin(0);
        }
    }

    public void setLineJoinPolygon(PdfContentByte cb) {
        if (lineJoin == 0) {
            lineJoin = 1;
            cb.setLineJoin(1);
        }
    }

    public boolean getLineNeutral() {
        return lineJoin == 0;
    }

}
