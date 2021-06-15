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
package org.apache.james.userrepository;

import org.apache.james.security.DigestUtil;
import org.apache.james.services.User;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of User Interface. Instances of this class do not allow
 * the the user name to be reset.
 *
 *
 * @version CVS $Revision: 1.6.4.3 $
 */

public class DefaultUser implements User, Serializable {

    private String userName;
    private String hashedPassword;
    private String algorithm ;

    /**
     * Standard constructor.
     *
     * @param name the String name of this user
     * @param hashAlg the algorithm used to generate the hash of the password
     */
    public DefaultUser(String name, String hashAlg) {
        userName = name;
        algorithm = hashAlg;
    }

    /**
     * Constructor for repositories that are construcing user objects from
     * separate fields, e.g. databases.
     *
     * @param name the String name of this user
     * @param passwordHash the String hash of this users current password
     * @param hashAlg the String algorithm used to generate the hash of the
     * password
     */
    public DefaultUser(String name, String passwordHash, String hashAlg) {
        userName = name;
        hashedPassword = passwordHash;
        algorithm = hashAlg;
    }

    /**
     * Accessor for immutable name
     *
     * @return the String of this users name
     */
    public String getUserName() {
        return userName;
    }

    /**
     *  Method to verify passwords. 
     *
     * @param pass the String that is claimed to be the password for this user
     * @return true if the hash of pass with the current algorithm matches
     * the stored hash.
     */
    public boolean verifyPassword(String pass) {
        try {
            String hashGuess = DigestUtil.digestString(pass, algorithm);
            return hashedPassword.equals(hashGuess);
        } catch (NoSuchAlgorithmException nsae) {
        throw new RuntimeException("Security error: " + nsae);
    }
    }

    /**
     * Sets new password from String. No checks made on guessability of
     * password.
     *
     * @param newPass the String that is the new password.
     * @return true if newPass successfuly hashed
     */
    public boolean setPassword(String newPass) {
        try {
            hashedPassword = DigestUtil.digestString(newPass, algorithm);
            return true;
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("Security error: " + nsae);
        }
    }

    /**
     * Method to access hash of password
     *
     * @return the String of the hashed Password
     */
    protected String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Method to access the hashing algorithm of the password.
     *
     * @return the name of the hashing algorithm used for this user's password
     */
    protected String getHashAlgorithm() {
        return algorithm;
    }


}
