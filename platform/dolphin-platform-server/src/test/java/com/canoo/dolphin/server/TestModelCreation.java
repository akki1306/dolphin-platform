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
package com.canoo.dolphin.server;

import com.canoo.dolphin.BeanManager;
import com.canoo.dolphin.impl.BeanDefinitionException;
import com.canoo.dolphin.impl.ClassRepositoryImpl;
import com.canoo.dolphin.impl.PlatformConstants;
import com.canoo.dolphin.server.util.AbstractDolphinBasedTest;
import com.canoo.dolphin.server.util.ChildModel;
import com.canoo.dolphin.server.util.ComplexDataTypesModel;
import com.canoo.dolphin.server.util.ListReferenceModel;
import com.canoo.dolphin.server.util.PrimitiveDataTypesModel;
import com.canoo.dolphin.server.util.SimpleAnnotatedTestModel;
import com.canoo.dolphin.server.util.SimpleTestModel;
import com.canoo.dolphin.server.util.SingleReferenceModel;
import org.hamcrest.Matchers;
import org.opendolphin.core.Attribute;
import org.opendolphin.core.PresentationModel;
import org.opendolphin.core.Tag;
import org.opendolphin.core.server.ServerAttribute;
import org.opendolphin.core.server.ServerDolphin;
import org.opendolphin.core.server.ServerPresentationModel;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.fail;

public class TestModelCreation extends AbstractDolphinBasedTest {

