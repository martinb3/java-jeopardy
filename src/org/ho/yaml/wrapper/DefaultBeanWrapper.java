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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ho.yaml.ReflectionUtil;
import org.ho.yaml.Utilities;
import org.ho.yaml.exception.PropertyAccessException;

@SuppressWarnings( "all" )
public class DefaultBeanWrapper extends AbstractWrapper implements MapWrapper {

	public DefaultBeanWrapper(Class type){
		super(type);
	}
	
	public boolean hasProperty(String name) {
		PropertyDescriptor prop = ReflectionUtil.getPropertyDescriptor(type, name);
        return config.isPropertyAccessibleForEncoding(prop);
	}

	public Object getProperty(String name) throws PropertyAccessException{
        Object obj = getObject();
		return getProperty(obj, name);
	}

    public Object getProperty(Object obj, String name) {
        try {
            PropertyDescriptor prop = ReflectionUtil.getPropertyDescriptor(type, name);
            if (config.isPropertyAccessibleForEncoding(prop)){
				Method rm = prop.getReadMethod();
                rm.setAccessible(true);
                return rm.invoke(obj, null);
            }
		} catch (Exception e){   
        }
		try {
			Field field = type.getDeclaredField(name);
            if (config.isFieldAccessibleForEncoding(field)){
                field.setAccessible(true);
            	return field.get(obj);
            }
		} catch (Exception e){
        }
        throw new PropertyAccessException("Can't get " + name + " property on type " + type + ".");
    }

	public void setProperty(String name, Object value) throws PropertyAccessException{
		try {
			PropertyDescriptor prop = ReflectionUtil.getPropertyDescriptor(type, name);
            if (config.isPropertyAccessibleForEncoding(prop)){
                Method wm = prop.getWriteMethod();
                wm.setAccessible(true);
                wm.invoke(getObject(), new Object[]{value});
                return;
            }
			
		} catch (Exception e){}
		try {
			Field field = type.getDeclaredField(name);
			if (config.isFieldAccessibleForDecoding(field)){
			    field.setAccessible(true);
            	field.set(getObject(), value);
            }
            return;
		} catch (Exception e){
        }
		// ignore this
	}

	public Class getPropertyType(String name) {
		if (ReflectionUtil.hasProperty(type, name))
			return ReflectionUtil.getPropertyDescriptor(type, name).getPropertyType();
		else{
		    try {
                Field field = type.getDeclaredField(name);
                if (ReflectionUtil.isMemberField(field)){
                    field.setAccessible(true);
                    return field.getType();
                }
            } catch (Exception e){}
            return null;
        }
	}

    /* =========== MapWrapper implementation =========================== */
    
    public boolean containsKey(Object key) {
        return hasProperty((String)key);
    }

    public Object get(Object key) {
        return getProperty((String)key);
    }

    public Class getExpectedType(Object key) {
        return getPropertyType((String)key);
    }

    public Collection keys() {
        Object prototype = createPrototype();
        Set<String> set = new HashSet<String>();
        for (PropertyDescriptor prop: ReflectionUtil.getProperties(getType())){
            if (config.isPropertyAccessibleForEncoding(prop))
                try {
                    if (!Utilities.same(getProperty(getObject(), prop.getName()), 
                            getProperty(prototype, prop.getName())))
                    set.add(prop.getName());
                } catch (Exception e){}
        }
        for (Field field: getType().getDeclaredFields())
            if (config.isFieldAccessibleForEncoding(field)){
                field.setAccessible(true);
                try {
                    if (!Utilities.same(field.get(prototype), field.get(getObject())))
                        set.add(field.getName());
                } catch (Exception e){}
            }
        
        // sort the keys alphabetically
        List<String> ret = new ArrayList<String>(set);
        Collections.sort(ret, new Comparator<String>(){
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
            
        });
        return ret;
    }

    public void put(Object key, Object value) {
        setProperty((String)key, value);
    }



}
