/*
 * Copyright (c) 2005, Yu Cheung Ho
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
package org.ho.yaml;

import static yaml.parser.YamlParser.LIST_OPEN;
import static yaml.parser.YamlParser.MAP_OPEN;

import java.io.File;
import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.ho.util.Logger;
import org.ho.yaml.exception.YamlException;
import org.ho.yaml.wrapper.CollectionWrapper;
import org.ho.yaml.wrapper.MapWrapper;
import org.ho.yaml.wrapper.ObjectWrapper;

@SuppressWarnings( "all" )
abstract class State {

    Logger logger;
    YamlDecoder decoder;
    Map<String, ObjectWrapper> aliasMap;
    Stack<State> stack;
	ObjectWrapper wrapper;
	String declaredClassname;
	String anchorname;
    
    State(Map<String, ObjectWrapper> aliasMap, Stack<State> stack, YamlDecoder decoder, Logger logger){
        this.aliasMap = aliasMap;
        this.stack = stack;
        this.decoder = decoder;
        this.logger = logger;
    }
    
	public void nextOnEvent(int event) {
		switch (event){
		case MAP_OPEN:
			openMap(stack);
			break;
		case LIST_OPEN:
			openList(stack);
			break;
		default:
		}
	}
	
	public void nextOnContent(String type, String content){
	}
	
	public void nextOnProperty(String type, String value){
		if ("transfer".equals(type)){
			if (getDeclaredClassname() == null && value.startsWith("!")){
				setDeclaredClassname(ReflectionUtil.transfer2classname(value.substring(1), decoder.getConfig()));
			}
		}else if("anchor".equals(type)){
            if (value.startsWith("&"))
                setAnchorname(value.substring(1));
        }
	}
	
	public abstract void childCallback(ObjectWrapper child);
	
    void clear(){
        setDeclaredClassname(null);
        setAnchorname(null);
    }
	
    ObjectWrapper createWrapper(String fallback){
        ObjectWrapper ret = decoder.getConfig().getWrapper(expectedType());
        if (ret == null)
            ret = decoder.getConfig().getWrapper(fallback);
        return ret;
    }
	
    protected String expectedType(String type){
        String ret = expectedType();
        if (ret == null && "string".equals(type))
            ret = "java.lang.String";
        return ret;
    }
    
	protected String expectedType(){
		return getClassname();
	}
	
	void openMap(Stack<State> stack){
		ObjectWrapper obj = createWrapper(ReflectionUtil.className(HashMap.class));
//        if (!(obj instanceof MapWrapper))
//            throw new YamlException("TODO"); //TODO
        if (getAnchorname() != null)
            markAnchor(obj, getAnchorname());
		State s = new MapState(aliasMap, stack, decoder, logger);
        if (!(obj instanceof MapWrapper))
            throw new YamlException(obj.getObject() + " is not a Collection and so cannot be mapped from a sequence.");
        s.wrapper = obj;
		stack.push(s);
	}
	
	void openList(Stack<State> stack){
		ObjectWrapper newObject = createWrapper(ReflectionUtil.className(ArrayList.class));
//        if (!(wrapper instanceof CollectionWrapper))
//            throw new YamlException("TODO"); //TODO
        if (getAnchorname() != null)
            markAnchor(newObject, getAnchorname());
		State s = new ListState(aliasMap, stack, decoder, logger);
		if (!(newObject instanceof CollectionWrapper))
			throw new YamlException(newObject.getObject() + " is not a Collection and so cannot be mapped from a sequence.");
		s.wrapper = newObject;
		stack.push(s);
	}
	
    void markAnchor(ObjectWrapper obj, String anchorname){
        if (aliasMap.get(anchorname) == null)
            aliasMap.put(anchorname, obj);
    }


	
	public ObjectWrapper getWrapper() {
		return wrapper;
	}

	public void setWrapper(ObjectWrapper obj) {
		this.wrapper = obj;
	}

	public String getClassname() {
		return declaredClassname;
	}
	
	public String getDeclaredClassname(){
		return declaredClassname;
	}

	public void setDeclaredClassname(String type) {
		this.declaredClassname = type;
	}

    public String getAnchorname() {
        return anchorname;
    }

    public void setAnchorname(String anchorname) {
        this.anchorname = anchorname;
    }

}
