package com.chrisgammage.gwtjitsu.server;

import com.chrisgammage.ginjitsu.server.GeneratorHelper;
import com.chrisgammage.gwtjitsu.shared.*;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import javax.validation.constraints.NotNull;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gammagec
 * Date: 10/29/12
 * Time: 2:15 PM
 */
public class PresenterGenerator extends Generator {

  Map<String, JField> models = new HashMap<String, JField>();

  private class ModelElement {
    private String property;
    private String modelName;
    private JMethod callback;
    private boolean notNull;
  }

  private class SubModelCollectionElement {
    private String model;
    private String collection;
    private JMethod callback;
  }

  private class SubSubModelCollectionElement {
    private String model1;
    private String model2;
    private String collection;
    private JMethod method;
  }

  private class SubModelPropertyElement {
    private String subModel;
    private String property;
    private JMethod method;
  }

  private class SubModelCollectionPropertyElement {
    private String subModel;
    private String collection;
    private String property;
    private JMethod method;
  }

  private List<ModelElement> modelHandlers = new ArrayList<ModelElement>();
  private List<SubModelCollectionElement> subModelCollectionHandlers =
          new ArrayList<SubModelCollectionElement>();
  private List<SubSubModelCollectionElement> subSubModelCollectionHandlers =
          new ArrayList<SubSubModelCollectionElement>();
  private List<SubModelPropertyElement> subModelPropertyHandlers =
          new ArrayList<SubModelPropertyElement>();
  private List<SubModelCollectionPropertyElement> subModelCollectionPropertyHandlers =
          new ArrayList<SubModelCollectionPropertyElement>();

