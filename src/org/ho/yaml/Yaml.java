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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Iterator;

/**
 * Yaml is the front end to the Jyaml library. It contains the most commonly used and easy to use methods for 
 * accessing Yaml. For most usages, this is the only class one needs to use. All of these methods also exist 
 * in YamlOperations and are documented there. See {@link YamlOperations}.
 *
 */
@SuppressWarnings( "all" )
public class Yaml {
	
    public static YamlConfig config = YamlConfig.getDefaultConfig();
    
    public static Object load(Reader reader){
    	return config.load(reader);
    }
	
	public static Object load(InputStream in){
    	return config.load(in);
    }

	public static Object load(File file) throws FileNotFoundException{
		return config.load(file);
	}

    public static Object load(String yamlText){
        return config.load(yamlText);
    }
    
    public static <T> T loadType(Reader reader, Class<T> clazz) {
        return config.loadType(reader, clazz);
    }
    
    public static <T> T loadType(InputStream in, Class<T> clazz) throws FileNotFoundException{
        return config.loadType(in, clazz);
    }
    
    public static <T> T loadType(File file, Class<T> clazz) throws FileNotFoundException{
    	return config.loadType(file, clazz);
    }
    	 
    public static <T> T loadType(String yamlText, Class<T> clazz){
    	return config.loadType(yamlText, clazz);
    }

    public static YamlStream loadStream(Reader reader){
        return config.loadStream(reader);
    }
    
    public static YamlStream loadStream(InputStream in){
        return config.loadStream(in);
    }

    public static YamlStream loadStream(File file)  throws FileNotFoundException{
    	return config.loadStream(file);
    }
    
    public static YamlStream loadStream(String yamlText){
    	return config.loadStream(yamlText);
    }

    public static <T> YamlStream<T> loadStreamOfType(Reader reader, Class<T> clazz){
    	return config.loadStreamOfType(reader, clazz);
    }

    public static <T> YamlStream<T> loadStreamOfType(InputStream in, Class<T> clazz){
    	return config.loadStreamOfType(in, clazz);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(File file, Class<T> clazz) throws FileNotFoundException{
    	return config.loadStreamOfType(file, clazz);
    }
    
    public static <T> YamlStream<T> loadStreamOfType(String yamlText, Class<T> clazz) throws FileNotFoundException{
    	return config.loadStreamOfType(yamlText, clazz);
    }

    public static void dump(Object obj, File file) throws FileNotFoundException{
    	config.dump(obj, file);
    }

    public static void dump(Object obj, File file, boolean minimalOutput) throws FileNotFoundException{
    	config.dump(obj, file, minimalOutput);
    }
    
    public static void dumpStream(Iterator iterator, File file, boolean minimalOutput) throws FileNotFoundException{
    	config.dumpStream(iterator, file, minimalOutput);
    }
    
    public static void dumpStream(Iterator iterator, File file) throws FileNotFoundException{
    	config.dumpStream(iterator, file);
    }

    public static String dump(Object obj){
    	return config.dump(obj);
    }
    
    public static String dump(Object obj, boolean minimalOutput){
    	return config.dump(obj, minimalOutput);
    }
    	
    public static String dumpStream(Iterator iterator){
    	return config.dumpStream(iterator);
    }
    
    public static String dumpStream(Iterator iterator, boolean minimalOutput){
    	return config.dumpStream(iterator, minimalOutput);
    }
    
    public static void dump(Object obj, OutputStream out) {
        config.dump(obj, out);
    }

    public static void dump(Object obj, OutputStream out, boolean minimalOutput) {
        config.dump(obj, out, minimalOutput);
    }
}
