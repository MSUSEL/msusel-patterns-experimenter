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
package org.apache.james.transport.mailets.listservcommands;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.james.transport.mailets.ICommandListservManager;
import org.apache.mailet.Mail;

import javax.mail.MessagingException;

/**
 * IListServCommand is the interface that all pluggable list serv commands must implement.
 * The lifecycle of a IListServCommand will be controlled by the {@link ICommandListservManager}
 *
 * <br />
 * <br />
 * Requests sent to the CommandListservManager take the form of:
 * <pre>
 * &lt;listName&gt;-&lt;commandName&gt;@domain
 * </pre>
 * and if the commandName matches the command's name, then the {@link #onCommand} will be invoked.
 *
 * <br />
 * <br />
 * A typical command is configured:
 * <pre>
 * &lt;command name="subscribe" class="Subscribe"/&gt;
 * </pre>
 *
 * <br />
 * <br />
 * Typically, IListServCommands will format some text to reply with based off of resource files
 * and calls to {@link org.apache.james.util.XMLResources#getString}
 *
 * This allows you to customize the messages sent by these commands by editing text files and not editing the javacode.
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/03/15 03:54:20 $
 * @since 2.2.0
 * @see ICommandListservManager
 */
public interface IListServCommand {

    /**
     * The name of this command
     * specified by the 'name' parameter.
     * eg:
     * <pre>
     * &lt;command name="subscribe" class="Subscribe"/&gt;
     * </pre>
     * @return the name of this command
     */
    public String getCommandName();

    /**
     * Perform any required initialization
     * @param configuration
     * @throws ConfigurationException
     */
    public void init(ICommandListservManager commandListservManager, Configuration configuration) throws ConfigurationException;

    /**
     * Process this command to your hearts content
     * @param mail
     * @throws MessagingException
     */
    public void onCommand(Mail mail) throws MessagingException;
}
