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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.ho.yaml.Utilities;
import org.ho.yaml.YamlConfig;
import org.ho.yaml.exception.YamlException;

@SuppressWarnings( "all" )
public class DateWrapper extends DefaultSimpleTypeWrapper implements WrapperFactory {

    public static final String DATEFORMAT_YAML = "yyyy-MM-dd hh:mm:ss";
    public static final String DATEFORMAT_ISO8601 = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";

    public DateWrapper() {
        super(Date.class);
    }
    
    @Override
    public Class expectedArgType() {
        return String.class;
    }

    @Override
    public ObjectWrapper makeWrapper() {
        ObjectWrapper ret = new DateWrapper();
        ret.setYamlConfig(config);
        return ret;
    }

    @Override
    public void setObject(Object obj) {
        if (obj == null)
            super.setObject(null);
        else if (obj instanceof Date){
            super.setObject(obj);
        }else{
            String arg = (String)obj;
            super.setObject(parseDate(arg));
            if (!objectInitialized)
                throw new YamlException("Can't instantiate " + getType() + " with literal " + arg);
        }
    }
    
    Date parseDate(String s)
    {
        DateFormat fmt = config.getDateFormatter();
        if (fmt != null) {
            try {
                return fmt.parse(s);
            } catch (ParseException e) {
                throw new YamlException("Error parsing date: '" + s + "'", e );
            }
        }
        else {
            // Original method
            try {
                return new Date(Long.parseLong(s));
            } catch (NumberFormatException e){}
            return new Date(s);
        }
    }


    @Override
    public Object getOutputValue() {
        return formateDate((Date)getObject());
    }
    
    /**
     * Writes a date into the output, using the preferred format
     *
     * @param date - the date to write
     *
     * @author Steve Leach
     */
    String formateDate(Date date) {
        DateFormat fmt = config.getDateFormatter();
        if (fmt == null) {
            return "" + date.getTime();
        } else {
            return "\"" + fmt.format(date) + "\"" ;
        }
    }

}
