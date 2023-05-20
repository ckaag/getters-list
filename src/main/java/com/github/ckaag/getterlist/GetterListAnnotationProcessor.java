package com.github.ckaag.getterlist;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes(
        "lombok.experimental.FieldNameConstants")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GetterListAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (TypeElement annotation : annotations) {
            var annotatedElements
                    = roundEnv.getElementsAnnotatedWith(annotation);

            for (Element clazz : annotatedElements) {
                StringBuilder getterCalls = new StringBuilder();
                List<? extends Element> elements = clazz.getEnclosedElements();
                for (Element element : elements) {
                    if (element.getKind() != ElementKind.METHOD) {
                        continue;
                    }
                    var methodName = element.toString();
                    boolean withoutParameters = element.asType().toString().startsWith("()");
                    if (element.asType().toString().endsWith("void") || !withoutParameters) {
                        continue;
                    }
                    var prefixes = List.of("get", "is");
                    for (String prefix : prefixes) {
                        if (methodName.startsWith(prefix)) {
                            var beanFieldName = String.valueOf(methodName.charAt(prefix.length())).toLowerCase() + methodName.substring(prefix.length() + 1, methodName.length() - 2);
                            getterCalls.append("        list.add(new AbstractMap.SimpleImmutableEntry<>(\"").append(beanFieldName).append("\", (obj) -> obj.").append(methodName).append("));\n");
                        }
                    }

                    System.out.println(element);
                }

                if (getterCalls.length() > 0) {
                    String originalClassname = clazz.toString();
                    try {
                        writeOutputFile(originalClassname, getterCalls);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return false;
    }

    private void writeOutputFile(String className, StringBuilder getterCalls) throws IOException {

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String builderClassName = className + "GetterList";
        String builderSimpleClassName = builderClassName
                .substring(lastDot + 1);

        JavaFileObject builderFile;
        builderFile = processingEnv.getFiler()
                .createSourceFile(builderClassName);


        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.println("import java.util.List;");
            out.println("import java.util.ArrayList;");
            out.println("import java.util.AbstractMap;");
            out.println("import java.util.function.Function;");
            out.println();

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();
            out.print(
                    """
                                private ExampleDtoGetterList() {
                                }

                            """);

            var listType = "List<AbstractMap.SimpleImmutableEntry<String, Function<" + className + ", Object>>>";
            out.print("    public ");
            out.print(listType);
            out.print(" getGetters() ");
            out.println("{");
            out.print("        " + listType);
            out.println(" list = new ArrayList<>();");
            out.println(getterCalls.toString());
            out.println("        return list;");
            out.println("    }");
            out.println();


            out.println("}");
        }
    }


}
