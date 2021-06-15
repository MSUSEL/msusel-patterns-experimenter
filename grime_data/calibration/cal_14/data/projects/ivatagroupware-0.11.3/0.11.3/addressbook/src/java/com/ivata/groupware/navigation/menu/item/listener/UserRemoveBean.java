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
package com.ivata.groupware.navigation.menu.item.listener;

 import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;

 /**
 * <p>This bean listens for events when a user is removed.</p>
 *
 * @since 2003-10-09
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 *
 * @ejb.bean
 *      name = "UserRemoveMenuItem"
 *      display-name = "UserRemoveMenuItem"
 *      acknowledge-mode = "Auto-acknowledge"
 *      destination-type = "javax.jms.Topic"
 *
 * @jboss.destination-jndi-name
 *      name = "topic/topicUserRemove"
 */
public class UserRemoveBean implements MessageDrivenBean, MessageListener {

  MessageDrivenContext messageDrivenContext;

  public void ejbCreate() {
  }

  public void ejbRemove() {
  }

  public void onMessage(final Message message) {
/** TODO
      // check this is an object message
      if (!ObjectMessage.class.isInstance(message)) {
          throw new EJBException("ERROR in folder.UserRemoveBean: unknown messaage class ("
            + message.getClass().getName()
            + ")");
      }
      ObjectMessage objectMessage = (ObjectMessage) message;
      UserEvent userEvent = null;
      try {
          userEvent = (UserEvent) objectMessage.getObject();
      } catch (JMSException e) {
          throw new EJBException(e);
      }
      String userName = userEvent.getUserName();
      try {
          // remove all menuItems which belong to this user
          java.util.Collection menuItems = menuItemHome.findByUserName(userName);
          for (Iterator i = menuItems.iterator(); i.hasNext(); ) {
              MenuItemLocal item = (MenuItemLocal) i.next();
              item.remove();
          }
      } catch (FinderException e) {
          throw new EJBException(e);
      } catch (NamingException e) {
          throw new EJBException(e);
      } catch (RemoveException e) {
          throw new EJBException(e);
      }
*/
  }

  public final void setMessageDrivenContext(final MessageDrivenContext messageDrivenContext) {
    this.messageDrivenContext = messageDrivenContext;
  }
}