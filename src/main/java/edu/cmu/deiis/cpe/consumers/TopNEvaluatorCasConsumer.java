package edu.cmu.deiis.cpe.consumers;

import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.deiis.annotators.TopNEvaluator;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

/**
 * This is a CAS consumer implementation of the TopNEvaluator. It utilizes the
 * *.annotator.TopNEvaluator implementation to process each individual CAS object.
 * 
 * @author jeff
 * 
 */
public class TopNEvaluatorCasConsumer extends CasConsumer_ImplBase {

  private TopNEvaluator evaluator;

  private int casCount;

  private double precSum;

  @Override
  public void initialize() throws ResourceInitializationException {
    super.initialize();
    this.evaluator = new TopNEvaluator();
    this.evaluator.initialize(this.getUimaContext());
    this.casCount = 0;
    this.precSum = 0D;
  }

  /**
   * Evaluate the AnswerScores for each CAS object.
   */
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {

    AnnotationIndex<Annotation> answerScores;
    try {
      JCas jcas = aCAS.getJCas();
      AnnotationIndex<Annotation> question = jcas.getAnnotationIndex(Question.type);
      Iterator<Annotation> qIterator = question.iterator();
      while (qIterator.hasNext()) {
        System.out.println("Question: " + qIterator.next().getCoveredText());
      }

      answerScores = jcas.getAnnotationIndex(AnswerScore.type);
      double precision = this.evaluator.evaluate(answerScores);
      this.precSum += precision;
      this.casCount++;
    } catch (CASRuntimeException e) {

      e.printStackTrace();
    } catch (CASException e) {

      e.printStackTrace();
    }
  }

  /**
   * Upon completion of collecting all CAS objects, outputs the average precision for all CAS
   * objects.
   */
  @Override
  public void collectionProcessComplete(ProcessTrace arg0) {
    double averagePrec = this.precSum / casCount;
    double roundedPrec = Math.round(averagePrec * 100D) / 100D;
    System.out.println("Average Precision: " + roundedPrec);
  }
}
