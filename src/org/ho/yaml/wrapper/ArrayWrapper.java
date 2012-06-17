/*
 * Copyright (c) 2007, Yu Cheung Ho
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this list of 
 *        conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice, this list 
 *        of conditions and the following disclaimer in the documentation and/or other materials 
 *        provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF 
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ho.yaml.wrapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.ho.yaml.ReflectionUtil;
import org.ho.yaml.exception.YamlException;

@SuppressWarnings( "all" )
public class ArrayWrapper extends AbstractWrapper implements
		CollectionWrapper {

	ArrayList list = new ArrayList();
	
	public ArrayWrapper(Class type){
		super(type);
		assert type.isArray();
	}
	
    
	@Override
    public Object createPrototype() {
	    throw new UnsupportedOperationException("createPrototype not supported.");
    }


    public void add(Object object) {
		list.add(object);
	}

	public void add(int index, Object object) {
	    list.add(index, object);
    }

    public boolean isTyped() {
		return true;
	}

	public Class componentType() {
		return type.getComponentType();
	}

	@Override
	protected Object createObject() {
		String componentTypeName = ReflectionUtil.arrayComponentName(ReflectionUtil.className(getType()));
		Class componentType = ReflectionUtil.classForName(componentTypeName);
		if (componentType == null)
			throw new YamlException("class " + componentTypeName + " cannot be resolved.");
		Object array = Array.newInstance(componentType, list.size());
		for (int i = 0; i < Array.getLength(array); i++)
            try {
                Array.set(array, i, list.get(i));
            } catch (IllegalArgumentException e) {
                throw new YamlException("Fail to set " + list.get(i) + " into array of type " + componentTypeName);
            }
		return array;
	}

    @Override
    protected void fireCreated() {
        list = toList(object);
        super.fireCreated();
    }

    ArrayList toList(Object array){
        ArrayList ret = new ArrayList(Array.getLength(array));
        for (int i = 0; i < Array.getLength(array); i++)
            ret.add(Array.get(array, i));
        return ret;
    }

    public int size() {
        return list.size();
    }

    public boolean isOrdered() {
        return true;
    }

    public Iterator iterator() {
        return list.iterator();
    }

}
