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
package com.finalist.jag.skelet;


import java.util.*;


/**
 * Class SkeletDataObj
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class SkeletDataObj extends ModuleData {

   /** Field config           */
   private JagSkeletConfig config = null;


   /**
    * Constructor SkeletDataObj
    *
    *
    * @param name
    *
    */
   public SkeletDataObj(String name) {
      super(name, new ArrayList());
   }


   /**
    * Method getConfig
    *
    *
    * @return
    *
    */
   public JagSkeletConfig getConfig() {
      return (this.config);
   }


   /**
    * Method setConfig
    *
    *
    * @param config
    *
    */
   public void setConfig(JagSkeletConfig config) {
      this.config = config;
   }


   /**
    * Method processReferences
    *
    *
    * @throws JagSkeletException
    *
    */
   public void processReferences() throws JagSkeletException {

      HashMap refNames = new HashMap();

      createRefMap(this, refNames);
      replaceReferences(this, refNames);
   }


   /**
    * Method createRefMap
    *
    *
    * @param root
    * @param refNames
    *
    * @throws JagSkeletException
    *
    */
   protected void createRefMap(ModuleData root, HashMap refNames)
      throws JagSkeletException {

      Collection modules = (Collection) getValue();
      Iterator iterator = modules.iterator();

      while (iterator.hasNext()) {
         SkeletModule module = (SkeletModule) iterator.next();

         if (!"".equals(module.getRefname()) && refNames.get(module.getRefname()) != null) {
            throw new JagSkeletException(
               "Found duplicate ref-name field : "
               + module.getRefname());
         }

         refNames.put(module.getRefname(), module);
      }
   }


   /**
    * Method replaceReferences
    *
    *
    * @param root
    * @param refNames
    *
    */
   protected void replaceReferences(ModuleData root, HashMap refNames) {

      Collection modules = (Collection) getValue();
      Iterator iterator = modules.iterator();

      while (iterator.hasNext()) {
         SkeletModule module = (SkeletModule) iterator.next();
         Collection refs = module.getRefs();
         Iterator iteratorRefs = refs.iterator();

         while (iteratorRefs.hasNext()) {
            SkeletModule refModule =
               (SkeletModule) refNames.get((String) iteratorRefs.next());

            ((Collection) module.getValue()).add(refModule);
         }
      }
   }
}