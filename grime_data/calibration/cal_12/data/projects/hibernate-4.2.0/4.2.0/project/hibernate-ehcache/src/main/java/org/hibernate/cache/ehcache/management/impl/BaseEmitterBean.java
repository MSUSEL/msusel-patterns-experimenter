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
package org.hibernate.cache.ehcache.management.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.StandardMBean;

/**
 * @author gkeim
 */
public abstract class BaseEmitterBean extends StandardMBean implements NotificationEmitter {
	/**
	 * emitter
	 */
	protected final Emitter emitter = new Emitter();

	/**
	 * sequenceNumber
	 */
	protected final AtomicLong sequenceNumber = new AtomicLong();


	private final List<NotificationListener> notificationListeners = new CopyOnWriteArrayList<NotificationListener>();

	/**
	 * BaseEmitterBean
	 *
	 * @param <T>
	 * @param mbeanInterface
	 *
	 * @throws javax.management.NotCompliantMBeanException
	 */
	protected <T> BaseEmitterBean(Class<T> mbeanInterface) throws NotCompliantMBeanException {
		super( mbeanInterface );
	}

	/**
	 * sendNotification
	 *
	 * @param eventType
	 */
	public void sendNotification(String eventType) {
		sendNotification( eventType, null, null );
	}

	/**
	 * sendNotification
	 *
	 * @param eventType
	 * @param data
	 */
	public void sendNotification(String eventType, Object data) {
		sendNotification( eventType, data, null );
	}

	/**
	 * sendNotification
	 *
	 * @param eventType
	 * @param data
	 * @param msg
	 */
	public void sendNotification(String eventType, Object data, String msg) {
		Notification notif = new Notification(
				eventType,
				this,
				sequenceNumber.incrementAndGet(),
				System.currentTimeMillis(),
				msg
		);
		if ( data != null ) {
			notif.setUserData( data );
		}
		emitter.sendNotification( notif );
	}

	/**
	 * Dispose of this SampledCacheManager and clean up held resources
	 */
	public final void dispose() {
		doDispose();
		removeAllNotificationListeners();
	}

	/**
	 * Dispose callback of subclasses
	 */
	protected abstract void doDispose();

	/**
	 * @author gkeim
	 */
	private class Emitter extends NotificationBroadcasterSupport {
		/**
		 * @see javax.management.NotificationBroadcasterSupport#getNotificationInfo()
		 */
		@Override
		public MBeanNotificationInfo[] getNotificationInfo() {
			return BaseEmitterBean.this.getNotificationInfo();
		}
	}

	/**
	 * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener,
	 *	  javax.management.NotificationFilter, java.lang.Object)
	 */
	public void addNotificationListener(NotificationListener notif, NotificationFilter filter, Object callBack) {
		emitter.addNotificationListener( notif, filter, callBack );
		notificationListeners.add( notif );
	}

	/**
	 * remove all added notification listeners
	 */
	private void removeAllNotificationListeners() {
		for ( NotificationListener listener : notificationListeners ) {
			try {
				emitter.removeNotificationListener( listener );
			}
			catch ( ListenerNotFoundException e ) {
				// ignore
			}
		}
		notificationListeners.clear();
	}

	/**
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public abstract MBeanNotificationInfo[] getNotificationInfo();


	/**
	 * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
	 */
	public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
		emitter.removeNotificationListener( listener );
		notificationListeners.remove( listener );
	}

	/**
	 * @see javax.management.NotificationEmitter#removeNotificationListener(javax.management.NotificationListener,
	 *	  javax.management.NotificationFilter, java.lang.Object)
	 */
	public void removeNotificationListener(NotificationListener notif, NotificationFilter filter, Object callBack)
			throws ListenerNotFoundException {
		emitter.removeNotificationListener( notif, filter, callBack );
		notificationListeners.remove( notif );
	}
}
