/*
 * TemplateParameter.java July 2011
 *
 * Copyright (C) 2009, Niall Gallagher <niallg@users.sf.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package org.simpleframework.xml.core;

import org.simpleframework.xml.stream.Style;

/**
 * The <code>TemplateParameter</code> object is used to provide stock
 * functions that can be used by all implementations. This ensures
 * there is a consistent set of behaviours for each parameter. It 
 * also reduces the number of methods that need to be maintained for
 * each <void>Parameter</code> implementation.
 * 
 * @author Niall Gallagher
 */
abstract class TemplateParameter implements Parameter {
   
   /**
    * Constructor for the <code>TemplateParameter</code> is used
    * to create a template for other parameters. If any of the
    * method implementations are not as required they should be
    * overridden by the subclass.
    */
   protected TemplateParameter() {
      super();
   }

   /**
    * This is used to acquire the path of the element or attribute
    * represented by this parameter. The path is determined by
    * acquiring the XPath expression and appending the name of the
    * label to form a fully qualified styled path.
    * 
    * @param context this is the context used to style the path
    * 
    * @return returns the path that is used for this parameter
    */
   public String getPath(Context context) {
      Expression expression = getExpression();
      String name = getName(context);
      
      return expression.getElement(name);
   }
   
   /**
    * This is used to acquire the name of the parameter that this
    * represents. The name is determined using annotation and 
    * the name attribute of that annotation, if one is provided.
    * 
    * @param context this is the context used to style the name
    * 
    * @return this returns the name of the annotated parameter
    */
   public String getName(Context context) {
      Style style = context.getStyle();
      String name = getName();
      
      return style.getElement(name);
   }
   
   /**
    * This method is used to determine if the parameter represents 
    * an attribute. This is used to style the name so that elements
    * are styled as elements and attributes are styled as required.
    * 
    * @return this is used to determine if this is an attribute
    */
   public boolean isAttribute() {
      return false;
   }
   
   /**
    * This is used to determine if the parameter represents text. 
    * If this represents text it typically does not have a name,
    * instead the empty string represents the name. Also text
    * parameters can not exist with other text parameters.
    * 
    * @return returns true if this parameter represents text
    */
   public boolean isText() {
      return false;
   }
}