    @Test
    public void testWithAnnotatedSimpleModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        SimpleAnnotatedTestModel model = manager.create(SimpleAnnotatedTestModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getMyProperty(), notNullValue());
        assertThat(model.getMyProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(SimpleAnnotatedTestModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, containsInAnyOrder(
                allOf(
                        hasProperty("propertyName", is("myProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(SimpleAnnotatedTestModel.class.getName())),
                                hasProperty("baseValue", is(SimpleAnnotatedTestModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("myProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }

    @Test
    public void testWithSimpleModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        SimpleTestModel model = manager.create(SimpleTestModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getTextProperty(), notNullValue());
        assertThat(model.getTextProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(SimpleTestModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, containsInAnyOrder(
                allOf(
                        hasProperty("propertyName", is("text")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(SimpleTestModel.class.getName())),
                                hasProperty("baseValue", is(SimpleTestModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("text")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }

    @Test(expectedExceptions = BeanDefinitionException.class)
    public void testWithWrongModelType() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        String model = manager.create(String.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testWithNull() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);
        String model = manager.create(null);
    }


    @Test
    public void testWithAllPrimitiveDatatypes() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        PrimitiveDataTypesModel model = manager.create(PrimitiveDataTypesModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getTextProperty(), notNullValue());
        assertThat(model.getTextProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(PrimitiveDataTypesModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();
        assertThat(attributes, hasSize(9));

        for(Attribute attribute : attributes) {
            if (PlatformConstants.SOURCE_SYSTEM.equals(attribute.getPropertyName())) {
                assertThat(attribute.getValue(), Matchers.<Object>is(PlatformConstants.SOURCE_SYSTEM_SERVER));
                assertThat(attribute.getBaseValue(), Matchers.<Object>is(PlatformConstants.SOURCE_SYSTEM_SERVER));
            } else {
                assertThat(attribute.getValue(), nullValue());
                assertThat(attribute.getBaseValue(), nullValue());
            }
            assertThat(attribute.getQualifier(), nullValue());
            assertThat(attribute.getTag(), is(Tag.VALUE));
        }

        final List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, hasSize(1));

        final PresentationModel classModel = classModels.get(0);

        final List<Attribute> classAttributes = classModel.getAttributes();
        assertThat(classAttributes, hasSize(10));

        for(Attribute attribute : classAttributes) {
            if (PlatformConstants.JAVA_CLASS.equals(attribute.getPropertyName())) {
                assertThat(attribute.getValue(), Matchers.<Object>is(PrimitiveDataTypesModel.class.getName()));
                assertThat(attribute.getBaseValue(), Matchers.<Object>is(PrimitiveDataTypesModel.class.getName()));
            } else if (PlatformConstants.SOURCE_SYSTEM.equals(attribute.getPropertyName())) {
                assertThat(attribute.getValue(), Matchers.<Object>is(PlatformConstants.SOURCE_SYSTEM_SERVER));
                assertThat(attribute.getBaseValue(), Matchers.<Object>is(PlatformConstants.SOURCE_SYSTEM_SERVER));
            } else {
                switch (attribute.getPropertyName()) {
                    case "byteProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.BYTE.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.BYTE.ordinal()));
                        break;
                    case "shortProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.SHORT.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.SHORT.ordinal()));
                        break;
                    case "integerProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.INT.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.INT.ordinal()));
                        break;
                    case "longProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.LONG.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.LONG.ordinal()));
                        break;
                    case "floatProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.FLOAT.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.FLOAT.ordinal()));
                        break;
                    case "doubleProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.DOUBLE.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.DOUBLE.ordinal()));
                        break;
                    case "booleanProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.BOOLEAN.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.BOOLEAN.ordinal()));
                        break;
                    case "textProperty":
                        assertThat(attribute.getValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.STRING.ordinal()));
                        assertThat(attribute.getBaseValue(), Matchers.<Object>is(ClassRepositoryImpl.FieldType.STRING.ordinal()));
                        break;
                    default:
                        fail("Unknown attribute found: " + attribute);
                        break;
                }
            }
            assertThat(attribute.getQualifier(), nullValue());
            assertThat(attribute.getTag(), is(Tag.VALUE));
        }
    }


    @Test
    public void testWithComplexDataTypesModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        ComplexDataTypesModel model = manager.create(ComplexDataTypesModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getDateProperty(), notNullValue());
        assertThat(model.getDateProperty().get(), nullValue());
        assertThat(model.getCalendarProperty(), notNullValue());
        assertThat(model.getCalendarProperty().get(), nullValue());
        assertThat(model.getEnumProperty(), notNullValue());
        assertThat(model.getEnumProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(ComplexDataTypesModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        PresentationModel dolphinModel = dolphinModels.get(0);

        List<Attribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, containsInAnyOrder(
                allOf(
                        hasProperty("propertyName", is("dateProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is("calendarProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is("enumProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(ComplexDataTypesModel.class.getName())),
                                hasProperty("baseValue", is(ComplexDataTypesModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("dateProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.DATE.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.DATE.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("calendarProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.DATE.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.DATE.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("enumProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.ENUM.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.ENUM.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }


    @Test
    public void testWithSingleReferenceModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        SingleReferenceModel model = manager.create(SingleReferenceModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getReferenceProperty(), notNullValue());
        assertThat(model.getReferenceProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(SingleReferenceModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, containsInAnyOrder(
                allOf(
                        hasProperty("propertyName", is("referenceProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(SingleReferenceModel.class.getName())),
                                hasProperty("baseValue", is(SingleReferenceModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("referenceProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.DOLPHIN_BEAN.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.DOLPHIN_BEAN.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }

    @Test
    public void testWithListReferenceModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        ListReferenceModel model = manager.create(ListReferenceModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getObjectList(), empty());
        assertThat(model.getPrimitiveList(), empty());

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(ListReferenceModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, contains(
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(ListReferenceModel.class.getName())),
                                hasProperty("baseValue", is(ListReferenceModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("objectList")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.DOLPHIN_BEAN.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.DOLPHIN_BEAN.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("primitiveList")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }

    @Test
    public void testWithInheritedModel() {
        final ServerDolphin dolphin = createServerDolphin();
        final BeanManager manager = createBeanManager(dolphin);

        ChildModel model = manager.create(ChildModel.class);

        assertThat(model, notNullValue());
        assertThat(model.getParentProperty(), notNullValue());
        assertThat(model.getParentProperty().get(), nullValue());
        assertThat(model.getChildProperty(), notNullValue());
        assertThat(model.getChildProperty().get(), nullValue());
        assertThat(manager.isManaged(model), is(true));

        List<ServerPresentationModel> dolphinModels = dolphin.findAllPresentationModelsByType(ChildModel.class.getName());
        assertThat(dolphinModels, hasSize(1));

        ServerPresentationModel dolphinModel = dolphinModels.get(0);

        List<ServerAttribute> attributes = dolphinModel.getAttributes();

        assertThat(attributes, containsInAnyOrder(
                allOf(
                        hasProperty("propertyName", is("childProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is("parentProperty")),
                        hasProperty("value", nullValue()),
                        hasProperty("baseValue", nullValue()),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                ),
                allOf(
                        hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                        hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                        hasProperty("qualifier", nullValue()),
                        hasProperty("tag", is(Tag.VALUE))
                )
        ));

        List<ServerPresentationModel> classModels = dolphin.findAllPresentationModelsByType(PlatformConstants.DOLPHIN_BEAN);
        assertThat(classModels, hasSize(1));
        assertThat(classModels, contains(
                hasProperty("attributes", containsInAnyOrder(
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.JAVA_CLASS)),
                                hasProperty("value", is(ChildModel.class.getName())),
                                hasProperty("baseValue", is(ChildModel.class.getName())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("childProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is("parentProperty")),
                                hasProperty("value", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("baseValue", is(ClassRepositoryImpl.FieldType.STRING.ordinal())),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        ),
                        allOf(
                                hasProperty("propertyName", is(PlatformConstants.SOURCE_SYSTEM)),
                                hasProperty("value", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("baseValue", is(PlatformConstants.SOURCE_SYSTEM_SERVER)),
                                hasProperty("qualifier", nullValue()),
                                hasProperty("tag", is(Tag.VALUE))
                        )
                ))
        ));
    }

}
