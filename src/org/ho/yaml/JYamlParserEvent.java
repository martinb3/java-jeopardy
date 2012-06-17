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

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.ho.util.Logger;
import org.ho.yaml.exception.YamlException;
import org.ho.yaml.wrapper.ObjectWrapper;

import yaml.parser.YamlParserEvent;

@SuppressWarnings( "all" )
class JYamlParserEvent extends YamlParserEvent {

	Stack<State> stack = new Stack<State>();
	Map<String, ObjectWrapper> aliasMap = new HashMap<String, ObjectWrapper>();
	
	public JYamlParserEvent(Logger logger, YamlDecoder decoder){
		stack.push(new NoneState(aliasMap, stack, decoder, logger));
	}
	
    public JYamlParserEvent(Object object, Logger logger, YamlDecoder decoder){
        // TODO
    }
    
	public JYamlParserEvent(Class clazz, Logger logger, YamlDecoder decoder){
		this(logger, decoder);
		String classname = ReflectionUtil.className(clazz);
        stack.peek().setDeclaredClassname(classname);
//        stack.peek().setWrapper(decoder.getConfig().getWrapper(clazz));
//        if (!clazz.isArray() && !ReflectionUtil.isSimpleType(clazz))
//	        try {
//	            stack.peek().setWrapper(clazz.newInstance());
//	        } catch (Exception e){
//	            throw new YamlException("Can't instantiate object of type " + clazz.getName());
//	        }
        
	}
	
	@Override
	public void content(String a, String b) {
		stack.peek().nextOnContent(a, b);
	}

	@Override
	public void error(Exception e, int line) {
        throw new YamlException(e.getMessage(), line);
	}

	@Override
	public void event(int c) {
		stack.peek().nextOnEvent(c);
	}

	@Override
	public void property(String a, String b) {
		stack.peek().nextOnProperty(a, b);
	}

	public Object getBean(){
		return stack.peek().getWrapper().getObject();
	}
	
}
