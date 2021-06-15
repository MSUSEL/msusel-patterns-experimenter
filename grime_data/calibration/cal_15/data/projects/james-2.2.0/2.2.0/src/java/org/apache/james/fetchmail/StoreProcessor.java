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
package org.apache.james.fetchmail;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * <p>Class <code>StoreProcessor</code> connects to a message store, gets the
 * target Folder and delegates its processing to <code>FolderProcessor</code>.</p>
 * 
 * <p>Creation Date: 27-May-03</p>
 */
public class StoreProcessor extends ProcessorAbstract
{
    /**
     * Constructor for StoreProcessor.
     * @param account 
     */
    protected StoreProcessor(Account account)
    {
        super(account);        
    }
    
    /**
     * Method process connects to a Folder in a Message Store, creates a
     * <code>FolderProcessor</code> and runs it to process the messages in
     * the Folder.
     * 
     * @see org.apache.james.fetchmail.ProcessorAbstract#process()
     */
    public void process() throws MessagingException
    {
        Store store = null;
        Folder folder = null;

        StringBuffer logMessageBuffer =
            new StringBuffer("Starting fetching mail from server '");
        logMessageBuffer.append(getHost());
        logMessageBuffer.append("' for user '");
        logMessageBuffer.append(getUser());
        logMessageBuffer.append("' in folder '");
        logMessageBuffer.append(getJavaMailFolderName());
        logMessageBuffer.append("'");
        getLogger().info(logMessageBuffer.toString());

        try
        {
            // Get a Store object
            store = getSession().getStore(getJavaMailProviderName());

            // Connect
            if (getHost() != null
                || getUser() != null
                || getPassword() != null)
                store.connect(getHost(), getUser(), getPassword());
            else
                store.connect();

            // Get the Folder
            folder = store.getFolder(getJavaMailFolderName());
            if (folder == null)
                getLogger().error(getFetchTaskName() + " No default folder");

            // Process the Folder
            new FolderProcessor(folder, getAccount()).process();

        }
        catch (MessagingException ex)
        {
            getLogger().error(
                "A MessagingException has terminated processing of this Folder",
                ex);
        }
        finally
        {
            try
            {
                if (null != store && store.isConnected())
                    store.close();
            }
            catch (MessagingException ex)
            {
                getLogger().error(
                    "A MessagingException occured while closing the Store",
                    ex);
            }
            logMessageBuffer =
                new StringBuffer("Finished fetching mail from server '");
            logMessageBuffer.append(getHost());
            logMessageBuffer.append("' for user '");
            logMessageBuffer.append(getUser());
            logMessageBuffer.append("' in folder '");
            logMessageBuffer.append(getJavaMailFolderName());
            logMessageBuffer.append("'");
            getLogger().info(logMessageBuffer.toString());
        }
    }

}
