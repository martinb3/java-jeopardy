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

import static yaml.parser.YamlParser.LIST_CLOSE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.ho.util.Logger;
import org.ho.yaml.wrapper.CollectionWrapper;
import org.ho.yaml.wrapper.ObjectWrapper;


@SuppressWarnings( "all" )
class ListState extends State {

	/**
     * @param aliasMap
     * @param stack
     */
    ListState(Map<String, ObjectWrapper> aliasMap, Stack<State> stack, YamlDecoder decoder, Logger logger) {
        super(aliasMap, stack, decoder, logger);
    }

    CollectionWrapper getCollection(){
    	return (CollectionWrapper)getWrapper();
    }
    
    @Override
	public void nextOnContent(String type, String content) {
        if (content.length() > 0 && "alias".equals(type) && aliasMap.containsKey(content.substring(1))){
            String alias = content.substring(1);
            ObjectWrapper toAdd = aliasMap.get(alias);
            final int position = getCollection().size();
            toAdd.addCreateHandler(new ObjectWrapper.CreateListener(){
                public void created(Object obj) {
                    if (getCollection().isOrdered())
                        getCollection().add(position, obj);
                    else
                        getCollection().add(obj);
                }
            });
        }else{
            ObjectWrapper toAdd = decoder.getConfig().getWrapperSetContent(expectedType(type), content);
            if (getAnchorname() != null)
                markAnchor(toAdd, getAnchorname());
            getCollection().add(toAdd.getObject());
        }
        clear();
	}

	@Override
	public void nextOnEvent(int event) {
		switch (event){
		case LIST_CLOSE:
			stack.pop();
//            handleArray(stack.peek().getClassname());
			stack.peek().childCallback(getWrapper());
			break;
		default:
			super.nextOnEvent(event);
		}
		
	}
    
	protected String expectedType(){
        if (getCollection().isTyped()){
            Class arrayComponentType = getCollection().componentType();
            if (Object.class != arrayComponentType)
                return ReflectionUtil.className(arrayComponentType);
        }
        {
			String ret = super.expectedType();
            if (List.class.getName().equals(ret))
                return ArrayList.class.getName();
            else if (Map.class.getName().equals(ret))
                return HashMap.class.getName();
            else
                return ret;
        }
	}
	
	@Override
	public void childCallback(ObjectWrapper child) {
		getCollection().add(child.getObject());
		clear();
	}
	
	
}
