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
package org.geotools.data.aggregate;

import java.util.List;

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.type.Name;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

class AggregateTypeEncoder extends TransformerBase {

    @Override
    public Translator createTranslator(ContentHandler handler) {
        return new ConfigurationTranslator(handler);
    }

    private static class ConfigurationTranslator extends TranslatorSupport {

        public ConfigurationTranslator(ContentHandler contentHandler) {
            super(contentHandler, null, null);
        }

        @Override
        public void encode(Object o) throws IllegalArgumentException {
            List<AggregateTypeConfiguration> configs = (List<AggregateTypeConfiguration>) o;

            start("AggregateTypes", attributes("version", "1.0"));
            for (AggregateTypeConfiguration config : configs) {
                start("AggregateType", attributes("name", config.getName()));
                for (SourceType st : config.getSourceTypes()) {
                    element("Source",
                            null,
                            attributes("store", st.getStoreName().getURI(), "type",
                                    st.getTypeName()));
                }
                end("AggregateType");
            }
            end("AggregateTypes");
        }

        private AttributesImpl attributes(String... kvp) {
            String[] atts = kvp;
            AttributesImpl attributes = new AttributesImpl();
            for (int i = 0; i < atts.length; i += 2) {
                String name = atts[i];
                String value = atts[i + 1];
                attributes.addAttribute("", name, name, "", value);
            }
            return attributes;
        }

    }

}
