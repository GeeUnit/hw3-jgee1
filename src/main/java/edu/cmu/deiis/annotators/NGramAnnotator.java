package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

/**
 * NGramAnnotator is an abstract class for all *-Gram Annotators. Its process() method calls an
 * abstract extractNGramsFromAnnotation() method on all Question and Answer spans. Implementation on
 * how NGrams will be extracted is dependent on the actual NGrams being produced.
 * 
 * @author jeff
 * 
 */
public abstract class NGramAnnotator extends AbstractQAAnnotator {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    AnnotationIndex<Annotation> questionIndex = aJCas.getAnnotationIndex(Question.type);
    AnnotationIndex<Annotation> answerIndex = aJCas.getAnnotationIndex(Answer.type);

    // Extract NGrams for each Question
    Iterator<Annotation> qIterator = questionIndex.iterator();
    while (qIterator.hasNext()) {
      extractNgramsFromAnnotation(qIterator.next(), aJCas);
    }
    // Extract NGrams for each Answer
    Iterator<Annotation> aIterator = answerIndex.iterator();
    while (aIterator.hasNext()) {
      extractNgramsFromAnnotation(aIterator.next(), aJCas);
    }
  }

  /**
   * Checks to see if a token lies within an input annotation span
   * 
   * @param annotation
   *          Annotation span
   * @param token
   *          Input Token
   * @return
   */
  protected boolean tokenIsInAnnotation(Annotation annotation, Annotation token) {
    return annotationInAnnotation(annotation, token);
  }

  /**
   * Outputs all NGram annotations for tokens in a single input Annotation span.
   * 
   * @param annotation
   *          Annotation span
   * @param aJCas
   *          Input CAS document
   */
  protected abstract void extractNgramsFromAnnotation(Annotation annotation, JCas aJCas);
}
