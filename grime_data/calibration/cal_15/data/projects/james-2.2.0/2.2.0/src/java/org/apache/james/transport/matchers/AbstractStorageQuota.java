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
package org.apache.james.transport.matchers;

import java.util.Iterator;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;

import org.apache.james.Constants;
import org.apache.james.core.MailImpl;
import org.apache.james.services.MailServer;
import org.apache.james.services.MailRepository;
import org.apache.james.services.UsersStore;
import org.apache.james.services.UsersRepository;
import org.apache.james.services.JamesUser;

import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.MailetContext;

import javax.mail.MessagingException;

/**
 * <P>Abstract matcher checking whether a recipient has exceeded a maximum allowed
 * <I>storage</I> quota for messages standing in his inbox.</P>
 * <P>"Storage quota" at this level is still an abstraction whose specific interpretation
 * will be done by subclasses (e.g. could be specific for each user or common to all of them).</P> 
 *
 * @version CVS $Revision: 1.1.2.4 $ $Date: 2004/02/26 20:37:00 $
 * @since 2.2.0
 */
abstract public class AbstractStorageQuota extends AbstractQuotaMatcher { 

    private MailServer mailServer;

    /** The store containing the local user repository. */
    private UsersStore usersStore;

    /** The user repository for this mail server.  Contains all the users with inboxes
     * on this server.
     */
    private UsersRepository localusers;

    /**
     * Standard matcher initialization.
     * Overriding classes must do a <CODE>super.init()</CODE>.
     */
    public void init() throws MessagingException {
        super.init();
        ComponentManager compMgr = (ComponentManager)getMailetContext().getAttribute(Constants.AVALON_COMPONENT_MANAGER);
        try {
            mailServer = (MailServer) compMgr.lookup(MailServer.ROLE);
        } catch (ComponentException e) {
            log("Exception in getting the MailServer: " + e.getMessage() + e.getRole());
        }        
        try {
            usersStore = (UsersStore)compMgr.lookup(UsersStore.ROLE);
        } catch (ComponentException e) {
            log("Exception in getting the UsersStore: " + e.getMessage() + e.getRole());
        }        
        localusers = (UsersRepository)usersStore.getRepository("LocalUsers");
    }

    /** 
     * Checks the recipient.
     * Does a <CODE>super.isRecipientChecked</CODE> and checks that the recipient
     * is a known user in the local server.
     * If a subclass overrides this method it should "and" <CODE>super.isRecipientChecked</CODE>
     * to its check.
     *
     * @param recipient the recipient to check
     */    
    protected boolean isRecipientChecked(MailAddress recipient) throws MessagingException {
        MailetContext mailetContext = getMailetContext();
        return super.isRecipientChecked(recipient) && (mailetContext.isLocalServer(recipient.getHost()) && mailetContext.isLocalUser(recipient.getUser()));
    }

    /** 
     * Gets the storage used in the recipient's inbox.
     *
     * @param recipient the recipient to check
     */    
    protected long getUsed(MailAddress recipient, Mail _) throws MessagingException {
        long size = 0;
        MailRepository userInbox = mailServer.getUserInbox(getPrimaryName(recipient.getUser()));
        for (Iterator it = userInbox.list(); it.hasNext(); ) {
            String key = (String) it.next();
            MailImpl mc = userInbox.retrieve(key);
            // Retrieve can return null if the mail is no longer in the store.
            if (mc != null) try {
                size += mc.getMessageSize();
            } catch (Throwable e) {
                // MailRepository.retrieve() does NOT lock the message.
                // It could be deleted while we're looping.
                log("Exception in getting message size: " + e.getMessage());
            }
        }
        return size;
    }

    /**
     * Gets the main name of a local customer, handling aliases.
     *
     * @param originalUsername the user name to look for; it can be already the primary name or an alias
     * @return the primary name, or originalUsername unchanged if not found
     */
    protected String getPrimaryName(String originalUsername) {
        String username;
        try {
            username = localusers.getRealName(originalUsername);
            JamesUser user = (JamesUser) localusers.getUserByName(username);
            if (user.getAliasing()) {
                username = user.getAlias();
            }
        }
        catch (Exception e) {
            username = originalUsername;
        }
        return username;
    }
    
}
