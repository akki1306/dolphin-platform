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
package com.canoo.dolphin.server.util;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.impl.BeanManagerImpl;
import com.canoo.dolphin.impl.BeanRepositoryImpl;
import com.canoo.dolphin.impl.ClassRepositoryImpl;
import com.canoo.dolphin.impl.Converters;
import com.canoo.dolphin.impl.PresentationModelBuilderFactory;
import com.canoo.dolphin.impl.ReflectionHelper;
import com.canoo.dolphin.impl.collections.ListMapperImpl;
import com.canoo.dolphin.internal.BeanBuilder;
import com.canoo.dolphin.internal.ClassRepository;
import com.canoo.dolphin.internal.EventDispatcher;
import com.canoo.dolphin.internal.collections.ListMapper;
import com.canoo.dolphin.server.impl.ServerBeanBuilderImpl;
import com.canoo.dolphin.server.impl.ServerEventDispatcher;
import com.canoo.dolphin.server.impl.ServerPresentationModelBuilderFactory;
import com.canoo.dolphin.server.impl.gc.GarbageCollector;
import com.canoo.dolphin.server.impl.gc.GarbageCollectionCallback;
import com.canoo.dolphin.server.impl.gc.Instance;
import org.opendolphin.core.comm.DefaultInMemoryConfig;
import org.opendolphin.core.server.ServerDolphin;
import org.opendolphin.core.server.ServerModelStore;

import java.util.ArrayList;
import java.util.Set;

public abstract class AbstractDolphinBasedTest {

    protected ServerDolphin createServerDolphin() {
        DefaultInMemoryConfig config = new DefaultInMemoryConfig();
        config.getServerDolphin().registerDefaultActions();

        ServerModelStore store = config.getServerDolphin().getServerModelStore();
        try {
            ReflectionHelper.setPrivileged(ServerModelStore.class.getDeclaredField("currentResponse"), store, new ArrayList<>());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return config.getServerDolphin();
    }

    protected BeanManager createBeanManager(ServerDolphin dolphin) {
        final EventDispatcher dispatcher = new ServerEventDispatcher(dolphin);
        final BeanRepositoryImpl beanRepository = new BeanRepositoryImpl(dolphin, dispatcher);
        final Converters converters = new Converters(beanRepository);
        final PresentationModelBuilderFactory builderFactory = new ServerPresentationModelBuilderFactory(dolphin);
        final ClassRepository classRepository = new ClassRepositoryImpl(dolphin, converters, builderFactory);
        final ListMapper listMapper = new ListMapperImpl(dolphin, classRepository, beanRepository, builderFactory, dispatcher);
        final GarbageCollector garbageCollector = new GarbageCollector(new GarbageCollectionCallback() {
            @Override
            public void onReject(Set<Instance> instances) {

            }
        });
        final BeanBuilder beanBuilder = new ServerBeanBuilderImpl(classRepository, beanRepository, listMapper, builderFactory, dispatcher, garbageCollector);
        return new BeanManagerImpl(beanRepository, beanBuilder);
    }
}
