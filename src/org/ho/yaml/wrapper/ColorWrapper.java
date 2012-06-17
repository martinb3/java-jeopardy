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

import java.awt.Color;

@SuppressWarnings( "all" )
public class ColorWrapper extends DelayedCreationBeanWrapper {

    public ColorWrapper(Class type) {
        super(type);
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{"red", "green", "blue", "alpha"};
    }

    @Override
    protected Object createObject() {
        Color prototype = (Color)createPrototype();
        
        return new Color(
                (values.containsKey("red") ? ((Number)values.get("red")).floatValue() : (float)prototype.getRed()) / 255 , 
                (values.containsKey("green") ? ((Number)values.get("green")).floatValue() : (float)prototype.getGreen()) / 255, 
                (values.containsKey("blue") ? ((Number)values.get("blue")).floatValue() : (float)prototype.getBlue()) / 255, 
                (values.containsKey("alpha") ? ((Number)values.get("alpha")).floatValue(): (float)prototype.getAlpha()) / 255);
    }

    @Override
    public Object createPrototype() {
        return Color.BLACK;
    }
    
    

}
