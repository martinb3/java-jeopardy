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

import org.ho.yaml.ReflectionUtil;
import org.ho.yaml.exception.YamlException;

@SuppressWarnings( "all" )
public class OneArgConstructorTypeWrapper extends DefaultSimpleTypeWrapper implements WrapperFactory {

    protected String argType;
    
    public OneArgConstructorTypeWrapper(){
        super(null);
    }
    
    public OneArgConstructorTypeWrapper(Class type) {
        super(type);
    }

    public OneArgConstructorTypeWrapper(Class type, String argType){
        this(type);
        this.argType = argType;
    }
    
    @Override
    public Class expectedArgType() {
        return ReflectionUtil.classForName(argType);
    }

    @Override
    public void setObject(Object obj) {
        if (obj == null)
            super.setObject(null);
        else if (obj.getClass() == getType())
            super.setObject(obj);
        else
            try {
                obj = type.getConstructor(new Class[]{expectedArgType()}).newInstance(new Object[]{obj});
                super.setObject(obj);
            } catch (Exception e){
                throw new YamlException(e);
            }
    }

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType = argType;
    }
    
    public void setType(Class type){
        this.type = type;
    }

    public ObjectWrapper makeWrapper() {
        ObjectWrapper ret = new OneArgConstructorTypeWrapper(getType(), argType);
        ret.setYamlConfig(config);
        return ret;
    }

}
