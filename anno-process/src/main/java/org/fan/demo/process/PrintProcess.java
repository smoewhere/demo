package org.fan.demo.process;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * @version 1.0
 * @author: Fan
 * @date 2020.11.26 16:03
 */
@SupportedAnnotationTypes({"org.fan.demo.annotation.PrintMe"})
public class PrintProcess extends AbstractProcessor {

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Messager messager = processingEnv.getMessager();
    for (TypeElement te : annotations) {
      for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
        messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString());
      }
    }
    return false;
  }
}
