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
package com.ivata.groupware.business.search;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.business.search.index.SearchIndexDO;
import com.ivata.groupware.business.search.item.SearchItemDO;
import com.ivata.groupware.business.search.item.content.SearchItemContentDO;
import com.ivata.groupware.business.search.result.SearchResult;
import com.ivata.groupware.container.persistence.QueryPersistenceManager;
import com.ivata.groupware.web.format.SanitizerFormat;
import com.ivata.mask.persistence.FinderException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.format.FormatConstants;
import com.ivata.mask.web.format.HTMLFormatter;



/**
 * <p>Performs indexing of documents and search</p>
 *
 * @since 2002-09-17
 * @author Peter Illes
 * @version $Revision: 1.4 $
 */
public class SearchEngineImpl implements SearchEngine {
    /**
     * <p>
     * <strong>Log4J</strong> logger.
     * </p>
     */
    private static Logger log = Logger.getLogger(SearchEngineImpl.class);


    /**
     * <p>the separator characters used in <code>StringTokenizer</code>s.</p>
     */
    private final static String SEPARATORS = " '`%*+={}[])&#-_,;/<>|:.!?\t\n\r\f";

    /**
     * This datasource will be used to execute queries to search for library
     * items.
     */
    private QueryPersistenceManager persistenceManager;

    SanitizerFormat sanitizer = new SanitizerFormat();