  @Override
  public String generate(TreeLogger logger,
                         GeneratorContext context,
                         String typeName) throws UnableToCompleteException {
    JClassType classType;
    try {

      classType = context.getTypeOracle().getType(typeName);

      SourceWriter src = getSourceWriter(classType, context, logger);
      if(src == null) {
        return typeName + "Generated";
      }

      findModels(classType);
      findModelHandlers(classType);
      if(models.size() == 0 && modelHandlers.size() > 0) {
        throw new UnableToCompleteException();
      }

      GeneratorHelper.writeConstructor(src, classType);

      src.println("@Override");
      src.println("public void setupModelHandlers() {");
      for(ModelElement handler : modelHandlers) {
        JField model = models.get(handler.modelName);
        src.println("\t" + model.getName() + ".addPropertyChangeHandler(\""
          + handler.property + "\", new PropertyChangeHandler() {");
        src.println("\t\t\t@Override");
        src.println("\t\t\tpublic void onPropertyChange(PropertyChangeEvent event) {");
        if(handler.notNull) {
          src.println("\t\t\t\tif(" + model.getName() + ".getProperty(\"" + handler.property +
                  "\") != null) {");
        }
        src.println("\t\t\t\t" + handler.callback.getName() + "();");
        if(handler.notNull) {
          src.println("\t\t\t\t}");
        }
        src.println("\t\t\t}");
        src.println("\t\t});");
        if(handler.notNull) {
          src.println("\tif(" + model.getName() + ".getProperty(\"" + handler.property +
                  "\") != null) {");
        }
        src.println("\t" + handler.callback.getName() + "();");
        if(handler.notNull) {
          src.println("\t}");
        }
      }
      for(SubModelCollectionElement handler : subModelCollectionHandlers) {
        assert handler.callback.getParameterTypes().length > 2;
        JField model = models.get("");
        JType modelType = handler.callback.getParameterTypes()[0];
        JType objectType = handler.callback.getParameterTypes()[1];
        src.println("\t" + model.getName() + ".addSubModelCollectionHandler(\""
          + handler.model + "\", \"" + handler.collection + "\", new SubModelCollectionHandler<"
                + objectType.getQualifiedSourceName() + ">() {");
        src.println("\t\t\t@Override");
        src.println("\t\t\tpublic void onSubModelCollection(Model subModel, String collectionName, "
          + objectType.getQualifiedSourceName() + " object, boolean removed) {");
        src.println("\t\t\t\t" + handler.callback.getName() + "(" +
                "(" + modelType.getQualifiedSourceName() + ")subModel , object, removed);");
        src.println("\t\t\t}");
        src.println("\t\t});");
      }
      for(SubSubModelCollectionElement handler : subSubModelCollectionHandlers) {
        assert handler.method.getParameterTypes().length > 3;
        JField model = models.get("");
        JType model1Type = handler.method.getParameterTypes()[0];
        JType model2Type = handler.method.getParameterTypes()[1];
        JType objectType = handler.method.getParameterTypes()[2];
        src.println("\t" + model.getName() + ".addSubSubModelCollectionHandler(\""
                + handler.model1 + "\", \"" + handler.model2 + "\", \"" + handler.collection + "\", new SubSubModelCollectionHandler<"
                + objectType.getQualifiedSourceName() + ">() {");
        src.println("\t\t\t@Override");
        src.println("\t\t\tpublic void onSubSubModelCollection(Model model1, Model model2, String collectionName, "
                + objectType.getQualifiedSourceName() + " object, boolean removed) {");
        src.println("\t\t\t\t" + handler.method.getName() + "("
                + "(" + model1Type.getQualifiedSourceName() + ")model1, (" +
                model2Type.getQualifiedSourceName() + ")model2, object, removed);");
        src.println("\t\t\t}");
        src.println("\t\t});");
      }
      for(SubModelPropertyElement handler : subModelPropertyHandlers) {
        assert handler.method.getParameterTypes().length > 1;
        JField model = models.get("");
        //JType modelType = handler.method.getParameterTypes()[0];
        src.println("\t" + model.getName() + ".addSubModelPropertyChangeHandler(\""
                + handler.subModel + "\", \"" + handler.property + "\", new SubModelPropertyChangeHandler() {");
        src.println("\t\t\t@Override");
        src.println("\t\t\tpublic void onPropertyPropertyChange(String modelName, String propertyName) {");
        src.println("\t\t\t\t" + handler.method.getName() + "();");
        src.println("\t\t\t}");
        src.println("\t\t});");
      }
      for(SubModelCollectionPropertyElement handler : subModelCollectionPropertyHandlers) {
        assert handler.method.getParameterTypes().length > 1;
        JType modelType = handler.method.getParameterTypes()[0];
        JField model = models.get("");
        src.println("\t" + model.getName() + ".addSubModelCollectionPropertyHandler(\""
                + handler.subModel + "\", \"" + handler.collection + "\", \"" + handler.property
                + "\", new SubModelCollectionPropertyHandler() {");
        src.println("\t\t\t@Override");
        src.println("\t\t\tpublic void onSubModelCollectionProperty(Object model, String propertyName) {");
        src.println("\t\t\t\t" + handler.method.getName() + "("
          + "(" + modelType.getQualifiedSourceName() + ")model, propertyName);");
        src.println("\t\t\t}");
        src.println("\t\t});");
      }
      src.println("}");

      src.commit(logger);
      System.out.println("Generating for: " + typeName);
      return typeName + "Generated";

    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void findModelHandlers(JClassType clazz) {
    for(JMethod method : GeneratorHelper.getAllMethods(clazz)) {
      if(method.isAnnotationPresent(AfterModelSet.class)) {
        String property = method.getAnnotation(AfterModelSet.class).value();
        ModelElement modelHandler = new ModelElement();
        modelHandler.property = property;
        modelHandler.callback = method;
        modelHandler.modelName = "";
        modelHandler.notNull = method.getAnnotation(NotNull.class) != null;
        modelHandlers.add(modelHandler);
      } else if(method.isAnnotationPresent(AfterNamedModelSet.class)) {
        String property = method.getAnnotation(AfterNamedModelSet.class).property();
        String modelName = method.getAnnotation(AfterNamedModelSet.class).model();
        ModelElement modelHandler = new ModelElement();
        modelHandler.modelName = modelName;
        modelHandler.property = property;
        modelHandler.callback = method;
        modelHandler.notNull = method.getAnnotation(NotNull.class) != null;
        modelHandlers.add(modelHandler);
      } else if(method.isAnnotationPresent(AfterSubModelCollectionChanged.class)) {
        AfterSubModelCollectionChanged ann = method.getAnnotation(AfterSubModelCollectionChanged.class);
        SubModelCollectionElement handler = new SubModelCollectionElement();
        handler.model = ann.subModel();
        handler.collection = ann.collection();
        handler.callback = method;
        subModelCollectionHandlers.add(handler);
      } else if(method.isAnnotationPresent(AfterSubSubModelCollectionChanged.class)) {
        AfterSubSubModelCollectionChanged ann = method.getAnnotation(AfterSubSubModelCollectionChanged.class);
        SubSubModelCollectionElement handler = new SubSubModelCollectionElement();
        handler.model1 = ann.subModel1();
        handler.model2 = ann.subModel2();
        handler.collection = ann.collection();
        handler.method = method;
        subSubModelCollectionHandlers.add(handler);
      } else if(method.isAnnotationPresent(AfterSubModelPropertyChanged.class)) {
        AfterSubModelPropertyChanged ann = method.getAnnotation(AfterSubModelPropertyChanged.class);
        SubModelPropertyElement handler = new SubModelPropertyElement();
        handler.subModel = ann.subModel();
        handler.property = ann.property();
        handler.method = method;
        subModelPropertyHandlers.add(handler);
      } else if(method.isAnnotationPresent(AfterSubModelCollectionPropertyChanged.class)) {
        AfterSubModelCollectionPropertyChanged ann = method.getAnnotation(AfterSubModelCollectionPropertyChanged.class);
        SubModelCollectionPropertyElement handler = new SubModelCollectionPropertyElement();
        handler.subModel = ann.subModel();
        handler.collection = ann.collection();
        handler.property = ann.property();
        handler.method = method;
        subModelCollectionPropertyHandlers.add(handler);
      }
    }
  }

  private void findModels(JClassType clazz) {
    for(JClassType clazzType : clazz.getFlattenedSupertypeHierarchy()) {
      for(JField field : clazzType.getFields()) {
        if(field.isAnnotationPresent(PresenterModel.class)) {
          PresenterModel pm = field.getAnnotation(PresenterModel.class);
          models.put(pm.value(), field);
        }
      }
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
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.PropertyChangeHandler");
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.PropertyChangeEvent");
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.SubModelCollectionHandler");
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.SubSubModelCollectionHandler");
    composer.addImport("com.chrisgammage.gwtjitsu.client.Model");
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.SubModelPropertyChangeHandler");
    composer.addImport("com.chrisgammage.gwtjitsu.client.events.SubModelCollectionPropertyHandler");

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
