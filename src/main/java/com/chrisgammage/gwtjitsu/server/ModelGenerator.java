package com.chrisgammage.gwtjitsu.server;

import com.chrisgammage.ginjitsu.server.GeneratorHelper;
import com.chrisgammage.gwtjitsu.shared.*;
import com.chrisgammage.gwtjitsu.shared.Collection;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 2:14 PM
 */
public class ModelGenerator extends Generator {
  private JClassType mapType;

  private class Property {
    String name;
    String nameCap;
    String typeString;

    JMethod afterSet;
    JMethod beforeGet;
    boolean afterGetFromHook;
    JMethod afterAdd;
    JMethod afterRemove;
    JMethod filter;

    boolean afterPutIntoHook;
    boolean isPrimitive;
    String objectCast;
    Class<? extends GwtEvent> observableEvent;
    boolean isCollection;
    boolean isMap;
    String addMethodName;
    String removeMethodName;
    String getAtMethodName;

    List<JType> typeParameters = new ArrayList<JType>();
    JType type;
    JField field;
    JMethod getter;
    JMethod setter;
    JMethod getFrom;
    JMethod putInto;
    JMethod remover;
    JMethod adder;
    JMethod getAt;
    JMethod count;
  }

  static final private Logger LOG = Logger.getLogger(ModelGenerator.class.getName());

  static {
    LOG.setLevel(Level.WARNING);
  }

  private String getPropertyTypeString(JType type) {
    String ret = type.getQualifiedSourceName();
    if(type instanceof JParameterizedType) {
      ret += "<";
      boolean first = true;
      for(JClassType cType : ((JParameterizedType)type).getTypeArgs()) {
        if(!first) {
          ret += ", ";
        }
        ret += getPropertyTypeString(cType);
        first = false;
      }
      ret += ">";
    }
    return ret;
  }

  private Map<String, Property> getProperties(JClassType classType) {
    Map<String, Property> properties = new HashMap<String, Property>();

    for(JClassType clazz : classType.getFlattenedSupertypeHierarchy()) {
      for(JField field : clazz.getFields()) {
        if(field.isAnnotationPresent(com.chrisgammage.gwtjitsu.shared.Property.class) ||
                field.isAnnotationPresent(Collection.class)) {
          LOG.log(Level.INFO, "property field found");
          Property property = new Property();
          property.name = field.getName();
          property.field = field;
          property.type = field.getType();
          properties.put(property.name, property);
        }
      }
    }
    for(Property property : properties.values()) {
      parsePropertyField(property, classType);
    }
    return properties;
  }

  private Set<JMethod> getAllMethods(JClassType classType) {
    return GeneratorHelper.getAllMethods(classType);
  }