    /**
     * Construct a search engine implementation.
     *
     * @param library used to retrieve items and topics.
     */
    public SearchEngineImpl(QueryPersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    /**
     * <p>checks whether a word is a frequently used english word, not worth to
     * index and therefore not worth to search for it too
     * @param connection the db connection passed
     * @param word the word to check
     * @return <code>true</code> when the word appears in the list of
     * frequently used english words (stopwords)
     */
    private boolean isStopWord(final Connection connection,
            final String word)
            throws SystemException {
        // quotes and slashes not allowed in stop word!
        if ((word.indexOf('\'') != -1)
                || (word.indexOf('\\') != -1)) {
            return false;
        }
        try {
            Statement statementReturn = connection.createStatement();

            ResultSet results = statementReturn.executeQuery("SELECT word FROM search_stopword WHERE word='" + word + "'");
            if (results.next()) {
                statementReturn.close();
                return true;
            } else {
                statementReturn.close();
                return false;
            }
        } catch (SQLException e) {
            throw new SystemException(e);
        }
    }

    /**
     * <p>removes an item or part of an item from index</p>
     * @param type one of {@link: SearchConstant SearchContant}s values
     * @param id the id of the document, when null, all entries for {@see:item
     * item } will be removed
     * @param item the item id of the library item which this document relates to
     *
     * @ejb.interface-method
     *      view-type = "local"
     */
    public void removeFromIndex(final SecuritySession securitySession,
            final String type,
            final String id,
            final String category)
            throws SystemException {
        PersistenceSession persistenceSession
                = persistenceManager.openSession(securitySession);
        SearchItemDO item;
        try {
            item = (SearchItemDO) persistenceManager.findInstance(persistenceSession,
                    "searchItemByTargetIdTypeCategory",
                    new Object[]{id, category, type});
            // clear out all previous index entries for this item
            persistenceManager.removeAll(persistenceSession,
                    "searchIndexByItemId",
                    new Object [] {item.getId()});
            persistenceManager.remove(persistenceSession, item);
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }

    /**
     * <p>searches the index for the given query, returns appropriate DOs as a
     * <code>Vector</code>, most relevant documents first</p>
     * @param query the query of one or more space-separated words
     * @param topic the id of the topic of the wanted documents, <code>null</code>
     * when the all topics should be searched
     * @param filter <code>Collection</code> of {@link: SearchConstant SearchContant}s,
     * describing what parts of the system to search, when <code>null</code>,
     * search is performed on all kinds of documents
     * @return <code>Vector</code> of appropriate DOs,
     * most relevant documents first
     */
    public SortedSet search(final SecuritySession securitySession,
            final String query)
            throws SystemException {
        if (query == null || query.equals("")) {
            return null;
        }

        SortedSet results = Collections.synchronizedSortedSet(new TreeSet());
        PersistenceSession persistenceSession = persistenceManager.openSession(securitySession);
        try {
            Map resultsMap = new HashMap();
            // process words in the query one by one
            for(StringTokenizer tokenizer = new StringTokenizer(query, SEPARATORS);tokenizer.hasMoreTokens();) {

                String currentWord = tokenizer.nextToken();
                currentWord = currentWord.toLowerCase();

                // if the current word from the query is a stopword, skip it
                Connection connection = persistenceSession.getConnection();
                if (isStopWord(connection, currentWord)) {
                    continue;
                }

                currentWord = PorterStemmer.stem(currentWord);

                // if the stemmer returned "", skip the current word of the query
                if (currentWord=="") {
                    continue;
                }

                List indexes = persistenceManager.find(persistenceSession,
                        "searchIndexByWord",
                        new Object[] {currentWord});
                Iterator iterator = indexes.iterator();
                while (iterator.hasNext()) {
                    SearchIndexDO index = (SearchIndexDO) iterator.next();
                    SearchItemDO item = index.getContent().getItem();
                    Integer id = item.getId();

                    // if there is none already, this is a new result
                    SearchResult result = (SearchResult) resultsMap.get(id);
                    if (result == null) {
                        result = new SearchResult();
                        result.setItem(item);
                        result.setWeight(index.getWeight());
                        results.add(result);
                        resultsMap.put(id, result);
                    } else {
                        // increase the weight of this result
                        result.setWeight(result.getWeight() + index.getWeight());
                    }
                }
            }

        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
        return results;
    }

    /**
     * <p>stems the text content and indexes the document with the provided type
     * {@see SearchConstants} and id</p>
     * @param id the id of the document - subpart of a library item,
     * used as reference when searching
     * @param type in <strong>ivata groupware</strong>, always indicates the class of
     *     the object to be stored.
     * @param textParam the content (HTML is converted to plain text) of the
     * document
     * @param format format of the text, see
     * {@link com.ivata.mask.web.format.FormatConstants}
     * @ejb.interface-method
     *      view-type = "local"
     */
    public void updateIndex(SecuritySession securitySession,
            final Integer id,
            final String type,
            final String category,
            final Integer contentId,
            final String contentType,
            final String textParam,
            final int format)
            throws SystemException {
        String text = textParam;
        // this Hashtable will contain the stem-occurences pairs
        Hashtable stems = new Hashtable();

        // if the text is HTML, we need to extract plain text from it
        if (format == FormatConstants.FORMAT_HTML) {
            if (log.isDebugEnabled()) {
                log.debug("Converting HTML into plain text.");
            }
            HTMLFormatter formatter = new HTMLFormatter();

            sanitizer.setTextOnly(true);
            formatter.add(sanitizer);
            String oldText = text;
            text = formatter.format(text);
            if (log.isDebugEnabled()) {
                log.debug("Converted "
                        + oldText.getBytes().length
                        + " bytes HTML into "
                        + text.getBytes().length
                        + " bytes plain text.");
            }
            oldText = null;
        }

        // split up the text to fragments (hopefully words)
        StringTokenizer tokenizer = new StringTokenizer(text, SEPARATORS);

        String newWord;
        int counter = 0;

        PersistenceSession persistenceSession = persistenceManager
                .openSession(securitySession);
        try {
            // looping through all tokens (words)...
            if (log.isDebugEnabled()) {
                log.debug("Locating stemmed words in text ("
                        + text.getBytes().length
                        + " bytes).");
            }
            while (tokenizer.hasMoreTokens()) {
                counter++;

                newWord = tokenizer.nextToken();

                newWord = newWord.toLowerCase();

                // checking whether it's a stopword, when not, stem it
                if (!isStopWord(persistenceSession.getConnection(), newWord)) {
                    newWord = PorterStemmer.stem(newWord);
                    // if there's an output from the stemmer, add it to the collection
                    // or increment the occurences if there was such word in the text
                    if (!(newWord.equals(""))) {
                        // if the word is already here, increment the occurences
                        if (stems.containsKey(newWord)) {
                            Integer occurences = (Integer) stems.get(newWord);
                            stems.put(newWord, new Integer(occurences.intValue() + 1));
                        // if it's a new word for this text, put it to the Hashtable
                        } else {
                            stems.put(newWord, new Integer(1));
                        }
                    }
                }
            }

            // a small security check :>)
            if (counter == 0) {
                if (log.isDebugEnabled()) {
                    log.debug("Found no valid stems (text empty or only contains stop words.)");
                }
                return;
            }

            // Now we have the stems with their occurences, let's put it to index,
            // each occurence divided by the total word count in the text
            // first try to see if this search item already exists
            SearchItemDO item = null;
            SearchItemContentDO content;
            try {
                item = (SearchItemDO) persistenceManager.findInstance(persistenceSession,
                        "searchItemByTargetIdTypeCategory",
                        new Object[]{id, category, type});
                content = (SearchItemContentDO) persistenceManager.findInstance(persistenceSession,
                        "searchItemContentByTargetIdType",
                        new Object[]{contentId, contentType});
                if (log.isDebugEnabled()) {
                    log.debug("Removing previous index contents for this search item content (target id "
                            + contentId
                            + ").");
                }
                // clear out all previous index entries for this item content
                persistenceManager.removeAll(persistenceSession,
                        "searchIndexByContentId",
                        new Object [] {content.getId()});
            } catch (FinderException finderException) {
                // this could be finder exception on either the item or the
                // content
                if (item == null) {
                    item = new SearchItemDO();
                    item.setCategory(category);
                    item.setTargetId(id);
                    item.setType(type);
                    if (log.isDebugEnabled()) {
                        log.debug("Creating new search item (target id "
                                + id
                                + ").");
                    }
                    persistenceManager.add(persistenceSession, item);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Creating new search item content (target id "
                            + contentId
                            + ").");
                }
                content = new SearchItemContentDO();
                content.setItem(item);
                content.setTargetId(contentId);
                content.setType(contentType);
                persistenceManager.add(persistenceSession, content);
            }



            Set keySet = stems.keySet();
            if (log.isDebugEnabled()) {
                log.debug("Adding index for "
                        + keySet.size()
                        + " stemmed words.");
            }
            Iterator iterator = keySet.iterator();
            while(iterator.hasNext()) {
                SearchIndexDO index = new SearchIndexDO();
                index.setContent(content);

                // the current stem, it's also the key in stems Hashlist
                String currentStem = (String) iterator.next();

                // the stemmed word
                index.setWord(currentStem);


                // the stemmed word is the key to find its occurences, then divided
                // by the total count of words in the doc
                index.setWeight((((float)((Integer) stems.get(currentStem)).intValue()))/counter);

                if (log.isDebugEnabled()) {
                    log.debug(currentStem
                            + " --> "
                            + index.getWeight());
                }

                persistenceManager.add(persistenceSession, index);
            }
        } catch (Exception e) {
            persistenceSession.cancel();
            throw new SystemException(e);
        } finally {
            persistenceSession.close();
        }
    }
}
