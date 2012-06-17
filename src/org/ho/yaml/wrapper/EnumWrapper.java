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

import java.lang.reflect.InvocationTargetException;

import org.ho.yaml.exception.YamlException;
import org.ho.yaml.wrapper.ObjectWrapper.CreateListener;

@SuppressWarnings( "all" )
public class EnumWrapper extends AbstractWrapper implements SimpleObjectWrapper {

    public EnumWrapper(Class type) {
        super(type);
    }

    public Class expectedArgType() {
        return String.class;
    }

    public Object getOutputValue() {
        try {
            return getType().getMethod("name", null).invoke(getObject(), null);
        } catch (Exception e){
            throw new Error("Error getting enum value", e);
        }
    }

    @Override
    public void setObject(Object obj) {
        if (obj instanceof String){
            try {
                super.setObject(getType().getMethod("valueOf", new Class[]{String.class}).invoke(getType(), new Object[]{obj}));
            } catch (Exception e){
                throw new YamlException("Problem getting " + obj + " value of enum type " + type, e);
            }
        }else
            super.setObject(obj);
                
    }

    
}
