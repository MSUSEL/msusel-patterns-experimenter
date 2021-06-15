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
package org.apache.james.transport.mailets;

import org.apache.james.transport.mailets.listservcommands.IListServCommand;
import org.apache.james.services.UsersRepository;
import org.apache.james.util.XMLResources;
import org.apache.mailet.Mailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.avalon.framework.configuration.ConfigurationException;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Properties;

/**
 * ICommandListservManager is the interface that describes the functionality of any
 * command based list serv managers.
 *
 * In order to obtain a reference to one, you can call:
 * <pre>
 * ICommandListservManager mgr = (ICommandListservManager)mailetContext.getAttribute(ICommandListservManager.ID + listName);
 * </pre>
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/03/15 03:54:19 $
 * @since 2.2.0
 */
public interface ICommandListservManager extends Mailet {

    public static final String ID = ICommandListservManager.class.getName();

    /**
     * Get the name of this list
     * @param displayFormat is whether you want a display version of this or not
     * @return the official display name of this list
     */
    public String getListName(boolean displayFormat);

    /**
     * Gets the owner of this list
     * @return this is an address like listOwner@localhost
     */
    public String getListOwner();

    /**
     * Get the domain of the list
     * @return a string like localhost
     */
    public String getListDomain();

    /**
     * Get a specific command
     * @param name case in-sensitive
     * @return a {@link IListServCommand} if found, null otherwise
     */
    public IListServCommand getCommand(String name);

    /**
     * Get all the available commands
     * @return a map of {@link IListServCommand}s
     */
    public Map getCommands();

    /**
     * Based on the to address get a valid or command or null
     * @param mailAddress
     * @return IListServCommand or null
     */
    public IListServCommand getCommandTarget(MailAddress mailAddress);

    /**
     * Get the current user repository for this list serv
     * @return an instance of {@link UsersRepository} that is used for the member list of the list serv
     */
    public UsersRepository getUsersRepository();

    /**
     * An error occurred, send some sort of message to the sender
     * @param subject the subject of the message to send
     * @param mail
     * @param errorMessage
     */
    public void onError(Mail mail, String subject, String errorMessage) throws MessagingException;

    /**
     * @return the configuration file for the xml resources
     */
    public String getResourcesFile();

    /**
     * Use this to get standard properties for future calls to {@link org.apache.james.util.XMLResources}
     * @return properties with the "LIST_NAME" and the "DOMAIN_NAME" properties
     */
    public Properties getStandardProperties();

    /**
     * Initializes an array of resources
     * @param names such as 'header, footer' etc...
     * @return an initialized array of XMLResources
     * @throws org.apache.avalon.framework.configuration.ConfigurationException
     */
    public XMLResources[] initXMLResources(String[] names) throws ConfigurationException;
}
