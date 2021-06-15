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
package com.jaspersoft.ireport.designer.widgets.visitor;

import java.awt.Graphics2D;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRPrintFrame;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.convert.ConvertVisitor;
import net.sf.jasperreports.engine.convert.ReportConverter;
import net.sf.jasperreports.engine.export.TextRenderer;
import net.sf.jasperreports.engine.export.draw.DrawVisitor;
import net.sf.jasperreports.engine.export.draw.FrameDrawer;

/**
 *
 * @author gtoffoli
 */
public class ConfigurableDrawVisitor extends DrawVisitor {

    private ConvertVisitor convertVisitor = null;
    private ReportConverter reportConverter = null;
    private FrameDrawer frameDrawer = null;
    private Graphics2D grx = null;

    /**
	 *
	 */
	public ConfigurableDrawVisitor(JRReport report, Graphics2D grx)
	{
		this(new ReportConverter(report, true, true), grx);
	}

	/**
	 *
	 */
	public ConfigurableDrawVisitor(ReportConverter reportConverter, Graphics2D grx)
	{
		super( reportConverter, grx);
        //this.convertVisitor = new ConvertVisitor(reportConverter);
        this.grx = grx;
        this.reportConverter = reportConverter;
    }

    @Override
    public void visitFrame(JRFrame frame) {

       

        try
		{
            if (convertVisitor == null)
            {
                convertVisitor = new ConvertVisitor(reportConverter);
            }

            if (frameDrawer == null)
            {
                frameDrawer = new FrameDrawer(null, new TextRenderer(false, true));
                frameDrawer.setClip(true);
            }
            JRPrintFrame element = (JRPrintFrame)convertVisitor.getVisitPrintElement(frame);
            element.getElements().clear();
			frameDrawer.draw(
				grx,
				element,
				-frame.getX(),
				-frame.getY()
				);
		}
        catch (JRException e)
		{
            throw new JRRuntimeException(e);
		}


    }


    /**
	 *
	 */
    @Override
	public void setGraphics2D(Graphics2D grx)
	{
		this.grx = grx;
        super.setGraphics2D(grx);
	}



}
