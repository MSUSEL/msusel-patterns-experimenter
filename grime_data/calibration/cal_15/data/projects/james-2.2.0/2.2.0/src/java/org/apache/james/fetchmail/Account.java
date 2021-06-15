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

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;
import javax.mail.internet.ParseException;

import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.mailet.MailAddress;

/**
 * <p>Class <code>Account</code> encapsulates the account details required to
 * fetch mail from a message store.</p>
 * 
 * <p>Instances are <code>Comparable</code> based on their sequence number.</p>
 * 
 * <p>Creation Date: 05-Jun-03</p>
 */
class Account implements Comparable
{
    /**
     * The user password for this account
     */
    private String fieldPassword;     

    /**
     * The user to send the fetched mail to
     */
    private MailAddress fieldRecipient;  

    /**
     * The user name for this account
     */
    private String fieldUser;

    /**
     * The ParsedConfiguration
     */    
    private ParsedConfiguration fieldParsedConfiguration;
    
    /**
     * List of MessageIDs for which processing has been deferred
     * because the intended recipient could not be found.
     */
    private List fieldDeferredRecipientNotFoundMessageIDs;    
    
    /**
     * The sequence number for this account
     */
    private int fieldSequenceNumber;
    
    /**
     * Ignore the recipient deduced from the header and use 'fieldRecipient'
     */
    private boolean fieldIgnoreRecipientHeader;     

    /**
     * The JavaMail Session for this Account.
     */ 
    
    private Session fieldSession;
    /**
     * Constructor for Account.
     */
    private Account()
    {
        super();
    }
    
    /**
     * Constructor for Account.
     * 
     * @param sequenceNumber
     * @param parsedConfiguration
     * @param user
     * @param password
     * @param recipient
     * @param ignoreRecipientHeader
     * @param session
     * @throws ConfigurationException
     */
    
    public Account(
        int sequenceNumber,
        ParsedConfiguration parsedConfiguration,
        String user,
        String password,
        String recipient,
        boolean ignoreRecipientHeader,
        Session session)
        throws ConfigurationException
    {
        this();
        setSequenceNumber(sequenceNumber);
        setParsedConfiguration(parsedConfiguration);
        setUser(user);
        setPassword(password);
        setRecipient(recipient);
        setIgnoreRecipientHeader(ignoreRecipientHeader);
        setSession(session);
    }   

    /**
     * Returns the password.
     * @return String
     */
    public String getPassword()
    {
        return fieldPassword;
    }

    /**
     * Returns the recipient.
     * @return MailAddress
     */
    public MailAddress getRecipient()
    {
        return fieldRecipient;
    }

    /**
     * Returns the user.
     * @return String
     */
    public String getUser()
    {
        return fieldUser;
    }

    /**
     * Sets the password.
     * @param password The password to set
     */
    protected void setPassword(String password)
    {
        fieldPassword = password;
    }

    /**
     * Sets the recipient.
     * @param recipient The recipient to set
     */
    protected void setRecipient(MailAddress recipient)
    {
        fieldRecipient = recipient;
    }
    
    /**
     * Sets the recipient.
     * @param recipient The recipient to set
     */
    protected void setRecipient(String recipient) throws ConfigurationException
    {
        if (null == recipient)
        {
            fieldRecipient = null;
            return;
        }
            
        try
        {
            setRecipient(new MailAddress(recipient));
        }
        catch (ParseException pe)
        {
            throw new ConfigurationException(
                "Invalid recipient address specified: " + recipient);
        }
    }   


    

    /**
     * Sets the user.
     * @param user The user to set
     */
    protected void setUser(String user)
    {
        fieldUser = user;
    }

    /**
     * Sets the ignoreRecipientHeader.
     * @param ignoreRecipientHeader The ignoreRecipientHeader to set
     */
    protected void setIgnoreRecipientHeader(boolean ignoreRecipientHeader)
    {
        fieldIgnoreRecipientHeader = ignoreRecipientHeader;
    }

    /**
     * Returns the ignoreRecipientHeader.
     * @return boolean
     */
    public boolean isIgnoreRecipientHeader()
    {
        return fieldIgnoreRecipientHeader;
    }

    /**
     * Returns the sequenceNumber.
     * @return int
     */
    public int getSequenceNumber()
    {
        return fieldSequenceNumber;
    }

    /**
     * Sets the sequenceNumber.
     * @param sequenceNumber The sequenceNumber to set
     */
    protected void setSequenceNumber(int sequenceNumber)
    {
        fieldSequenceNumber = sequenceNumber;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer if this object is less
     * than, equal to, or greater than the specified object.
     * 
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(Object o)
    {
        return getSequenceNumber() - ((Account) o).getSequenceNumber();
    }

    /**
     * Returns the deferredRecipientNotFoundMessageIDs. lazily initialised.
     * @return List
     */
    public List getDeferredRecipientNotFoundMessageIDs()
    {
        List messageIDs = null;
        if (null
            == (messageIDs = getDeferredRecipientNotFoundMessageIDsBasic()))
        {
            updateDeferredRecipientNotFoundMessageIDs();
            return getDeferredRecipientNotFoundMessageIDs();
        }
        return messageIDs;
    }

    /**
     * Returns the deferredRecipientNotFoundMessageIDs.
     * @return List
     */
    private List getDeferredRecipientNotFoundMessageIDsBasic()
    {
        return fieldDeferredRecipientNotFoundMessageIDs;
    }
    
    /**
     * Returns a new List of deferredRecipientNotFoundMessageIDs.
     * @return List
     */
    protected List computeDeferredRecipientNotFoundMessageIDs()
    {
        return new ArrayList(16);
    }    
    
    /**
     * Updates the deferredRecipientNotFoundMessageIDs.
     */
    protected void updateDeferredRecipientNotFoundMessageIDs()
    {
        setDeferredRecipientNotFoundMessageIDs(computeDeferredRecipientNotFoundMessageIDs());
    }    

    /**
     * Sets the defferedRecipientNotFoundMessageIDs.
     * @param defferedRecipientNotFoundMessageIDs The defferedRecipientNotFoundMessageIDs to set
     */
    protected void setDeferredRecipientNotFoundMessageIDs(List defferedRecipientNotFoundMessageIDs)
    {
        fieldDeferredRecipientNotFoundMessageIDs =
            defferedRecipientNotFoundMessageIDs;
    }

    /**
     * Returns the parsedConfiguration.
     * @return ParsedConfiguration
     */
    public ParsedConfiguration getParsedConfiguration()
    {
        return fieldParsedConfiguration;
    }

    /**
     * Sets the parsedConfiguration.
     * @param parsedConfiguration The parsedConfiguration to set
     */
    protected void setParsedConfiguration(ParsedConfiguration parsedConfiguration)
    {
        fieldParsedConfiguration = parsedConfiguration;
    }

    /**
     * Returns the session.
     * @return Session
     */
    public Session getSession()
    {
        return fieldSession;
    }

    /**
     * Sets the session.
     * @param session The session to set
     */
    protected void setSession(Session session)
    {
        fieldSession = session;
    }

}