  private void parsePropertyField(Property property, JClassType classType) {
    property.nameCap = property.name.substring(0, 1).toUpperCase() +
            property.name.substring(1, property.name.length());

    property.isCollection = property.field.isAnnotationPresent(Collection.class);

    if(property.isCollection) {
      String singular = property.field.getAnnotation(Collection.class).singular();
      if(singular.length() > 0) {
        singular = singular.substring(0, 1).toUpperCase() + singular.substring(1, singular.length());
        property.addMethodName = "add" + singular;
        property.removeMethodName = "remove" + singular;
        property.getAtMethodName = "get" + singular;
      } else {
        property.addMethodName = "addTo" + property.nameCap;
        property.removeMethodName = "removeFrom" + property.nameCap;
        property.getAtMethodName = "getFrom" + property.nameCap + "At";
      }
    }

    if(property.field.isAnnotationPresent(ObservableEvent.class)) {
      property.observableEvent = property.field.getAnnotation(ObservableEvent.class).value();
    }

    for(JMethod method : getAllMethods(classType)) {
      if(method.isAnnotationPresent(AfterSet.class)) {
        AfterSet afterSet = method.getAnnotation(AfterSet.class);
        if(afterSet.value().equals(property.name)) {
          property.afterSet = method;
        }
      }
      if(method.isAnnotationPresent(AfterAdd.class)) {
        AfterAdd afterAdd = method.getAnnotation(AfterAdd.class);
        if(afterAdd.value().equals(property.name)) {
          property.afterAdd = method;
        }
      }
      if(method.isAnnotationPresent(Filter.class)) {
        Filter filter = method.getAnnotation(Filter.class);
        if(filter.value().equals(property.name)) {
          property.filter = method;
        }
      }
      if(method.isAnnotationPresent(BeforeGet.class)) {
        BeforeGet beforeGet = method.getAnnotation(BeforeGet.class);
        if(beforeGet.value().equals(property.name)) {
          property.beforeGet = method;
        }
      }
      if(method.isAbstract() ||
              method.getEnclosingType().isInterface() != null) {

        if(method.getEnclosingType().getName().equals("ModelBase")) {
          continue;
        }

        if(method.getName().equals("set" + property.nameCap) &&
                method.getParameters().length == 1) {
          property.setter = method;
          if(property.type == null) {
            property.type = method.getParameterTypes()[0];
          } else {
            assert property.type.equals(method.getParameterTypes()[0])
                    : "Differing Setter and Getter Types";
          }
        } else if(property.isCollection && method.getName().equals(property.getAtMethodName)) {
          property.getAt = method;
        } else if(method.getName().equals("getFrom" + property.nameCap)) {
          property.getFrom = method;
        } else if(method.getName().equals("get" + property.nameCap + "Count")) {
          property.count = method;
        } else if(method.getName().equals("get" + property.nameCap) ||
                method.getName().equals("is" + property.nameCap)) {
          property.getter = method;
          if(property.type == null) {
            property.type = method.getReturnType();
          } else {
            assert property.type.equals(method.getReturnType())
                    : "Differing Setter and Getter Types";
          }
        } else if(property.isCollection &&
                method.getName().equals(property.addMethodName)) {
          property.adder = method;
        } else if(property.isCollection &&
                method.getName().equals(property.removeMethodName)) {
          property.remover = method;
        } else if(method.getName().equals("putInto" + property.nameCap)) {
          property.putInto = method;
        }
      }
    }
    if(property.type == null && property.isCollection) {
      JType parameterType = null;
      if(property.adder != null) {
        parameterType = property.adder.getParameterTypes()[0];
      } else if(property.remover != null) {
        parameterType = property.remover.getParameterTypes()[0];
      }
      assert parameterType != null : "cannot detect parameter type for collection";
      try {
        if(property.isMap) {
          property.type = classType.getOracle().parse("java.util.HashMap<"
                  + parameterType.getQualifiedSourceName() + ">");
        } else {
          property.type = classType.getOracle().parse("java.util.ArrayList<"
                  + parameterType.getQualifiedSourceName() + ">");
        }
      } catch(TypeOracleException e) {
        LOG.log(Level.SEVERE, "Type Oracle Failed", e);
      }
    }
    assert property.type != null : "Property type not detected";
    property.typeString = getPropertyTypeString(property.type);
    if(property.type instanceof JParameterizedType) {
      JParameterizedType type = (JParameterizedType)property.type;
      if(type.getBaseType().isAssignableTo(mapType)) {
        property.isMap = true;
      }
      for(JClassType cType : type.getTypeArgs()) {
        property.typeParameters.add(cType);
      }
    }
    property.isPrimitive = property.type.isPrimitive() != null;
    if(property.isPrimitive) {
      JPrimitiveType prim = property.type.isPrimitive();
      property.objectCast = getObjectCastForPrimitive(prim);
    }
  }

  private String getObjectCastForPrimitive(JPrimitiveType prim) {
    if(prim == JPrimitiveType.BOOLEAN) {
      return "Boolean";
    } else if(prim == JPrimitiveType.BYTE) {
      return "Byte";
    } else if(prim == JPrimitiveType.CHAR) {
      return "Character";
    } else if(prim == JPrimitiveType.DOUBLE) {
      return "Double";
    } else if(prim == JPrimitiveType.FLOAT) {
      return "Float";
    } else if(prim == JPrimitiveType.INT) {
      return "Integer";
    } else if(prim == JPrimitiveType.LONG) {
      return "Long";
    } else if(prim == JPrimitiveType.SHORT) {
      return "Short";
    } else {
      return "Void";
    }
  }

