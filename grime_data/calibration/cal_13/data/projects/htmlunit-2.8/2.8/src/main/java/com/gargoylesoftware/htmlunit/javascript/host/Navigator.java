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

import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a Navigator.
 *
 * @version $Revision: 5642 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535867.aspx">MSDN documentation</a>
 */
public final class Navigator extends SimpleScriptable {

    private static final long serialVersionUID = 6741787912716453833L;

    private PluginArray plugins_;
    private MimeTypeArray mimeTypes_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Navigator() { }

    /**
     * Returns the property "appCodeName".
     * @return the property "appCodeName"
     */
    public String jsxGet_appCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the property "appMinorVersion".
     * @return the property "appMinorVersion"
     */
    public String jsxGet_appMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }

    /**
     * Returns the property "appName".
     * @return the property "appName"
     */
    public String jsxGet_appName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the property "appVersion".
     * @return the property "appVersion"
     */
    public String jsxGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser (for IE).
     * @return the language
     */
    public String jsxGet_browserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser (for Mozilla).
     * @return the language
     */
    public String jsxGet_language() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the property "cookieEnabled".
     * @return the property "cookieEnabled"
     */
    public boolean jsxGet_cookieEnabled() {
        return getWindow().getWebWindow().getWebClient().getCookieManager().isCookiesEnabled();
    }

    /**
     * Returns the property "cpuClass".
     * @return the property "cpuClass"
     */
    public String jsxGet_cpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Returns the property "onLine".
     * @return the property "onLine"
     */
    public boolean jsxGet_onLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the property "platform".
     * @return the property "platform"
     */
    public String jsxGet_platform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the property "product".
     * @return the property "product"
     */
    public String jsxGet_product() {
        return "Gecko";
    }

    /**
     * Returns the build number of the current browser.
     * @see <a href="https://developer.mozilla.org/en/navigator.productSub">Mozilla Doc</a>
     * @return false
     */
    public String jsxGet_productSub() {
        return "20100215";
    }

    /**
     * Returns the property "systemLanguage".
     * @return the property "systemLanguage"
     */
    public String jsxGet_systemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }

    /**
     * Returns the property "userAgent".
     * @return the property "userAgent"
     */
    public String jsxGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the property "userLanguage".
     * @return the property "userLanguage"
     */
    public String jsxGet_userLanguage() {
        return getBrowserVersion().getUserLanguage();
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    public Object jsxGet_plugins() {
        initPlugins();
        return plugins_;
    }

    private void initPlugins() {
        if (plugins_ != null) {
            return;
        }
        plugins_ = new PluginArray();
        plugins_.setParentScope(this);
        plugins_.setPrototype(getPrototype(PluginArray.class));

        mimeTypes_ = new MimeTypeArray();
        mimeTypes_.setParentScope(this);
        mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));

        for (final PluginConfiguration pluginConfig : getBrowserVersion().getPlugins()) {
            final Plugin plugin = new Plugin(pluginConfig.getName(), pluginConfig.getDescription(),
                pluginConfig.getFilename());
            plugin.setParentScope(this);
            plugin.setPrototype(getPrototype(Plugin.class));
            plugins_.add(plugin);

            for (final PluginConfiguration.MimeType mimeTypeConfig : pluginConfig.getMimeTypes()) {
                final MimeType mimeType = new MimeType(mimeTypeConfig.getType(), mimeTypeConfig.getDescription(),
                    mimeTypeConfig.getSuffixes(), plugin);
                mimeType.setParentScope(this);
                mimeType.setPrototype(getPrototype(MimeType.class));
                mimeTypes_.add(mimeType);
                plugin.add(mimeType);
            }
        }
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    public Object jsxGet_mimeTypes() {
        initPlugins();
        return mimeTypes_;
    }

    /**
     * Indicates if Java is enabled.
     * @return true/false (see {@link com.gargoylesoftware.htmlunit.WebClient#isAppletEnabled()}
     */
    public boolean jsxFunction_javaEnabled() {
        return getWindow().getWebWindow().getWebClient().isAppletEnabled();
    }

    /**
     * Returns <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false
     */
    public boolean jsxFunction_taintEnabled() {
        return false;
    }
}
