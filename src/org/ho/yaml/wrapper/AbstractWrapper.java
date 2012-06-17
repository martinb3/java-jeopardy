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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.ho.yaml.YamlConfig;
import org.ho.yaml.exception.ObjectCreationException;

@SuppressWarnings( "all" )
public abstract class AbstractWrapper implements ObjectWrapper {

	protected Class type;
	protected Object object;
	protected List<CreateListener> listeners = new ArrayList<CreateListener>();
    protected boolean objectInitialized = false;
	protected YamlConfig config;
        
	protected AbstractWrapper(Class type){
		this.type = type;
	}
    
    protected void fireCreated(){
        for (CreateListener listener: listeners)
            listener.created(object);
    }
	
	protected Object createObject(){
	    try{
	        if (config.isConstructorAccessibleForDecoding(type)){
	            Constructor constr = type.getDeclaredConstructor(null);
	            constr.setAccessible(true);
	            return constr.newInstance();
	        }else
                throw new ObjectCreationException("Default constructor for " + type + " is not accessible.");
	    }catch(Exception e)
		{
			throw new ObjectCreationException("Can't create object of type " + type + " using default constructor.", e);
		}
	}

	public void addCreateHandler(CreateListener listener) {
		if (object == null)
			listeners.add(listener);
		else
			listener.created(object);
	}

	public Class getType() {
		return type;
	}

    public void setObject(Object obj) {
        if (obj == null){
            object = null;
            objectInitialized = true;
        }else{
            object = obj;
            objectInitialized = true;
            fireCreated();
        }
        
    }

    public Object getObject() {
        if (!objectInitialized){
            setObject(createObject());
            return object;
        }else
            return object;
    }

    public Object createPrototype() {
        return createObject();
    }

    @Override
    public String toString() {
        return object == null? "[" + getType() + "]" : "[" + object + "]";
    }

    public ObjectWrapper makeWrapper() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setYamlConfig(YamlConfig config){
        this.config = config;
    }
    
}
