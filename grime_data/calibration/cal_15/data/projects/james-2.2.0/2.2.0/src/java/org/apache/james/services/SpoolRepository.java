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
package org.apache.james.services;

import org.apache.mailet.Mail;

/**
 * Interface for a Repository for Spooling Mails.
 * A spool repository is a transitory repository which should empty itself 
 * if inbound deliveries stop.
 *
 * @version 1.0.0, 24/04/1999
 */
public interface SpoolRepository 
    extends MailRepository {

    /**
     * Implementations of AcceptFilter can be used to select which mails a SpoolRepository
     * implementation returns from its accept (AcceptFilter) method
     **/
    public static interface AcceptFilter
    {
        /**
         * This method is called by accept(Filter) to determine if the message is
         * ready for delivery.
         *
         * @param key message key
         * @param state the state of the message
         * @param lastUpdated the last time the message was written to the spool
         * @param errorMessage the current errorMessage
         * @return true if the message is ready for delivery
         **/
        boolean accept (String key, String state, long lastUpdated, String errorMessage) ;


        /**
         * This method allows the filter to determine how long the thread should wait for a
         * message to get ready for delivery, when currently there are none.
         * @return the time to wait for a message to get ready for delivery
         **/
        long getWaitTime ();
    }
    
    /**
     * Define a STREAM repository. Streams are stored in the specified
     * destination.
     */
    String SPOOL = "SPOOL";

    /**
     * Returns an arbitrarily selected mail deposited in this Repository.
     * Usage: SpoolManager calls accept() to see if there are any unprocessed 
     * mails in the spool repository.
     *
     * @return the mail
     */
    Mail accept() throws InterruptedException;

    /**
     * Returns an arbitrarily select mail deposited in this Repository that
     * is either ready immediately for delivery, or is younger than it's last_updated plus
     * the number of failed attempts times the delay time.
     * Usage: RemoteDeliverySpool calls accept() with some delay and should block until an
     * unprocessed mail is available.
     *
     * @return the mail
     */
    Mail accept(long delay) throws InterruptedException;

    /**
     * Returns an arbitrarily select mail deposited in this Repository for
     * which the supplied filter's accept method returns true.
     * Usage: RemoteDeliverySpool calls accept(filter) with some a filter which determines
     * based on number of retries if the mail is ready for processing.
     * If no message is ready the method will block until one is, the amount of time to block is
     * determined by calling the filters getWaitTime method.
     *
     * @return the mail
     */
    Mail accept(AcceptFilter filter) throws InterruptedException;

}
