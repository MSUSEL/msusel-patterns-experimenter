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
import org.apache.james.util.XMLResources;
import org.apache.james.services.UsersRepository;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;

import javax.mail.MessagingException;
import java.util.Properties;

/**
 * Subscribe handles the subscribe-confirm command.
 * It is configured by:
 * <pre>&lt;command name="subscribe-confirm" class="SubscribeConfirm"/&gt;</pre>
 *
 * <br />
 * <br />
 *
 * It uses the formatted text-based resources for its return mail body:
 * <ul>
 *  <li>subscribe-confirm
 *  <li>admincommands
 * </ul>
 *
 * <br />
 * <br />
 * After formatting the text, the message is delivered with {@link #sendStandardReply}
 *
 * <br />
 * <br />
 * This command basically sends the welcome message and adds the user to the mailing list.
 *
 * @version CVS $Revision: 1.1.2.3 $ $Date: 2004/03/15 03:54:20 $
 * @since 2.2.0
 * @see Subscribe
 */
public class SubscribeConfirm extends BaseCommand {

    //For resources
    protected XMLResources[] xmlResources;

    protected static final int SUBSCRIBE_CONFIRM = 0;
    protected static final int ADMIN_COMMANDS = 1;

    public void init(ICommandListservManager commandListservManager, Configuration configuration) throws ConfigurationException {
        super.init(commandListservManager, configuration);
        xmlResources = initXMLResources(new String[]{"subscribeConfirm", "admincommands"});
    }

    /**
     * After ensuring that the user isn't already subscribed, add the user to the
     * mailing list, and send a welcome message.
     *
     * <br />
     * <br />
     *
     * It uses the formatted text-based resources for its return mail body:
     * <ul>
     *  <li>{@link #SUBSCRIBE_CONFIRM}
     *  <li>{@link #ADMIN_COMMANDS}
     * </ul>
     *
     * @param mail
     * @throws MessagingException
     */
    public void onCommand(Mail mail) throws MessagingException {
        if (checkSubscriptionStatus(mail)) {
            getUsersRepository().addUser(mail.getSender().toString(), "");

            //send mail
            Properties props = getStandardProperties();
            props.put("SENDER_ADDR", mail.getSender().toString());

            String confirmationMail = xmlResources[SUBSCRIBE_CONFIRM].getString("text", props);
            String adminCommands = xmlResources[ADMIN_COMMANDS].getString("text", props);
            String subject = xmlResources[SUBSCRIBE_CONFIRM].getString("welcome.subscribe.address", props);

            sendStandardReply(mail, subject, confirmationMail + adminCommands, null);
        }
    }

    /**
     * Checks to see if this user is already subscribed, if so return false and send a message
     * @param mail
     * @return false if the user is subscribed, true otherwise
     * @throws MessagingException
     */
    protected boolean checkSubscriptionStatus(Mail mail) throws MessagingException {
        MailAddress mailAddress = mail.getSender();
        UsersRepository usersRepository = getUsersRepository();
        if (usersRepository.contains(mailAddress.toString())) {
            getCommandListservManager().onError(mail,
                    "Invalid request",
                    xmlResources[SUBSCRIBE_CONFIRM].getString("already.subscribed", getStandardProperties()));
            return false;
        }
        return true;
    }
}