  @Override
  public String generate(TreeLogger treeLogger,
                         GeneratorContext generatorContext,
                         String typeName) throws UnableToCompleteException {
    JClassType classType;

    try {
      mapType = generatorContext.getTypeOracle().getType("java.util.Map");

      classType = generatorContext.getTypeOracle().getType(typeName);

      SourceWriter src = getSourceWriter(classType, generatorContext, treeLogger);
      if(src == null) {
        return typeName + "Generated";
      }
      src.println("@Override public Logger getLogger() { return LOG; }");

      Map<String, Property> properties = getProperties(classType);

      writeConstructor(src, classType, properties);
      writeFields(src, classType, properties);
      writeMappers(src, classType, properties);

      writeGetters(src, properties);
      writeSetters(src, properties);
      writeCollectionMethods(src, properties);

      src.commit(treeLogger);
      System.out.println("Generating for: " + typeName);
      return typeName + "Generated";

    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void writeMappers(SourceWriter src, JClassType classType, Map<String, Property> properties) {

    for(JMethod method : classType.getOverridableMethods()) {
      if(method.isAnnotationPresent(Mapper.class)) {
        boolean mapTo = method.getName().startsWith("mapTo");
        boolean mapFrom = method.getName().startsWith("mapFrom");
        if(mapTo || mapFrom) {
          src.println("@Override");
          src.println(method.getReadableDeclaration(false, true, true, true, true) + " {");

          JParameter parameter = method.getParameters()[0];
          JType dtoType = method.getParameterTypes()[0];
          JClassType dtoClass = classType.getOracle().findType(dtoType.getQualifiedSourceName());

          Map<String, String> mapNames = new HashMap<String, String>();
          Map<String, Mapping> mappings = new HashMap<String, Mapping>();
          for(Annotation annotation : method.getAnnotations()) {
            if(annotation instanceof Mapping) {
              Mapping mapping = (Mapping)annotation;
              mappings.put(mapping.property(), mapping);
            } else if(annotation instanceof MapName) {
              MapName mapName = (MapName)annotation;
              if(mapTo) {
                mapNames.put(mapName.from(), mapName.to());
              } else {
                mapNames.put(mapName.to(), mapName.from());
              }
            } else if(annotation instanceof Mappings) {
              for(Mapping mapping : ((Mappings)annotation).value()) {
                mappings.put(mapping.property(), mapping);
              }
            }
          }

          if(mapTo) {
            for(Property property : properties.values()) {
              if(mapNames.containsKey(property.name)) {
                String nameCap = mapNames.get(property.name);
                nameCap = nameCap.substring(0, 1).toUpperCase() +
                        nameCap.substring(1, nameCap.length());
                String setter = "set" + nameCap;
                src.println("\t" + parameter.getName() + "." + setter +
                        "(" + property.name + ");");
                continue;
              }
              JMethod setter = null;
              for(JMethod dMethod : dtoClass.getOverridableMethods()) {
                if(dMethod.getName().equals("set" + property.nameCap)) {
                  setter = dMethod;
                  break;
                }
              }
              if(setter != null) {
                Mapping mapping = mappings.get(property.name);
                if(mapping != null) {
                  src.println("\t" + parameter.getName() + "." + setter.getName() +
                          "(" + mapping.mapper().getCanonicalName() + ".map(" +
                          property.name + "));");
                } else {
                  src.println("\t" + parameter.getName() + "." + setter.getName() +
                          "(" + property.name + ");");
                }
              }
            }
          } else {
            for(Property property : properties.values()) {
              if(mapNames.containsKey(property.name)) {
                String nameCap = mapNames.get(property.name);
                nameCap = nameCap.substring(0, 1).toUpperCase() +
                        nameCap.substring(1, nameCap.length());

                JMethod getter = findMethod(dtoClass, "get" + nameCap, new JType[]{});
                if(getter == null) {
                  getter = findMethod(dtoClass, "is" + nameCap, new JType[]{});
                }
                if(getter != null) {
                  src.println("\t" + property.name + " = " + parameter.getName() + "." +
                          getter.getName() + "();");
                }
                continue;
              }

              JMethod getter = findMethod(dtoClass, "get" + property.nameCap, new JType[] {});

              if(getter == null) {
                getter = findMethod(dtoClass, "is" + property.nameCap, new JType[]{});
              }
              if(getter != null) {
                Mapping mapping = mappings.get(property.name);
                if(mapping != null) {
                  src.println("\tthis." + property.name + " = " +
                          mapping.mapper().getCanonicalName() + "." +
                          "map(" +
                          parameter.getName() + "." + getter.getName() + "());");
                } else {
                  src.println("\tthis." + property.name + " = " + parameter.getName() + "." +
                          getter.getName() + "();");
                }
              }
            }
          }
          src.println("}");
        }
      }
    }
  }

  JMethod findMethod(JClassType classType, String name, JType[] args) {
    for(JMethod dtoMethod : classType.getOverridableMethods()) {
      if(dtoMethod.getName().equals(name) &&
              dtoMethod.getParameterTypes().length == args.length) {
        int cnt = 0;
        boolean matched = true;
        for(JType type : dtoMethod.getParameterTypes()) {
          if(!type.equals(args[cnt])) {
            matched = false;
            break;
          }
          cnt++;
        }
        if(matched) {
          return dtoMethod;
        }
      }
    }
    return null;
  }

  void writeConstructor(SourceWriter src, JClassType classType,
                        Map<String, Property> properties) throws UnableToCompleteException {

    src.print("public " + classType.getName() + "Generated(");
    if(classType.getConstructors().length == 0) {
      src.println(") {");
    } else if(classType.getConstructors().length == 1) {
      int cnt = 0;
      JConstructor constructor = classType.getConstructors()[0];
      if(constructor.getParameters().length == 0) {
      } else {
        for(JParameter p : constructor.getParameters()) {
          if(cnt != 0) {
            src.print(", ");
          }
          src.print(p.getType().getQualifiedSourceName() + " p" + cnt);
          cnt++;
        }
      }
      src.println(") {");
      src.print("super(");
      cnt = 0;
      for(JParameter p : constructor.getParameters()) {
        if(cnt != 0) {
          src.print(", ");
        }
        src.print("p" + cnt);
        cnt++;
      }
      src.println(");");
    } else {
      throw new UnableToCompleteException();
    }
    src.println("}");
  }

  void writeFields(SourceWriter src, JClassType classType, Map<String, Property> properties) {
    src.println("static final private Logger LOG = Logger.getLogger(\"" + classType.getName() + "Generated\");");
    src.println("static { LOG.setLevel(Level.WARNING); }");

    /*for(Property property : properties.values()) {
      src.print("private " + property.typeString + " " + property.name);

      if(property.defaultIntValue != null) {
        src.println(" = " + property.defaultIntValue.value() + ";");
      } else if(property.defaultStringValue != null) {
        src.println(" = \"" + property.defaultStringValue.value() + "\";");
      } else if(property.defaultBooleanValue != null) {
        src.println(" = " + property.defaultBooleanValue.value() + ";");
      } else if(property.isCollection) {
        src.println(" = new " + property.typeString + "();");
      } else {
        src.println(";");
      }
    }*/
  }

  private void writePutInto(SourceWriter src, Property property) {
    src.println("@Override");
    src.println("public void putInto" + property.nameCap + "(" +
            property.typeParameters.get(0).getQualifiedSourceName() + " key, " +
            property.typeParameters.get(1).getQualifiedSourceName() + " val) {");
    src.println("\t" + property.name + ".put(key, val);");

    src.println("\tgetHandler(\"" + property.name + "\").fireEvent(new MapEvent(this, \"" +
            property.name + "\", key));");

    src.println("}");
  }

  private void writeRemover(SourceWriter src, Property property) {
    if(property.remover != null) {
      src.println("@Override");
      src.println("public void " + property.remover.getName() + "(" +
              property.typeParameters.get(0).getQualifiedSourceName() + " val) {");
      src.println("\t" + property.name + ".remove(val);");

      if(property.isMap) {
        src.println("\tgetHandler(\"" + property.name + "\").fireEvent(new MapEvent(this, \"" +
                property.name + "\", val));");
      } else {
        src.println("\tgetHandler(\"" + property.name + "\").fireEvent(new "
                + "CollectionRemoveEvent(val));");
      }

      src.println("}");
    }
  }

  private void writeCollectionMethods(SourceWriter src, Map<String, Property> properties) {
    for(Property property : properties.values()) {
      if(property.isCollection) {
        writeRemover(src, property);
        writeCount(src, property);
        if(property.isMap) {
          writePutInto(src, property);
          writeGetFrom(src, property);
        } else {
          writeAdder(src, property);
          writeGetAt(src, property);
        }
      }
    }
  }

  private void writeCount(SourceWriter src, Property property) {
    if(property.count == null) {
      return;
    }
    src.println("@Override");
    src.println("public int get" + property.nameCap + "Count() {");
    src.println("\treturn " + property.name + ".size();");
    src.println("}");
  }

  private void writeGetAt(SourceWriter src, Property property) {
    if(property.getAt == null) {
      return;
    }
    src.println("@Override");
    src.println("public " + property.typeParameters.get(0).getQualifiedSourceName() +
            " " + property.getAt.getName() + "(int index) {");
    src.println("\treturn " + property.name + ".get(index);");
    src.println("}");
  }

  private void writeAdder(SourceWriter src, Property property) {
    if(property.adder != null) {
      src.println("@Override");
      src.println("public void " + property.adder.getName() + "(" +
              property.typeParameters.get(0).getQualifiedSourceName() + " val) {");
      src.println("\t" + property.name + ".add(val);");
      if(property.afterAdd != null) {
        src.println("\t" + property.afterAdd.getName() + "(val);");
      }

      src.println("\tgetHandler(\"" + property.name + "\").fireEvent(new "
              + "CollectionAddEvent(val));");

      src.println("}");
    }
  }

  private void writeGetFrom(SourceWriter src, Property property) {
    src.println("@Override");
    src.println("public " + property.typeParameters.get(1).getQualifiedSourceName() +
            " getFrom" + property.nameCap + "(" +
            property.typeParameters.get(0).getQualifiedSourceName() + " key) {");
    src.println("\treturn " + property.name + ".get(key);");
    src.println("}");
  }

  private void writeSetters(SourceWriter src, Map<String, Property> properties) {
    writeSetProperties(src, properties);
  }

  private void writeGetters(SourceWriter src, Map<String, Property> properties) {
    writeGetPropertyNames(src, properties);
    writeGetProperties(src, properties);
  }

  private void writeGetPropertyNames(SourceWriter writer, Map<String, Property> propertyNames) {
    writer.println("@Override");
    writer.println("public List<String> getPropertyNames() {");
    writer.println("\treturn Arrays.asList(new String[] {");
    for(Property property : propertyNames.values()) {
      writer.println("\t\t\"" + property.name + "\",");
    }
    writer.println("\t});");
    writer.println("}");
  }

  private void writeSetProperties(SourceWriter writer, Map<String, Property> properties) {
    writer.println("@Override");
    writer.println("public void setProperty(String property, Object value, boolean fireEvents) {");
    writer.println("\tLOG.info(\"set property \" + property);");
    boolean first = true;
    for(Property property : properties.values()) {
      String statement = "\t} else if";
      if(first) {
        first = false;
        statement = "\tif";
      }
      writer.println(statement + "(property.equals(\"" + property.name + "\")) {");

      String cast = property.typeString;
      if(property.isPrimitive) {
        cast = property.objectCast;
      }
      if(property.setter != null) {
        writer.println("\t\t" + property.setter.getName() + "((" + cast + ")value, fireEvents);");
      } else {
        writer.println("\t\t" + property.name + " = (" + cast + ")value;");
      }
    }
    if(properties.size() > 0) {
      writer.println("\t}");
    }
    writer.println("}");

    for(Property property : properties.values()) {
      if(property.setter == null) {
        continue;
      }

      writer.println("public void " + property.setter.getName() + "("
              + property.typeString + " val, boolean fireEvents) {");
      writer.println("\tLOG.info(\"set " + property.name + " to \" + val + \" fireEvents is \" + fireEvents);");
      if(property.filter != null) {
        writer.println("val = " + property.filter.getName() + "(val);");
      }
      if(property.isPrimitive) {
        writer.println("\tif(val != this." + property.name + ") {");
      } else {
        writer.println("\tif((val == null && this." + property.name + " != null) ||" +
                "(val != null && !val.equals(this." + property.name + "))) {");
      }
      writer.println("\t\tLOG.info(\"changing\");");
      writer.println("\t\tObject prev = this." + property.name + ";");
      writer.println("\t\tthis." + property.name + " = val;");

      writer.println("\t\tif(fireEvents) {");
      writer.println("\t\t\tLOG.info(\"firing\");");
      writer.println("\t\t\tgetHandler(\"" + property.name + "\").fireEvent(new PropertyChangeEvent(\"" + property.name + "\", prev));");
      writer.println("\t\t}");

      if(property.observableEvent != null) {
        writer.println("\t\tif(fireEvents) {");
        writer.println("\t\t\thandlerManager.fireEvent(new " +
                property.observableEvent.getCanonicalName()
                + "(this));");
        writer.println("\t\t}");
      }
      if(property.afterSet != null) {
        writer.println(property.afterSet.getName() + "();");
      }
      writer.println("\t}");
      writer.println("}");

      writer.println("@Override");
      writer.println("public void " + property.setter.getName() + "("
              + property.typeString + " val) {");
      writer.println("\t" + property.setter.getName() + "(val, true);");
      writer.println("}");
    }
  }

  private void writeGetProperties(SourceWriter writer, Map<String, Property> properties) {
    writer.println("@Override");
    writer.println("public Object getProperty(String property) {");
    boolean first = true;
    for(Property property : properties.values()) {
      String statement = "\t} else if";
      if(first) {
        first = false;
        statement = "\tif";
      }

      writer.println(statement + "(property.equals(\"" + property.name + "\")) {");
      if(property.getter != null) {
        writer.println("\t\treturn " + property.getter.getName() + "();");
      } else {
        writer.println("\t\treturn " + property.name + ";");
      }
    }
    if(properties.size() > 0) {
      writer.println("\t}");
    }
    writer.println("\treturn null;");
    writer.println("}");

    for(Property property : properties.values()) {
      if(property.getter == null) {
        continue;
      }
      writer.println("@Override");
      writer.println("public " + property.typeString + " " + property.getter.getName() + "() {");
      if(property.beforeGet != null) {
        writer.println("\t" + property.beforeGet.getName() + "();");
      }
      if(property.isCollection) {
        writer.println("\treturn new " + property.typeString + "(" + property.name + ");");
      } else {
        writer.println("\treturn " + property.name + ";");
      }
      writer.println("}");
    }
  }

  private SourceWriter getSourceWriter(JClassType classType,
                                       GeneratorContext context,
                                       TreeLogger logger) throws UnableToCompleteException {
    String packageName = classType.getPackage().getName();

    String simpleName = classType.getSimpleSourceName() + "Generated";
    ClassSourceFileComposerFactory composer = new ClassSourceFileComposerFactory(
            packageName, simpleName);

    composer.setSuperclass(classType.getQualifiedSourceName());
    composer.addImplementedInterface("com.chrisgammage.gwtjitsu.client.Generated");

    String[] imports = {
            "java.util.List",
            "java.util.Arrays",
            "com.chrisgammage.gwtjitsu.client.events.PropertyChangeEvent",
            "com.chrisgammage.gwtjitsu.client.events.PropertyChangeHandler",
            "com.google.gwt.event.shared.HandlerRegistration",
            "com.google.gwt.event.shared.HandlerManager",
            "com.google.gwt.event.shared.GwtEvent",
            "com.google.gwt.event.shared.EventHandler",
            "com.chrisgammage.gwtjitsu.client.events.CollectionAddEvent",
            "com.chrisgammage.gwtjitsu.client.events.CollectionAddHandler",
            "com.chrisgammage.gwtjitsu.client.events.CollectionRemoveEvent",
            "com.chrisgammage.gwtjitsu.client.events.CollectionRemoveHandler",
            "com.chrisgammage.gwtjitsu.client.events.MapEvent",
            "com.chrisgammage.gwtjitsu.client.events.MapHandler",
            "java.util.Map",
            "java.util.HashMap",
            "java.util.ArrayList",
            "java.util.logging.Logger",
            "java.util.logging.Level",
            "javax.inject.Inject",
            "javax.inject.Singleton",
            "com.chrisgammage.gwtjitsu.client.Model"
    };

    for(String imp : imports) {
      composer.addImport(imp);
    }

    PrintWriter printWriter = context.tryCreate(logger, packageName,
            simpleName);
    if(printWriter == null) {
      return null;
    } else {
      SourceWriter sw = composer.createSourceWriter(context, printWriter);
      return sw;
    }
  }
}
