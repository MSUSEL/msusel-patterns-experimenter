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

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * <p>Class <code>FolderProcessor</code> opens a Folder and iterates over all
 * of the Messages, delegating their processing to 
 * <code>MessageProcessor</code>.</p>
 * 
 * <p>If isRecurse(), all subfolders are fetched recursively.</p>
 * 
 * <p>Creation Date: 25-May-03</p>
 *
 */
public class FolderProcessor extends ProcessorAbstract
{
    /**
     * The fetched folder
     */ 
    private Folder fieldFolder;
    
    private Boolean fieldMarkSeenPermanent;

    /**
     * Constructor for FolderProcessor.
     * @param folder The folder to be fetched
     * @param account The account being processed
     */
    protected FolderProcessor(Folder folder, Account account)
    {
        super(account);
        setFolder(folder);
    }
    
    /**
     * Method process opens a Folder, fetches the Envelopes for all of its 
     * Messages, creates a <code>MessageProcessor</code> and runs it to process
     * each message.
     * 
     * @see org.apache.james.fetchmail.ProcessorAbstract#process()
     */
    public void process() throws MessagingException
    {
        int messagesProcessed = 0;
        int messageCount = 0;
        try
        {
            // open the folder            
            try
            {
                open();
            }
            catch (MessagingException ex)
            {
                getLogger().error(
                    getFetchTaskName() + " Failed to open folder!");
                throw ex;
            }

            // Lock the folder while processing each message
            synchronized (getFolder())
            {
                messageCount = getFolder().getMessageCount();
                for (int i = 1; i <= messageCount; i++)
                {
                    MimeMessage message =
                        (MimeMessage) getFolder().getMessage(i);
                    if (isFetchAll() || !isSeen(message))
                    {
                        try
                        {
                            new MessageProcessor(message, getAccount())
                                .process();
                            messagesProcessed++;
                        }
                        // Catch and report an exception but don't rethrow it, 
                        // allowing subsequent messages to be processed.                    
                        catch (Exception ex)
                        {
                            StringBuffer logMessageBuffer =
                                new StringBuffer("Exception processing message ID: ");
                            logMessageBuffer.append(message.getMessageID());
                            getLogger().error(logMessageBuffer.toString(), ex);
                        }
                    }
                }
            }
        }
        catch (MessagingException mex)
        {
            getLogger().error(
                "A MessagingException has terminated fetching messages for this folder",
                mex);
        }
        finally
        {
            // Close the folder
            try
            {
                close();
            }
            catch (MessagingException ex)
            {
                // No-op
            }
            StringBuffer logMessageBuffer = new StringBuffer("Processed ");
            logMessageBuffer.append(messagesProcessed);
            logMessageBuffer.append(" messages of ");
            logMessageBuffer.append(messageCount);
            logMessageBuffer.append(" in folder '");
            logMessageBuffer.append(getFolder().getName());
            logMessageBuffer.append("'");
            getLogger().info(logMessageBuffer.toString());
        }

        // Recurse through sub-folders if required
        try
        {
            if (isRecurse())
                recurse();
        }
        catch (MessagingException mex)
        {
            getLogger().error(
                "A MessagingException has terminated recursing through sub-folders",
                mex);
        }

        return;
    }
    
    /**
     * Method close.
     * @throws MessagingException
     */
    protected void close() throws MessagingException
    {
        if (null != getFolder() && getFolder().isOpen())
            getFolder().close(true);
    }   
    
    /**
     * Method recurse.
     * @throws MessagingException
     */
    protected void recurse() throws MessagingException
    {
        if ((getFolder().getType() & Folder.HOLDS_FOLDERS)
            == Folder.HOLDS_FOLDERS)
        {
            // folder contains subfolders...
            Folder folders[] = getFolder().list();

            for (int i = 0; i < folders.length; i++)
            {
                new FolderProcessor(folders[i], getAccount()).process();
            }

        }
    }   
    
    /**
     * Method open.
     * @throws MessagingException
     */
    protected void open() throws MessagingException
    {
        int openFlag = Folder.READ_WRITE;
        
        if (isOpenReadOnly())
            openFlag = Folder.READ_ONLY;

        getFolder().open(openFlag);                 
    }           

    /**
     * Returns the folder.
     * @return Folder
     */
    protected Folder getFolder()
    {
        return fieldFolder;
    }
    
    /**
     * Answer if <code>aMessage</code> has been SEEN.
     * @param aMessage
     * @return boolean
     * @throws MessagingException
     */
    protected boolean isSeen(MimeMessage aMessage) throws MessagingException
    {
        boolean isSeen = false;
        if (isMarkSeenPermanent().booleanValue())
            isSeen = aMessage.isSet(Flags.Flag.SEEN);
        else
            isSeen = handleMarkSeenNotPermanent(aMessage);
        return isSeen;
    }

    /**
     * Answer the result of computing markSeenPermanent.
     * @return Boolean
     */
    protected Boolean computeMarkSeenPermanent()
    {
        return new Boolean(
            getFolder().getPermanentFlags().contains(Flags.Flag.SEEN));
    }

    /**
     * <p>Handler for when the folder does not support the SEEN flag.
     * The default behaviour implemented here is to answer the value of the
     * SEEN flag anyway.</p>
     * 
     * <p>Subclasses may choose to override this method and implement their own
     *  solutions.</p>
     *
     * @param aMessage
     * @return boolean 
     * @throws MessagingException
     */
    protected boolean handleMarkSeenNotPermanent(MimeMessage aMessage)
        throws MessagingException
    {
        return aMessage.isSet(Flags.Flag.SEEN);
    }    

    /**
     * Sets the folder.
     * @param folder The folder to set
     */
    protected void setFolder(Folder folder)
    {
        fieldFolder = folder;
    }
    
    /**
     * Returns the isMarkSeenPermanent.
     * @return Boolean
     */
    protected Boolean isMarkSeenPermanent()
    {
        Boolean markSeenPermanent = null;
        if (null == (markSeenPermanent = isMarkSeenPermanentBasic()))
        {
            updateMarkSeenPermanent();
            return isMarkSeenPermanent();
        }    
        return markSeenPermanent;
    }
    
    /**
     * Returns the markSeenPermanent.
     * @return Boolean
     */
    private Boolean isMarkSeenPermanentBasic()
    {
        return fieldMarkSeenPermanent;
    }    

    /**
     * Sets the markSeenPermanent.
     * @param markSeenPermanent The isMarkSeenPermanent to set
     */
    protected void setMarkSeenPermanent(Boolean markSeenPermanent)
    {
        fieldMarkSeenPermanent = markSeenPermanent;
    }
    
    /**
     * Updates the markSeenPermanent.
     */
    protected void updateMarkSeenPermanent()
    {
        setMarkSeenPermanent(computeMarkSeenPermanent());
    }    

}
