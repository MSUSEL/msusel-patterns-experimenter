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
// Copyright (c) 2002 by Niko Schmuck
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
// $Id: SpringPersistenceManager.java,v 1.2 2006/12/04 23:43:28 italobb Exp $
//

package de.nava.informa.utils.manager.hibernate;

import java.net.URL;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import de.nava.informa.core.ChannelGroupIF;
import de.nava.informa.core.ChannelIF;
import de.nava.informa.core.ItemIF;
import de.nava.informa.impl.hibernate.Channel;
import de.nava.informa.impl.hibernate.ChannelGroup;
import de.nava.informa.utils.manager.PersistenceManagerException;
import de.nava.informa.utils.manager.PersistenceManagerIF;

/**
 * Implementation of Informa's PersistenceManagerIF interface with the
 * Hibernate O/R mapper as backend, making use of the DAO class provided by
 * the Spring Framework. The aim is to ease integration of informa into an
 * Spring scenario.
 * 
 * @since Informa 0.7
 * @author Niko Schmuck
 */
public class SpringPersistenceManager extends HibernateDaoSupport implements PersistenceManagerIF {


  public ChannelGroupIF createGroup(String title) throws PersistenceManagerException {
    ChannelGroupIF group = new ChannelGroup(title);
    getHibernateTemplate().save(group);
    return group;
  }

  public void updateGroup(ChannelGroupIF group) throws PersistenceManagerException {
    getHibernateTemplate().saveOrUpdate(group);
  }

  public void deleteGroup(ChannelGroupIF group) throws PersistenceManagerException {
    getHibernateTemplate().delete(group);
  }

  public void mergeGroups(ChannelGroupIF first, ChannelGroupIF second) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public ChannelGroupIF[] getGroups() throws PersistenceManagerException {
    List<?> groups = getHibernateTemplate().find("from ChannelGroup"); //  group order by group.title");
    return groups.toArray(new ChannelGroupIF[groups.size()]);
  }

  public ChannelIF createChannel(String title, URL location) throws PersistenceManagerException {
    ChannelIF channel = new Channel(title, location);
    getHibernateTemplate().save(channel);
    return channel;
  }

  public void updateChannel(ChannelIF channel) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public void addChannelToGroup(ChannelIF channel, ChannelGroupIF group) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public void removeChannelFromGroup(ChannelIF channel, ChannelGroupIF group) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public void deleteChannel(ChannelIF channel) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public ItemIF createItem(ChannelIF channel, String title) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    return null;
  }

  public ItemIF createItem(ChannelIF channel, ItemIF ethalon) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    return null;
  }

  public void updateItem(ItemIF item) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

  public void deleteItem(ItemIF item) throws PersistenceManagerException {
    // TODO Auto-generated method stub
    
  }

}
