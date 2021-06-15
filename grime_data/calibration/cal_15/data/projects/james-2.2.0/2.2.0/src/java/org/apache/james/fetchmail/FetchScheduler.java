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
import java.util.Iterator;

import org.apache.avalon.cornerstone.services.scheduler.PeriodicTimeTrigger;
import org.apache.avalon.cornerstone.services.scheduler.TimeScheduler;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

/**
 *  A class to instantiate and schedule a set of mail fetching tasks
 *
 * $Id: FetchScheduler.java,v 1.8.2.4 2004/02/18 21:47:26 hilmer Exp $
 *
 *  @see org.apache.james.fetchmail.FetchMailOriginal#configure(Configuration) FetchMailOriginal
 *  
 */
public class FetchScheduler
    extends AbstractLogEnabled
    implements Serviceable, Configurable, Initializable, Disposable, FetchSchedulerMBean {

    /**
     * Configuration object for this service
     */
    private Configuration conf;

    /**
     * The service manager that allows access to the system services
     */
    private ServiceManager m_manager;

    /**
     * The scheduler service that is used to trigger fetch tasks.
     */
    private TimeScheduler scheduler;

    /**
     * Whether this service is enabled.
     */
    private volatile boolean enabled = false;

    private ArrayList theFetchTaskNames = new ArrayList();

    /**
     * @see org.apache.avalon.framework.service.Serviceable#service( ServiceManager )
     */
    public void service(ServiceManager comp) throws ServiceException
    {
        m_manager = comp;
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure(Configuration conf) throws ConfigurationException
    {
        this.conf = conf;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception
    {
        enabled = conf.getAttributeAsBoolean("enabled", false);
        if (enabled)
        {
            scheduler = (TimeScheduler) m_manager.lookup(TimeScheduler.ROLE);
            Configuration[] fetchConfs = conf.getChildren("fetch");
            for (int i = 0; i < fetchConfs.length; i++)
            {
                FetchMail fetcher = new FetchMail();
                Configuration fetchConf = fetchConfs[i];
                String fetchTaskName = fetchConf.getAttribute("name");
                fetcher.enableLogging(
                    getLogger().getChildLogger(fetchTaskName));
                fetcher.service(m_manager);
                fetcher.configure(fetchConf);
                Integer interval =
                    new Integer(fetchConf.getChild("interval").getValue());
                PeriodicTimeTrigger fetchTrigger =
                    new PeriodicTimeTrigger(0, interval.intValue());

                scheduler.addTrigger(fetchTaskName, fetchTrigger, fetcher);
                theFetchTaskNames.add(fetchTaskName);
            }

            if (getLogger().isInfoEnabled())
                getLogger().info("FetchMail Started");
            System.out.println("FetchMail Started");
        }
        else
        {
            if (getLogger().isInfoEnabled())
                getLogger().info("FetchMail Disabled");
            System.out.println("FetchMail Disabled");
        }
    }

    /**
     * @see org.apache.avalon.framework.activity.Disposable#dispose()
     */
    public void dispose()
    {
        if (enabled)
        {
            getLogger().info("FetchMail dispose...");
            Iterator nameIterator = theFetchTaskNames.iterator();
            while (nameIterator.hasNext())
            {
                scheduler.removeTrigger((String) nameIterator.next());
            }
            getLogger().info("FetchMail ...dispose end");
        }
    }
    
    /**
     * Describes whether this service is enabled by configuration.
     *
     * @return is the service enabled.
     */
    public final boolean isEnabled() {
        return enabled;
    }
    
}
