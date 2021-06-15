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
package com.jaspersoft.ireport.designer.welcome;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class uses code from Piet Blok released under Apache License Version 2.0.
 *
 * @author gtoffoli
 */
public class TextLabel extends JLabel {

    private int maxWidth = 0;

    JLabel textLabel = null;
    JPanel offScreenPanel = null;

    @Override
    public final Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        if (getMaxWidth() > 0 && getMaxWidth() < preferred.width) {
            //preferred =  new Dimension(getMaxWidth(), preferred.height * (int)Math.ceil(preferred.width / (double)getMaxWidth()));
            preferred =  recalculatePreferredSize(maxWidth);
        }
        return preferred;
    }

    /**
     * @return the maxWidth
     */
    public int getMaxWidth() {
        return maxWidth;
    }

    /**
     * @param maxWidth the maxWidth to set
     */
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }



    private Dimension recalculatePreferredSize(int widthLimit) {
        if (textLabel == null)
        {
            textLabel = new JLabel(this.getText());
        }
        textLabel.setBorder(this.getBorder());
        textLabel.setIcon(this.getIcon());
        textLabel.setLocale(this.getLocale());
        textLabel.setDisabledIcon(this.getDisabledIcon());
        textLabel.setFont(this.getFont());
        textLabel.setHorizontalAlignment(this.getHorizontalAlignment());
        textLabel.setHorizontalTextPosition(this.getHorizontalTextPosition());
        textLabel.setVerticalAlignment(this.getVerticalAlignment());
        textLabel.setVerticalTextPosition(this.getVerticalTextPosition());
        textLabel.setIconTextGap(this.getIconTextGap());

        if (offScreenPanel == null)
        {
            offScreenPanel = new JPanel();
            offScreenPanel.setLayout(new BorderLayout());
            offScreenPanel.add(textLabel);
        }

        Dimension initialPreferred = offScreenPanel.getPreferredSize();
        offScreenPanel.setSize(widthLimit, 2 * initialPreferred.height
			+ initialPreferred.height * initialPreferred.width/ widthLimit);

        offScreenPanel.getLayout().layoutContainer(offScreenPanel);
        //offScreenPanel.paint(previewGraphics);

    	return offScreenPanel.getSize();

    }

}
