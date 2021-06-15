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
package org.archive.crawler.datamodel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.archive.crawler.datamodel.credential.Credential;
import org.archive.crawler.settings.CrawlerSettings;
import org.archive.crawler.settings.SettingsFrameworkTestCase;


/**
 * Test add, edit, delete from credential store.
 *
 * @author stack
 * @version $Revision: 4668 $, $Date: 2006-09-26 21:49:01 +0000 (Tue, 26 Sep 2006) $
 */
public class CredentialStoreTest extends SettingsFrameworkTestCase {

    protected static Logger logger =
        Logger.getLogger("org.archive.crawler.datamodel.CredentialTest");

    final public void testCredentials()
        throws InvalidAttributeValueException, IllegalArgumentException,
        InvocationTargetException, AttributeNotFoundException, MBeanException,
        ReflectionException {

        CredentialStore store = (CredentialStore)this.settingsHandler.
            getOrder().getAttribute(CredentialStore.ATTR_NAME);
        writeCrendentials(store, this.getGlobalSettings(), "global");
        writeCrendentials(store, this.getPerDomainSettings(), "domain");
        writeCrendentials(store, this.getPerHostSettings(), "host");
        List types = CredentialStore.getCredentialTypes();
        List globalNames = checkContextNames(store.iterator(
            this.getGlobalSettings()), types.size());
        checkContextNames(store.iterator(this.getPerDomainSettings()),
            types.size() * 2 /*This should be global + domain*/);
        checkContextNames(store.iterator(this.getPerHostSettings()),
            types.size() * 3 /*This should be global + domain + host*/);
        for (Iterator i = globalNames.iterator();
                i.hasNext();) {
            store.remove(this.getGlobalSettings(),(String)i.next());
        }
        // Should be only host and domain objects at deepest scope.
        checkContextNames(store.iterator(this.getPerHostSettings()),
           types.size() * 2);
    }

    private List checkContextNames(Iterator i, int size) {
        List<String> names = new ArrayList<String>(size);
        for (; i.hasNext();) {
            String name = ((Credential)i.next()).getName();
            names.add(name);
        }
        logger.info("Added: " + names.toString());
        assertTrue("Not enough names, size " + size, size == names.size());
        return names;
    }

    private void writeCrendentials(CredentialStore store, CrawlerSettings context,
                String prefix)
        throws InvalidAttributeValueException, AttributeNotFoundException,
        IllegalArgumentException, InvocationTargetException {

        List types = CredentialStore.getCredentialTypes();
        for (Iterator i = types.iterator(); i.hasNext();) {
            Class cl = (Class)i.next();
            Credential c = store.create(context, prefix + "." + cl.getName(),
                cl);
            assertNotNull("Failed create of " + cl, c);
            logger.info("Created " + c.getName());
        }
        List<String> names = new ArrayList<String>(types.size());
        for (Iterator i = store.iterator(null); i.hasNext();) {
            names.add(((Credential)i.next()).getName());
        }
        getSettingsHandler().writeSettingsObject(context);
    }
}
