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
package org.archive.httpclient;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;
import org.archive.util.HttpRecorder;


/**
 * Override of GetMethod that marks the passed HttpRecorder w/ the transition
 * from HTTP head to body and that forces a close on the http connection.
 *
 * The actions done in this subclass used to be done by copying
 * org.apache.commons.HttpMethodBase, overlaying our version in place of the
 * one that came w/ httpclient.  Here is the patch of the difference between
 * shipped httpclient code and our mods:
 * <pre>
 *    -- -1338,6 +1346,12 --
 *
 *        public void releaseConnection() {
 *
 *   +        // HERITRIX always ants the streams closed.
 *   +        if (responseConnection != null)
 *   +        {
 *   +            responseConnection.close();
 *   +        }
 *   +
 *            if (responseStream != null) {
 *                try {
 *                    // FYI - this may indirectly invoke responseBodyConsumed.
 *   -- -1959,6 +1973,11 --
 *                        this.statusLine = null;
 *                    }
 *                }
 *   +            // HERITRIX mark transition from header to content.
 *   +            if (this.httpRecorder != null)
 *   +            {
 *   +                this.httpRecorder.markContentBegin();
 *   +            }
 *                readResponseBody(state, conn);
 *                processResponseBody(state, conn);
 *            } catch (IOException e) {
 * </pre>
 * 
 * <p>We're not supposed to have access to the underlying connection object;
 * am only violating contract because see cases where httpclient is skipping
 * out w/o cleaning up after itself.
 *
 * @author stack
 * @version $Revision: 4646 $, $Date: 2006-09-22 17:23:04 +0000 (Fri, 22 Sep 2006) $
 */
public class HttpRecorderGetMethod extends GetMethod {
    
    protected static Logger logger =
        Logger.getLogger(HttpRecorderGetMethod.class.getName());
    
    /**
     * Instance of http recorder method.
     */
    protected HttpRecorderMethod httpRecorderMethod = null;
    

	public HttpRecorderGetMethod(String uri, HttpRecorder recorder) {
		super(uri);
        this.httpRecorderMethod = new HttpRecorderMethod(recorder);
	}

	protected void readResponseBody(HttpState state, HttpConnection connection)
	throws IOException, HttpException {
        // We're about to read the body.  Mark transition in http recorder.
		this.httpRecorderMethod.markContentBegin(connection);
		super.readResponseBody(state, connection);
	}

    protected boolean shouldCloseConnection(HttpConnection conn) {
        // Always close connection after each request. As best I can tell, this
        // is superfluous -- we've set our client to be HTTP/1.0.  Doing this
        // out of paranoia.
        return true;
    }

    public int execute(HttpState state, HttpConnection conn)
    throws HttpException, IOException {
        // Save off the connection so we can close it on our way out in case
        // httpclient fails to (We're not supposed to have access to the
        // underlying connection object; am only violating contract because
        // see cases where httpclient is skipping out w/o cleaning up
        // after itself).
        this.httpRecorderMethod.setConnection(conn);
        return super.execute(state, conn);
    }
    
    protected void addProxyConnectionHeader(HttpState state, HttpConnection conn)
            throws IOException, HttpException {
        super.addProxyConnectionHeader(state, conn);
        this.httpRecorderMethod.handleAddProxyConnectionHeader(this);
    }
}
