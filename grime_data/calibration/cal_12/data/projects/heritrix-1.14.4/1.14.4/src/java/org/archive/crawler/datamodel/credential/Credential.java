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
package org.archive.crawler.datamodel.credential;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.framework.CrawlController;
import org.archive.crawler.settings.CrawlerSettings;
import org.archive.crawler.settings.ModuleType;
import org.archive.crawler.settings.SimpleType;
import org.archive.crawler.settings.Type;



/**
 * Credential type.
 *
 * Let this be also a credential in the JAAS sense to in that this is what
 * gets added to a subject on successful authentication since it contains
 * data needed to authenticate (realm, login, password, etc.).
 *
 * <p>Settings system assumes that subclasses implement a constructor that
 * takes a name only.
 *
 * @author stack
 * @version $Revision: 5826 $, $Date: 2008-05-22 01:39:28 +0000 (Thu, 22 May 2008) $
 */
public abstract class Credential extends ModuleType {

    private static final Logger logger =
        Logger.getLogger(Credential.class.getName());

    private static final String ATTR_CREDENTIAL_DOMAIN = "credential-domain";

    /**
     * Constructor.
     *
     * @param name Name of this credential.
     * @param description Descrtiption of this particular credential.
     */
    public Credential(String name, String description) {
        super(name, description);
        Type t = addElementToDefinition(new SimpleType(ATTR_CREDENTIAL_DOMAIN,
                "The root domain this credential goes against:" +
                " E.g. www.archive.org", ""));
            t.setOverrideable(false);
            t.setExpertSetting(true);
    }

    /**
     * @param context Context to use when searching for credential domain.
     * @return The domain/root URI this credential is to go against.
     * @throws AttributeNotFoundException If attribute not found.
     */
    public String getCredentialDomain(CrawlURI context)
    throws AttributeNotFoundException {
        return (String)getAttribute(ATTR_CREDENTIAL_DOMAIN, context);
    }

    /**
     * @param context Context to use when searching for credential domain.
     * @param domain New domain.
     * @throws AttributeNotFoundException
     * @throws InvalidAttributeValueException
     */
    public void setCredentialDomain(CrawlerSettings context, String domain)
    throws InvalidAttributeValueException, AttributeNotFoundException {
        setAttribute(context, new Attribute(ATTR_CREDENTIAL_DOMAIN, domain));
    }

    /**
     * Attach this credentials avatar to the passed <code>curi</code> .
     *
     * Override if credential knows internally what it wants to attach as
     * payload.  Otherwise, if payload is external, use the below
     * {@link #attach(CrawlURI, String)}.
     *
     * @param curi CrawlURI to load with credentials.
     */
    public void attach(CrawlURI curi) {
        attach(curi, null);
    }

    /**
     * Attach this credentials avatar to the passed <code>curi</code> .
     *
     * @param curi CrawlURI to load with credentials.
     * @param payload Payload to carry in avatar.  Usually credentials.
     */
    public void attach(CrawlURI curi, String payload) {
        CredentialAvatar ca = null;
        try {
            ca = (payload == null )?
                new CredentialAvatar(this.getClass(), getKey(curi)):
                new CredentialAvatar(this.getClass(), getKey(curi), payload);
            curi.addCredentialAvatar(ca);
        }
        catch (AttributeNotFoundException e) {
            logger.severe("Failed attach of " + this  + " for " + curi);
        }
    }

    /**
     * Detach this credential from passed curi.
     *
     * @param curi
     * @return True if we detached a Credential reference.
     */
    public boolean detach(CrawlURI curi) {
        boolean result = false;
        if (!curi.hasCredentialAvatars()) {
            logger.severe("This curi " + curi +
                " has no cred when it should");
        } else {
            Set avatars = curi.getCredentialAvatars();
            for (Iterator i = avatars.iterator(); i.hasNext();) {
                CredentialAvatar ca = (CredentialAvatar)i.next();
                try {
                    if (ca.match(getClass(), getKey(curi))) {
                        result = curi.removeCredentialAvatar(ca);
                    }
                }
                catch (AttributeNotFoundException e) {
                    logger.severe("Failed detach of " + ca + " from " + curi);
                }
            }
        }
        return result;
    }

    /**
     * Detach all credentials of this type from passed curi.
     *
     * @param curi
     * @return True if we detached references.
     */
    public boolean detachAll(CrawlURI curi) {
        boolean result = false;
        if (!curi.hasCredentialAvatars()) {
            logger.severe("This curi " + curi +
                " has no creds when it should.");
        } else {
            Set avatars = curi.getCredentialAvatars();
            for (Iterator i = avatars.iterator(); i.hasNext();) {
                CredentialAvatar ca = (CredentialAvatar)i.next();
                if (ca.match(getClass())) {
                    result = curi.removeCredentialAvatar(ca);
                }
            }
        }
        return result;
    }

    /**
     * @param curi CrawlURI to look at.
     * @return True if this credential IS a prerequisite for passed
     * CrawlURI.
     */
    public abstract boolean isPrerequisite(CrawlURI curi);

    /**
     * @param curi CrawlURI to look at.
     * @return True if this credential HAS a prerequisite for passed CrawlURI.
     */
    public abstract boolean hasPrerequisite(CrawlURI curi);

    /**
     * Return the authentication URI, either absolute or relative, that serves
     * as prerequisite the passed <code>curi</code>.
     *
     * @param curi CrawlURI to look at.
     * @return Prerequisite URI for the passed curi.
     */
    public abstract String getPrerequisite(CrawlURI curi);

    /**
     * @param context Context to use when searching for credential domain.
     * @return Key that is unique to this credential type.
     * @throws AttributeNotFoundException
     */
    public abstract String getKey(CrawlURI context)
        throws AttributeNotFoundException;

    /**
     * @return True if this credential is of the type that needs to be offered
     * on each visit to the server (e.g. Rfc2617 is such a type).
     */
    public abstract boolean isEveryTime();

    /**
     * @param curi CrawlURI to as for context.
     * @param http Instance of httpclient.
     * @param method Method to populate.
     * @param payload Avatar payload to use populating the method.
     * @return True if added a credentials.
     */
    public abstract boolean populate(CrawlURI curi, HttpClient http,
        HttpMethod method, String payload);

    /**
     * @param curi CrawlURI to look at.
     * @return True if this credential is to be posted.  Return false if the
     * credential is to be GET'd or if POST'd or GET'd are not pretinent to this
     * credential type.
     */
    public abstract boolean isPost(CrawlURI curi);

    /**
     * Test passed curi matches this credentials rootUri.
     * @param controller
     * @param curi CrawlURI to test.
     * @return True if domain for credential matches that of the passed curi.
     */
    public boolean rootUriMatch(CrawlController controller, 
            CrawlURI curi) {
        String cd = null;
        try {
            cd = getCredentialDomain(curi);
        }
        catch (AttributeNotFoundException e) {
            logger.severe("Failed to get credential domain " + curi + ": " +
                e.getMessage());
        }

        String serverName = controller.getServerCache().getServerFor(curi).
            getName();
        logger.fine("RootURI: Comparing " + serverName + " " + cd);
        return cd != null && serverName != null &&
            serverName.equalsIgnoreCase(cd);
    }
}
