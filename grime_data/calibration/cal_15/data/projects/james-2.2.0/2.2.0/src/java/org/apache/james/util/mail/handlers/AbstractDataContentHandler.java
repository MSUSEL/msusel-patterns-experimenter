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
package org.apache.james.util.mail.handlers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.activation.ActivationDataFlavor;
import javax.activation.DataContentHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;


/**
 * Abstract class providing common Data Handler behavior.
 */
public abstract class AbstractDataContentHandler implements DataContentHandler
{

    private ActivationDataFlavor fieldDataFlavor;

    /**
     * Default Constructor
     */
    public AbstractDataContentHandler()
    {
        super();
    }

    /**
     * Update the current DataFlavor.
     * 
     */    
    protected void updateDataFlavor()
    {
        setDataFlavor(computeDataFlavor());
    }

    /**
     * Compute an ActivationDataFlavor.
     * 
     * @return A new ActivationDataFlavor
     */
    abstract protected ActivationDataFlavor computeDataFlavor();

    protected void setDataFlavor(ActivationDataFlavor aDataFlavor)
    {
        fieldDataFlavor = aDataFlavor;
    }

    /**
     * @see javax.activation.DataContentHandler#getContent(javax.activation.DataSource)
     */
    public Object getContent(DataSource aDataSource) throws IOException
    {
        Object content = null;
        try
        {
            content = computeContent(aDataSource);
        }
        catch (MessagingException e)
        {
            // No-op
        }
        return content;
    }

    /**
     * Compute the content from aDataSource.
     * 
     * @param aDataSource
     * @return new Content built from the DataSource
     * @throws MessagingException
     */
    abstract protected Object computeContent(DataSource aDataSource)
            throws MessagingException;

    /**
     * @see javax.activation.DataContentHandler#getTransferData(java.awt.datatransfer.DataFlavor,
     *      javax.activation.DataSource)
     */
    public Object getTransferData(DataFlavor aDataFlavor, DataSource aDataSource)
            throws UnsupportedFlavorException, IOException
    {
        Object content = null;
        if (getDataFlavor().equals(aDataFlavor))
            content = getContent(aDataSource);
        return content;
    }

    /**
     * @see javax.activation.DataContentHandler#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors()
    {
        return new DataFlavor[]{getDataFlavor()};
    }

    /**
     * Get the DataFlavor, lazily initialised if required.
     * 
     * @return Returns the dataFlavor, lazily initialised.
     */
    protected ActivationDataFlavor getDataFlavor()
    {
        ActivationDataFlavor dataFlavor = null;
        if (null == (dataFlavor = getDataFlavorBasic()))
        {
            updateDataFlavor();
            return getDataFlavor();
        }
        return dataFlavor;
    }

    /**
     * Get the DataFlavor.
     * 
     * @return Returns the dataFlavor.
     */
    private ActivationDataFlavor getDataFlavorBasic()
    {
        return fieldDataFlavor;
    }

}
