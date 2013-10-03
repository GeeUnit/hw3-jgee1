package edu.cmu.deiis.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.tcas.Annotation;

/**
 * AbstractQAAnnotator is an abstract class for a basic Q&A System. It will house all
 * utility-related functions shared by all Annotators.
 * 
 * @author jeff
 * 
 */
public abstract class AbstractQAAnnotator extends JCasAnnotator_ImplBase {

  /**
   * Returns true if a second annotation is contained within the span of a first annotation. Returns
   * false otherwise.
   * 
   * @param annotation
   * @param annotation2
   * @return
   */
  protected boolean annotationInAnnotation(Annotation annotation, Annotation annotation2) {
    if (annotation2 == null) {
      return false;
    }
    if (annotation2.getBegin() >= annotation.getBegin()
            && annotation2.getEnd() <= annotation.getEnd()) {
      return true;
    }
    return false;
  }
}
