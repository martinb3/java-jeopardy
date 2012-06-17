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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.ho.util.Logger;
import org.ho.yaml.exception.YamlException;

import yaml.parser.SyntaxException;
import yaml.parser.YamlParser;
import yaml.parser.YamlParserEvent;

/**
 * YamlDecoder - The usage of YamlDecoder mirrors that of java.beans.XMLDecoder. You create a decoder, make some 
 * calls to readObject, and then close the decoder. In most cases you may find it is not necessary to access this
 * class directly as {@link Yaml} contain methods for the most common usages. The utility functions that
 * were previously in this class are now in {@link Yaml}.
 *
 */
@SuppressWarnings( "all" )
public class YamlDecoder {

	BufferedReader in;
    YamlParser parser;
    YamlConfig config = YamlConfig.getDefaultConfig();
    
    /**
     * This constructor is for bootstrapping in YamlConfig.
     * @param in the InputStream to read from.
     * @param config the YAML config
     * @throws FileNotFoundException
     */
    YamlDecoder(InputStream in, YamlConfig config){
    	this.config = config;
        try {
            this.in = new BufferedReader(new InputStreamReader(in, config.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new YamlException("Unsupported encoding " + config.encoding);
        }
    }
    
    /**
     * Creates a YamlDecoder that reads from specified stream.
     * @param in InputStream to read from.
     */
	public YamlDecoder(InputStream in){
        try {
            this.in = new BufferedReader(new InputStreamReader(in, config.encoding));
        } catch (UnsupportedEncodingException e) {
            throw new YamlException("Unsupported encoding " + config.encoding);
        }
	}
    
        
    /**
     * Creates a YamlDecoder that reads from specifed reader.
     * @param reader Reader to read from
     */
    public YamlDecoder(Reader reader) {
    	this.in = new BufferedReader(reader);
    }
    
    /**
     * Creates a YamlDecoder that reads from specifed reader and YAML config.
     * @param reader reader to read from
     * @param config YAML config
     */
    public YamlDecoder(Reader reader, YamlConfig config){
        this(reader);
        this.config = config;
    }
    
    /**
     * Reads one object from the Yaml stream.
     * @return the next object in the Yaml stream.
     * @throws EOFException - if the end of the Yaml stream has been reached.
     */
	public Object readObject() throws EOFException{
		try {
			JYamlParserEvent event = new JYamlParserEvent(createLogger(), this);
			if (parser == null){
                parser = new YamlParser(in,event);
                firstDocument(parser, event);
            }else{
                parser.setEvent(event);
                if (!nextDocument(parser, event))
                    throw new EOFException();
            }
			Object ret = event.getBean();
            if (ret == null)
                throw new YamlException("Document is empty.");
            return ret;
		} catch (EOFException e) {
            throw e;
        } catch (YamlException e){
            e.setLineNumber(parser.getLineNumber());
			throw e;
		}
	}
	
	public <T> YamlStream asStreamOfType(Class<T> clazz){
		return new Stream(clazz);
	}
	
	public YamlStream asStream(){
		return new Stream(Object.class);
	}
	
    Logger createLogger(){
        if (isSuppressWarnings())
            return new Logger(Logger.Level.NONE);
        else
            return new Logger();
    }
    
    void firstDocument(YamlParser p, YamlParserEvent event){
        try {
            while (p.comment(-1,false));
            
            if (!p.header())
               p.document_first();
            else
                p.value_na(-1);
        }catch (SyntaxException e)
        {
            event.error(e,e.line);
        }catch (IOException e){
            throw new YamlException(e);
        }
    }
    
    boolean nextDocument(YamlParser p, YamlParserEvent event){
        try {
            return p.document_next();
        }catch (SyntaxException e)
        {
            event.error(e,e.line);
            return false;
        }catch (IOException e){
            throw new YamlException(e);
        }
    }
    
    /**
     * Returns the next object from the Yaml stream given the expected type.
     * @param <T> The type of the object expected.
     * @param clazz the class representing the type of the object expected.
     * @return next object from the Yaml stream given the expected type.
     */
	public <T> T readObjectOfType(Class<T> clazz) throws EOFException{
		try {
			JYamlParserEvent event = new JYamlParserEvent(clazz, createLogger(), this);
            if (parser == null){
                parser = new YamlParser(in,event);
                firstDocument(parser, event);
            }else{
                parser.setEvent(event);
                if (!nextDocument(parser, event))
                    throw new EOFException();
            }
			T ret = (T)event.getBean();
            if (ret == null)
                throw new YamlException("Document is empty.");
            return ret;
        } catch (EOFException e) {
            throw e;
        }
	}
	
    
    /**
     * Closes this decoder instance.
     *
     */
	public void close(){
		try {
			in.close();
		} catch (IOException e) {
		}
	}
	
  
    private class Stream<T> implements YamlStream<T>{
        Class<T> clazz;
        T buffer;
        
        Stream(Class<T> clazz){
            this.clazz = clazz;
            peek();
        }

        /**
         * @param decoder
         * @param clazz
         */
        private void peek() {
            try{
                if (clazz == Object.class)
                    buffer = (T)readObject();
                else
                    buffer = readObjectOfType(clazz);
            }catch(EOFException e){
                close();
                buffer = null;
            }
        }
        
        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return buffer != null;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();
            T ret = buffer;
            peek();
            return ret;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
            
        }
        
        /*
         * implement iterator
         */
        public Iterator<T> iterator() {
            return this;
        }
        
    }

    /**
     * returns whether the suppress warnings option is on
     * @return whether the suppress warnings option is on
     */
	public boolean isSuppressWarnings() {
		return config.isSuppressWarnings();
	}

	/**
	 * sets the suppress warnings option
	 * @param suppressWarnings true for on; false for off.
	 */
	public void setSuppressWarnings(boolean suppressWarnings) {
		config.setSuppressWarnings(suppressWarnings);
	}

	/**
	 * returns the Jyaml configuration for this Decoder
	 * @return a YamlConfig
	 */
	public YamlConfig getConfig() {
		return config;
	}

	/**
	 * set the Jyaml configuration for this decoder
	 * @param config the config object to set
	 */
	public void setConfig(YamlConfig config) {
		this.config = config;
	}
}
