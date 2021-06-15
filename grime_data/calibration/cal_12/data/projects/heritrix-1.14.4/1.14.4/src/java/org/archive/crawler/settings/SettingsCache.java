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
package org.archive.crawler.settings;


/** This class keeps a map of host names to settings objects.
 *
 * It is implemented with soft references which implies that the elements can
 * be garbage collected when there's no strong references to the elements.
 * Even if there's no strong references left elements will not be garbage
 * collected unless the memory is needed.
 *
 * @author John Erik Halse
 *
 */
public class SettingsCache {
    /** Cached CrawlerSettings objects */
    private final SoftSettingsHash settingsCache = new SoftSettingsHash(16);

    /** Maps hostname to effective settings object */
    private final SoftSettingsHash hostToSettings = new SoftSettingsHash(4000);

    private final CrawlerSettings globalSettings;

    /** Creates a new instance of the settings cache
     */
    public SettingsCache(CrawlerSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    /** Get the effective settings for a host.
     *
     * @param host the host to get settings for.
     * @return the settings or null if not in cache.
     */
    public CrawlerSettings getSettings(String host, String refinement) {
        String key = computeKey(host, refinement);
        return (key == "")? this.globalSettings: hostToSettings.get(key);
    }

    /** Get a settings object.
     *
     * @param scope the scope of the settings object to get.
     * @return the settings object or null if not in cache.
     */
    public CrawlerSettings getSettingsObject(String scope, String refinement) {
        String key = computeKey(scope, refinement);
        return (key == "")? this.globalSettings: settingsCache.get(key);
    }

    /** Add a settings object to the cache.
     *
     * @param host the host for which the settings object is valid.
     * @param settings the settings object.
     */
    public synchronized void putSettings(String host, CrawlerSettings settings) {
        String refinement = settings.isRefinement() ? settings.getName() : null;
        String key = computeKey(host, refinement);
        hostToSettings.put(key, settings);
        key = computeKey(settings.getScope(), refinement);
        settingsCache.put(key, settings);
    }

    /** Delete a settings object from the cache.
     *
     * @param settings the settings object to remove.
     */
    public synchronized void deleteSettingsObject(CrawlerSettings settings) {
        String refinement = settings.isRefinement() ? settings.getName() : null;
        settingsCache.remove(computeKey(settings.getScope(), refinement));

        // Find all references to this settings object in the hostToSettings
        // cache and remove them.
        for (SoftSettingsHash.EntryIterator it = hostToSettings.iterator(); it.hasNext();) {
            if (it.nextEntry().getValue().equals(settings)) {
                it.remove();
            }
        }
    }

    /** Make sure that no host strings points to wrong settings.
     *
     * This method clears most of the host to settings mappings. Because of the
     * performance penalty this should only used when really needed.
     */
    public synchronized void refreshHostToSettings() {
        hostToSettings.clear();
        SoftSettingsHash.EntryIterator it = settingsCache.iterator();
        while (it.hasNext()) {
            SoftSettingsHash.SettingsEntry entry = it.nextEntry();
            hostToSettings.put(entry);
        }
    }
    
    /**
     * Clear all cached settings.
     */
    public void clear() {
        hostToSettings.clear();
        settingsCache.clear();
    }

    public CrawlerSettings getGlobalSettings() {
        return globalSettings;
    }

    private String computeKey(String host, String refinement) {
        host = host == null ? "" : host;
        return (refinement == null) || refinement.equals("") ? host : host
                + '#' + refinement;
    }
}
