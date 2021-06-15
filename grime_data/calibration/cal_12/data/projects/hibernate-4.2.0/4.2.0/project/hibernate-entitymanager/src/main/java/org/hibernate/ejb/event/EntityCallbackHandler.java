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
package org.hibernate.ejb.event;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.annotations.common.reflection.XClass;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.service.classloading.spi.ClassLoaderService;

/**
 * Keep track of all lifecycle callbacks and listeners for a given persistence unit
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 */
@SuppressWarnings({"unchecked", "serial"})
public class EntityCallbackHandler implements Serializable {
	private HashMap<Class, Callback[]> preCreates = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> postCreates = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> preRemoves = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> postRemoves = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> preUpdates = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> postUpdates = new HashMap<Class, Callback[]>();
	private HashMap<Class, Callback[]> postLoads = new HashMap<Class, Callback[]>();

	public void add(XClass entity, ReflectionManager reflectionManager) {
		addCallback( entity, preCreates, PrePersist.class, reflectionManager );
		addCallback( entity, postCreates, PostPersist.class, reflectionManager );
		addCallback( entity, preRemoves, PreRemove.class, reflectionManager );
		addCallback( entity, postRemoves, PostRemove.class, reflectionManager );
		addCallback( entity, preUpdates, PreUpdate.class, reflectionManager );
		addCallback( entity, postUpdates, PostUpdate.class, reflectionManager );
		addCallback( entity, postLoads, PostLoad.class, reflectionManager );
	}

	public void add( Class entity,
	                 ClassLoaderService classLoaderService,
	                 EntityBinding binding ) {
        addCallback( entity, preCreates, PrePersist.class, classLoaderService, binding );
        addCallback( entity, postCreates, PostPersist.class, classLoaderService, binding );
        addCallback( entity, preRemoves, PreRemove.class, classLoaderService, binding );
        addCallback( entity, postRemoves, PostRemove.class, classLoaderService, binding );
        addCallback( entity, preUpdates, PreUpdate.class, classLoaderService, binding );
        addCallback( entity, postUpdates, PostUpdate.class, classLoaderService, binding );
        addCallback( entity, postLoads, PostLoad.class, classLoaderService, binding );
	}

	public boolean preCreate(Object bean) {
		return callback( preCreates.get( bean.getClass() ), bean );
	}

	public boolean postCreate(Object bean) {
		return callback( postCreates.get( bean.getClass() ), bean );
	}

	public boolean preRemove(Object bean) {
		return callback( preRemoves.get( bean.getClass() ), bean );
	}

	public boolean postRemove(Object bean) {
		return callback( postRemoves.get( bean.getClass() ), bean );
	}

	public boolean preUpdate(Object bean) {
		return callback( preUpdates.get( bean.getClass() ), bean );
	}

	public boolean postUpdate(Object bean) {
		return callback( postUpdates.get( bean.getClass() ), bean );
	}

	public boolean postLoad(Object bean) {
		return callback( postLoads.get( bean.getClass() ), bean );
	}


	private boolean callback(Callback[] callbacks, Object bean) {
		if ( callbacks != null && callbacks.length != 0 ) {
			for ( Callback callback : callbacks ) {
				callback.invoke( bean );
			}
			return true;
		}
		else {
			return false;
		}
	}

	private void addCallback(
			XClass entity, HashMap<Class, Callback[]> map, Class annotation, ReflectionManager reflectionManager
	) {
		Callback[] callbacks = null;
		callbacks = CallbackResolver.resolveCallback( entity, annotation, reflectionManager );
		map.put( reflectionManager.toClass( entity ), callbacks );
	}

    private void addCallback( Class<?> entity,
                              HashMap<Class, Callback[]> map,
                              Class annotation,
                              ClassLoaderService classLoaderService,
                              EntityBinding binding ) {
        map.put(entity, CallbackResolver.resolveCallbacks(entity, annotation, classLoaderService, binding));
    }
}
