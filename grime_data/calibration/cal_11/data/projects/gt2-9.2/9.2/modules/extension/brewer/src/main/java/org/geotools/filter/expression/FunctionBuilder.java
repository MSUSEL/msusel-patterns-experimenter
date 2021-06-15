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
package org.geotools.filter.expression;

import java.util.ArrayList;
import java.util.List;

import org.geotools.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * 
 *
 * @source $URL$
 */
public class FunctionBuilder implements Builder<Function> {
    class ParamBuilder extends ChildExpressionBuilder<FunctionBuilder>{
        int index;        
        ParamBuilder( int index ){
            super( FunctionBuilder.this, index < args.size()? args.get(index) : null );
            this.index = index;
        }
        public Expression build() {
            return put( index, super.build() );
        }
        public ParamBuilder param(){
            build();
            return new ParamBuilder( index+1 );
        }
    }
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);    
    LiteralBuilder literal = new LiteralBuilder();
    boolean unset = false;
    private String name = null; // ie unset!
    List<Expression> args = new ArrayList<Expression>();
    
    public FunctionBuilder(){
        reset();
    }
    public FunctionBuilder(Function origional) {
        reset( origional );
    }
    
    Expression put(int index, Expression expr) {
        if( index < args.size() ){
            args.set(index, expr);
        }
        else if( index == args.size() ){
            args.add( expr );
        }
        else {
            // fine we will just add to the end?
            while( args.size()<index){
                args.add(null); // placeholders so we can add at the correct index
            }
            args.add( expr ); // add at the correct index
        }
        return expr;
    }
    
    public ParamBuilder param(){
        return param( args.size() );
    }
    public FunctionBuilder literal( Object literal ){
        put( args.size(), ff.literal( literal ));
        return this;
    }
    public FunctionBuilder property( String xpath ){
        put( args.size(), ff.property( xpath ));
        return this;
    }
    public ParamBuilder param(int index){
        return new ParamBuilder( index );
    }
    public FunctionBuilder name( String function ){
        this.name = function;
        return this;
    }
    /** Inline fallback value to use if named function is not implemented */
    public FunctionBuilder fallback( Object obj ){
        literal.value( obj );
        return this;        
    }
    /** Literal fallback value to use if named function is not implemented */
    public LiteralBuilder fallback(){
        return literal;        
    }
    
    public Function build() {
        if( name == null ){
            return null; // unset!
        }
        Expression[] arguments = new Expression[ args.size() ];
        for( int index = 0; index < args.size(); index++){
            arguments[index] = args.get( index );
        }
        return ff.function(name, arguments);
    }

    public FunctionBuilder reset() {
        name = null;
        args.clear();
        literal.unset();
        return this;
    }

    public FunctionBuilder reset(Function original) {
        name = original.getName();
        args.clear();
        args.addAll( original.getParameters() );
        literal.reset( original.getFallbackValue() );        
        return this;
    }

    public FunctionBuilder unset() {
        name = null;
        args.clear();        
        literal.unset();
        return this;
    }

}
