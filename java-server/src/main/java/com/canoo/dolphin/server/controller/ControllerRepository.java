/*
 * Copyright 2015-2016 Canoo Engineering AG.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canoo.dolphin.server.controller;

import com.canoo.dolphin.server.DolphinController;
import com.canoo.dolphin.server.impl.ClasspathScanner;
import com.canoo.dolphin.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by hendrikebbers on 16.09.15.
 */
public class ControllerRepository {

    private Map<String, Class> controllersClasses;

    public ControllerRepository() {
        controllersClasses = new HashMap<>();

        Set<Class<?>> foundControllerClasses = ClasspathScanner.getInstance().getTypesAnnotatedWith(DolphinController.class);
        for (Class<?> controllerClass : foundControllerClasses) {
            String name = controllerClass.getName();
            if (controllerClass.getAnnotation(DolphinController.class).value() != null && !controllerClass.getAnnotation(DolphinController.class).value().trim().isEmpty()) {
                name = controllerClass.getAnnotation(DolphinController.class).value();
            }
            controllersClasses.put(name, controllerClass);
        }
    }

    public synchronized Class<?> getControllerClassForName(String name) {
        Assert.requireNonBlank(name, "name");
        Class<?> foundClass = controllersClasses.get(name);
        if(foundClass == null) {
            throw new IllegalArgumentException("Can't find controller with name " + name);
        }
        return foundClass;
    }
}
