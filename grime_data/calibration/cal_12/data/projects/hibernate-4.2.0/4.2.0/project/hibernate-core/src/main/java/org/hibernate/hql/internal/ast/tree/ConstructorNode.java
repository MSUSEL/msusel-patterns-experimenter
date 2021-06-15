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
package org.hibernate.hql.internal.ast.tree;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import antlr.SemanticException;
import antlr.collections.AST;

import org.hibernate.PropertyNotFoundException;
import org.hibernate.QueryException;
import org.hibernate.hql.internal.ast.DetailedSemanticException;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;

/**
 * Represents a constructor (new) in a SELECT.
 *
 * @author josh
 */
public class ConstructorNode extends SelectExpressionList implements AggregatedSelectExpression {
	private Class resultType;
	private Constructor constructor;
	private Type[] constructorArgumentTypes;
	private boolean isMap;
	private boolean isList;

	public ResultTransformer getResultTransformer() {
		if ( constructor != null ) {
			return new AliasToBeanConstructorResultTransformer( constructor );
		}
		else if ( isMap ) {
			return Transformers.ALIAS_TO_ENTITY_MAP;
		}
		else if ( isList ) {
			return Transformers.TO_LIST;
		}
		throw new QueryException( "Unable to determine proper dynamic-instantiation tranformer to use." );
	}

	private String[] aggregatedAliases;

	public String[] getAggregatedAliases() {
		if ( aggregatedAliases == null ) {
			aggregatedAliases = buildAggregatedAliases();
		}
		return aggregatedAliases;
	}

	private String[] buildAggregatedAliases() {
		SelectExpression[] selectExpressions = collectSelectExpressions();
		String[] aliases = new String[selectExpressions.length] ;
		for ( int i=0; i<selectExpressions.length; i++ ) {
			String alias = selectExpressions[i].getAlias();
			aliases[i] = alias==null ? Integer.toString(i) : alias;
		}
		return aliases;
	}

	public void setScalarColumn(int i) throws SemanticException {
		SelectExpression[] selectExpressions = collectSelectExpressions();
		// Invoke setScalarColumnText on each constructor argument.
		for ( int j = 0; j < selectExpressions.length; j++ ) {
			SelectExpression selectExpression = selectExpressions[j];
			selectExpression.setScalarColumn( j );
		}
	}

	public int getScalarColumnIndex() {
		return -1;
	}

	public void setScalarColumnText(int i) throws SemanticException {
		SelectExpression[] selectExpressions = collectSelectExpressions();
		// Invoke setScalarColumnText on each constructor argument.
		for ( int j = 0; j < selectExpressions.length; j++ ) {
			SelectExpression selectExpression = selectExpressions[j];
			selectExpression.setScalarColumnText( j );
		}
	}

	@Override
    protected AST getFirstSelectExpression() {
		// Collect the select expressions, skip the first child because it is the class name.
		return getFirstChild().getNextSibling();
	}

	@Override
	public Class getAggregationResultType() {
		return resultType;
	}

	/**
	 * @deprecated (tell clover to ignore this method)
	 */
	@Deprecated
    @Override
    public Type getDataType() {
/*
		// Return the type of the object created by the constructor.
		AST firstChild = getFirstChild();
		String text = firstChild.getText();
		if ( firstChild.getType() == SqlTokenTypes.DOT ) {
			DotNode dot = ( DotNode ) firstChild;
			text = dot.getPath();
		}
		return getSessionFactoryHelper().requireEntityType( text );
*/
		throw new UnsupportedOperationException( "getDataType() is not supported by ConstructorNode!" );
	}

	public void prepare() throws SemanticException {
		constructorArgumentTypes = resolveConstructorArgumentTypes();
		String path = ( ( PathNode ) getFirstChild() ).getPath();
		if ( "map".equals( path.toLowerCase() ) ) {
			isMap = true;
			resultType = Map.class;
		}
		else if ( "list".equals( path.toLowerCase() ) ) {
			isList = true;
			resultType = List.class;
		}
		else {
			constructor = resolveConstructor( path );
			resultType = constructor.getDeclaringClass();
		}
	}

	private Type[] resolveConstructorArgumentTypes() throws SemanticException {
		SelectExpression[] argumentExpressions = collectSelectExpressions();
		if ( argumentExpressions == null ) {
			// return an empty Type array
			return new Type[]{};
		}

		Type[] types = new Type[argumentExpressions.length];
		for ( int x = 0; x < argumentExpressions.length; x++ ) {
			types[x] = argumentExpressions[x].getDataType();
		}
		return types;
	}

	private Constructor resolveConstructor(String path) throws SemanticException {
		String importedClassName = getSessionFactoryHelper().getImportedClassName( path );
		String className = StringHelper.isEmpty( importedClassName ) ? path : importedClassName;
		if ( className == null ) {
			throw new SemanticException( "Unable to locate class [" + path + "]" );
		}
		try {
			Class holderClass = ReflectHelper.classForName( className );
			return ReflectHelper.getConstructor( holderClass, constructorArgumentTypes );
		}
		catch ( ClassNotFoundException e ) {
			throw new DetailedSemanticException( "Unable to locate class [" + className + "]", e );
		}
		catch ( PropertyNotFoundException e ) {
			// this is the exception returned by ReflectHelper.getConstructor() if it cannot
			// locate an appropriate constructor
			throw new DetailedSemanticException( "Unable to locate appropriate constructor on class [" + className + "]", e );
		}
	}

	public Constructor getConstructor() {
		return constructor;
	}

	public List getConstructorArgumentTypeList() {
		return Arrays.asList( constructorArgumentTypes );
	}

	public List getAggregatedSelectionTypeList() {
		return getConstructorArgumentTypeList();
	}

	public FromElement getFromElement() {
		return null;
	}

	public boolean isConstructor() {
		return true;
	}

	public boolean isReturnableEntity() throws SemanticException {
		return false;
	}

	public boolean isScalar() {
		// Constructors are always considered scalar results.
		return true;
	}

	public void setAlias(String alias) {
		throw new UnsupportedOperationException("constructor may not be aliased");
	}

	public String getAlias() {
		throw new UnsupportedOperationException("constructor may not be aliased");
	}
}
