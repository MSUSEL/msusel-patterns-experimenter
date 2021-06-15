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
package org.apache.james.fetchpop;
import org.apache.avalon.cornerstone.services.scheduler.PeriodicTimeTrigger;
import org.apache.avalon.cornerstone.services.scheduler.TimeScheduler;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.component.ComponentException;
import org.apache.avalon.framework.component.ComponentManager;
import org.apache.avalon.framework.component.Composable;
import org.apache.avalon.framework.component.DefaultComponentManager;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.james.services.MailServer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *  A class to instantiate and schedule a set of POP mail fetching tasks<br>
 * <br>$Id: FetchScheduler.java,v 1.4.4.4 2004/03/15 03:54:16 noel Exp $
 *  @see org.apache.james.fetchpop.FetchPOP#configure(Configuration) FetchPOP
 *  
 */
public class FetchScheduler
    extends AbstractLogEnabled
    implements Component, Composable, Configurable, Initializable, Disposable, FetchSchedulerMBean {

    /**
     * Configuration object for this service
     */
    private Configuration conf;

    /**
     * The component manager that allows access to the system services
     */
    private ComponentManager compMgr;

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
     * @see org.apache.avalon.framework.component.Composable#compose(ComponentManager)
     */
    public void compose(ComponentManager comp) throws ComponentException {
        compMgr = comp;
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure(Configuration conf) throws ConfigurationException {
        this.conf = conf;
    }

    /**
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        enabled = conf.getAttributeAsBoolean("enabled", false);
        if (enabled) {
            scheduler = (TimeScheduler) compMgr.lookup(TimeScheduler.ROLE);
            Configuration[] fetchConfs = conf.getChildren("fetch");
            for (int i = 0; i < fetchConfs.length; i++) {
                FetchPOP fp = new FetchPOP();
                Configuration fetchConf = fetchConfs[i];
                String fetchTaskName = fetchConf.getAttribute("name");
                fp.enableLogging(getLogger().getChildLogger(fetchTaskName));
                fp.compose(compMgr);
                fp.configure(fetchConf);
                Integer interval = new Integer(fetchConf.getChild("interval").getValue());
                PeriodicTimeTrigger fetchTrigger = new PeriodicTimeTrigger(0, interval.intValue());
                scheduler.addTrigger(fetchTaskName, fetchTrigger, fp);
                theFetchTaskNames.add(fetchTaskName);
            }
            getLogger().info("Fetch POP Started");
            System.out.println("Fetch POP Started ");
        } else {
            getLogger().info("Fetch POP Disabled");
            System.out.println("Fetch POP Disabled");
        }
    }

    /**
     * @see org.apache.avalon.framework.activity.Disposable#dispose()
     */
    public void dispose() {
        if (enabled) {
            getLogger().info( "Fetch POP dispose..." );
            Iterator nameIterator = theFetchTaskNames.iterator();
            while (nameIterator.hasNext()) {
                scheduler.removeTrigger((String)nameIterator.next());
            }

            getLogger().info( "Fetch POP ...dispose end" );
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
