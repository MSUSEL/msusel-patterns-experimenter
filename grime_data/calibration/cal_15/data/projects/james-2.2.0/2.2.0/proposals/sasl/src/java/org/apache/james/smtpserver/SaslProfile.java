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
package org.apache.james.smtpserver;

import cryptix.jce.provider.CryptixCrypto;
import cryptix.sasl.Base64;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.security.sasl.*;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.log4j.BasicConfigurator;

class SaslProfile extends AbstractLogEnabled {

    private SaslServer server = null;
    private DataInputStream in    = null;
    private PrintWriter out  = null;

    static {
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        // Make use of Cryptix JCE and SASL libraries
        java.security.Security.addProvider(new CryptixCrypto());
        Sasl.setSaslClientFactory(new cryptix.sasl.ClientFactory());
    }

    SaslProfile(SaslServer _server, DataInputStream _in, PrintWriter _out) {
        this.server = _server;
        this.in     = _in;
        this.out    = _out;
    }
    
    boolean doAUTH(String initialResponse) {

	// It receives a request from the client requesting authentication for
	// a particular SASL mechanism, accompanied by an optional initial
  	// response.
    
        try
        {
	    // It processes the initial response and generates a challenge
	    // specific for the SASL mechanism to be sent back to the client if
	    // the response is processed successfully. If the response is not
	    // processed successfully, it sends an error to the client and
	    // terminates the authentication session.
      
            byte[] challenge = null;
            byte[] response  = null;

            challenge =
                server.evaluateResponse(Base64.decode(initialResponse));
            System.err.println("1");
            if (challenge != null) {
                System.err.println("334 "+Base64.encode(challenge));
                out.println("334 "+Base64.encode(challenge));
            }
            else {
                if (server.isComplete()) {
                    return true;
                } else {
                    System.err.println("334 ");
                    out.println("334 ");
                }
            }

	    // Responses/challenges are exchanged with the client. If the
	    // server cannot successful process a response, the server sends an
	    // error to the client and terminates the authentication. If the
	    // server has completed the authentication and has no more
	    // challenges to send, it sends a success indication to the client.

            System.err.println("2");
      
            do {
                try {
                    System.err.println("3");
                    String input = in.readLine().trim();
                    System.err.println("input: '"+input+"'");
        
                    if (server.isComplete()) return true;

                    challenge = server.evaluateResponse(Base64.decode(input));
                    if (challenge != null) {
                        System.err.println("334 "+Base64.encode(challenge));
                        out.println("334 "+Base64.encode(challenge));
                    }
                    else {
                       if (server.isComplete()) {
                           return true;
                       } else {
                           System.err.println("334 ");
                           out.println("334 ");
                       }
                    }
                }
                catch (IOException e) {
                    System.err.println("IOException: "+e.toString());
                    return false;
                }
             } while (!server.isComplete());
      
            return true;

	    // If the authentication has completed successfully, the server
	    // extracts the authorization ID of the client from the SaslServer
	    // instance (if appropriate) to be used for subsequent access
	    // control checks.
      
	    // For the rest of the session, messages to and from the client are
	    // encoded and decoded using the input and output streams that
	    // encapsulate the negotiated security layer (if any).
    
        }
        catch (SaslException e) {
            System.err.println("SaslException: "+e.toString());
            return false;
        } 
    }

}

