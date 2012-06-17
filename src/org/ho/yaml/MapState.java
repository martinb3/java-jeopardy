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

import static yaml.parser.YamlParser.MAP_CLOSE;
import static yaml.parser.YamlParser.MAP_SEPARATOR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.ho.util.Logger;
import org.ho.yaml.wrapper.MapWrapper;
import org.ho.yaml.wrapper.ObjectWrapper;


@SuppressWarnings( "all" )
class MapState extends State {

	String key;

	MapState(Map<String, ObjectWrapper> aliasMap, Stack<State> stack, YamlDecoder decoder, Logger logger) {
		super(aliasMap, stack, decoder, logger);
	}
    
    protected MapWrapper getMap(){
        return (MapWrapper)getWrapper();
    }
    
	@Override
	public void nextOnContent(String type, String content) {
		if (key == null)
			key = content;
		else {
			ObjectWrapper newObject = null;
            if ("alias".equals(type)){
                String alias = content.substring(1);
                if ( aliasMap.containsKey(alias)) {
                    newObject = aliasMap.get(alias);
                    final String currentKey = key;
                    newObject.addCreateHandler(new ObjectWrapper.CreateListener(){
                        public void created(Object obj) {
                            getMap().put(currentKey, obj);
                        }
                    });
                }
            }
            else {
                
                newObject = decoder.getConfig().getWrapperSetContent(expectedType(type), content);
                getMap().put(Utilities.decodeSimpleType(key), newObject.getObject());
			    
                if (getAnchorname() != null)
                    markAnchor(newObject, getAnchorname());
            }
			clear();
			key = null;
		}
	}

	@Override
	public void nextOnEvent(int event) {
		switch (event) {
		case MAP_SEPARATOR:
			break;
		case MAP_CLOSE:
			stack.pop();
			stack.peek().childCallback(wrapper);
			break;
		default:
			super.nextOnEvent(event);
		}

	}

	@Override
	public void childCallback(ObjectWrapper child) {
		getMap().put(Utilities.decodeSimpleType(key), child.getObject());
		clear();
		key = null;
	}
    
	@Override
	protected String expectedType() {
        if (getClassname() != null)
            return getClassname();
        else{
            Class type = getMap().getExpectedType(key);
            if (type == null)
                return null;
            else {
                String ret = ReflectionUtil.className(type);
                if (List.class.getName().equals(ret))
                    return ArrayList.class.getName();
                else if (Map.class.getName().equals(ret))
                    return HashMap.class.getName();
                else
                    return ret;
            }
        }
	}

}
