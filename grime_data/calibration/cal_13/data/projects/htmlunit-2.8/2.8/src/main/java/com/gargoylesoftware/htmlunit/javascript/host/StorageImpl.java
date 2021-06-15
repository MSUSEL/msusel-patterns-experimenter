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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Storage.Type;

/**
 * The actual implementation of different types of Storage.
 *
 * @version $Revision: 5751 $
 * @author Ahmed Ashour
 */
final class StorageImpl implements Serializable {

    private static final long serialVersionUID = -3161682908440199425L;

    private static final Log LOG = LogFactory.getLog(Storage.class);

    private static StorageImpl SINGLETON_ = new StorageImpl();

    static {
        load();
    }

    private Map<String, Map<String, String>> globalStorage_
        = new HashMap<String, Map<String, String>>();

    private Map<String, Map<String, String>> localStorage_
        = new HashMap<String, Map<String, String>>();

    private transient Map<String, Map<String, String>> sessionStorage_
        = new HashMap<String, Map<String, String>>();

    private StorageImpl() { }

    static StorageImpl getInstance() {
        return SINGLETON_;
    }

    void set(final Type type, final HtmlPage page, final String key, final String data) {
        set(getStorage(type), getKey(type, page), key, data);
    }

    Map<String, String> getMap(final Type type, final HtmlPage page) {
        final Map<String, Map<String, String>>storage = getStorage(type);
        final String key = getKey(type, page);
        Map<String, String> map = storage.get(key);
        if (map == null) {
            map = new LinkedHashMap<String, String>();
            storage.put(key, map);
        }
        return map;
    }

    void clear(final Type type, final HtmlPage page) {
        getStorage(type).remove(getKey(type, page));
    }

    private String getKey(final Type type, final HtmlPage page) {
        switch (type) {
            case GLOBAL_STORAGE:
                return page.getUrl().getHost();

            case LOCAL_STORAGE:
                final URL url = page.getUrl();
                return url.getProtocol() + "://" + url.getHost() + ':' + url.getProtocol();

            case SESSION_STORAGE:
                final WebWindow topWindow = page.getEnclosingWindow().getTopWindow();
                return Integer.toHexString(topWindow.hashCode());

            default:
                return null;
        }
    }

    Map<String, Map<String, String>> getStorage(final Type type) {
        switch (type) {
            case GLOBAL_STORAGE:
                return globalStorage_;

            case LOCAL_STORAGE:
                return localStorage_;

            case SESSION_STORAGE:
                return sessionStorage_;

            default:
                return null;
        }
    }

    private static void set(final Map<String, Map<String, String>> storage, final String url,
            final String key, final String data) {
        Map<String, String> map = storage.get(url);
        if (map == null) {
            map = new LinkedHashMap<String, String>();
            storage.put(url, map);
        }
        map.put(key, data);
        save();
    }

    String get(final Type type, final HtmlPage page, final String key) {
        return get(getStorage(type), getKey(type, page), key);
    }

    private static String get(final Map<String, Map<String, String>> storage, final String url,
            final String key) {
        final Map<String, String> map = storage.get(url);
        if (map != null) {
            return map.get(key);
        }
        return null;
    }

    private static void save() {
        try {
            final File file = new File(System.getProperty("user.home"), "htmlunit.storage");
            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(SINGLETON_);
            out.close();
        }
        catch (final IOException e) {
            LOG.info("Could not save storage", e);
        }
    }

    private static void load() {
        try {
            final File file = new File(System.getProperty("user.home"), "htmlunit.storage");
            if (file.exists()) {
                final ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                SINGLETON_ = (StorageImpl) in.readObject();
                SINGLETON_.sessionStorage_ = new HashMap<String, Map<String, String>>();
                in.close();
            }
        }
        catch (final Exception e) {
            LOG.info("Could not load storage", e);
        }
    }
}
