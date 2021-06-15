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
import org.apache.james.services.UsersRepository;
import org.apache.james.transport.mailets.ICommandListservManager;
import org.apache.james.util.XMLResources;
import org.apache.mailet.Mail;

import javax.mail.MessagingException;
import java.util.Iterator;
import java.util.Properties;

/**
 * Info handles the info command.
 * It is configured by:
 * <pre>&lt;command name="info" class="Info"/&gt;</pre>
 *
 * <br />
 * <br />
 *
 * It uses the formatted text-based resources for its return mail body:
 * <ul>
 *  <li>header
 *  <li>info
 *  <li>admincommands
 * </ul>
 *
 * <br />
 * <br />
 * After formatting the text, the message is delivered with {@link #sendStandardReply}
 *
 * Todo: make displaying the current member list optional
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/03/15 03:54:20 $
 * @since 2.2.0
 */
public class Info extends BaseCommand {

    //For resources
    protected XMLResources[] xmlResources;

    protected static final int HEADER = 0;
    protected static final int INFO = 1;
    protected static final int ADMIN_COMMANDS = 2;

    public void init(ICommandListservManager commandListservManager, Configuration configuration) throws ConfigurationException {
        super.init(commandListservManager, configuration);
        xmlResources = initXMLResources(new String[]{"header", "info", "admincommands"});
    }

    /**
     * Process the info command using the following text resources:
     * <ul>
     *  <li>{@link #HEADER}
     *  <li>{@link #INFO}
     *  <li>{@link #ADMIN_COMMANDS}
     * </ul>
     *
     * @param mail
     */
    public void onCommand(Mail mail) throws MessagingException {
        //send info mail
        Properties props = getStandardProperties();
        props.put("MEMBER_LIST", getMemberList());

        StringBuffer plainTextMessage = new StringBuffer();
        String header = xmlResources[HEADER].getString("text", props);
        plainTextMessage.append(header);

        String infoMail = xmlResources[INFO].getString("text", props);
        plainTextMessage.append(infoMail);

        String adminCommands = xmlResources[ADMIN_COMMANDS].getString("text", props);
        plainTextMessage.append(adminCommands);

        String subject = xmlResources[INFO].getString("info.subject", props);

        sendStandardReply(mail, subject, plainTextMessage.toString(), null);
    }

    /**
     * Retrieve the current member list
     * @return the formatted member list
     *
     * @see #getUsersRepository
     */
    protected String getMemberList() {

        StringBuffer buffer = new StringBuffer(0x1000);
        buffer.append("\r\n");
        UsersRepository usersRepository = getUsersRepository();
        for (Iterator it = usersRepository.list(); it.hasNext();) {
            String userName = (String) it.next();
            buffer.append("    ").append(userName);
            buffer.append("\r\n");
        }

        return buffer.toString();
    }
}
