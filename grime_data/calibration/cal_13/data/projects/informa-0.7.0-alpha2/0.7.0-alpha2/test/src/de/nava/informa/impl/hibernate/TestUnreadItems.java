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
//
// Informa -- RSS Library for Java
// Copyright (c) 2002, 2003 by Niko Schmuck
//
// Niko Schmuck
// http://sourceforge.net/projects/informa
// mailto:niko_schmuck@users.sourceforge.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE. If the license is not included with this distribution,
// you may find a copy at the FSF web site at 'www.gnu.org' or 'www.fsf.org',
// or you may write to the Free Software Foundation, 675 Mass Ave, Cambridge,
// MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//

// $Id: TestUnreadItems.java,v 1.7 2006/12/04 23:43:26 italobb Exp $

package de.nava.informa.impl.hibernate;

import java.net.URL;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.utils.InformaHibernateTestCase;

/**
 * Test for making channel categories persistent while using the hibernate
 * mapping backend.
 *
 * @author Niko Schmuck
 */
public class TestUnreadItems extends InformaHibernateTestCase {

  private static Log logger = LogFactory.getLog(TestUnreadItems.class);

  public TestUnreadItems(String name) {
    super("TestUnreadItems", name);
  }

  public void testUnreadItems() throws Exception {
    ChannelBuilder builder = new ChannelBuilder(session);
    int chId = -1;
    String chanName = "Unread Tester";
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      ChannelIF channel = builder.createChannel(chanName);
      channel.setDescription("Test Channel: " + chanName);
      session.save(channel);
      // Add items, with some marked read and some marked unread
      int items;
      for (items = 0; items < 20; items++) {
        boolean unreadflag = ((items > 0 && items < 5))
          || (items > 10 && items < 15);
        String desc = unreadflag ? "UnreadItem" : "ReadItem";
        ItemIF anItem = builder.createItem(channel, "Item: " + items, desc, new URL("http://www.sf.net"));
        anItem.setUnRead(unreadflag);
      }
      session.save(channel);
      chId = (int) channel.getId();
      tx.commit();
    }
    catch (HibernateException he) {
      logger.warn("trying to rollback the transaction");
      if (tx != null) tx.rollback();
      throw he;
    }
    assertTrue("No valid channel created.", chId >= 0);

    // -- try to retrieve channel and check the unread statuses
    try {
      logger.info("Searching for channel " + chId);
      Object result = session.get(Channel.class, new Long(chId));
      assertNotNull(result);
      ChannelIF c = (ChannelIF) result;
      logger.info("retrieved channel --> " + c);

      // now check unread settings
      Iterator itemsIter = c.getItems().iterator();
      while (itemsIter.hasNext()) {
        ItemIF anItem = (ItemIF) itemsIter.next();
        if (anItem.getUnRead())
            assertTrue("Item marked as Unread isn't supposed to be Unread",
                anItem.getDescription().compareTo("UnreadItem") == 0);
        if (!anItem.getUnRead())
            assertTrue("Item marked as Read isn't supposed to be Read", anItem
                .getDescription().compareTo("ReadItem") == 0);
      }
    } catch (HibernateException he) {
      logger.warn("Error while querying for channel");
      throw he;
    }
  }

}
