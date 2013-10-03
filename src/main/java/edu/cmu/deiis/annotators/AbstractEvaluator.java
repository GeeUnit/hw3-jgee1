package edu.cmu.deiis.annotators;

import java.util.Comparator;
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

/**
 * The AbstractEvaluator is an abstract class that should be extended by any class evluating a list
 * of AnswerScores
 * 
 * @author jeff
 * 
 */
public abstract class AbstractEvaluator extends AbstractQAAnnotator {

  private int numberOfCAS;
  private double sumOfPrecision;
  
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    this.numberOfCAS=0;
    this.sumOfPrecision=0D;
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    AnnotationIndex<Annotation> scores = aJCas.getAnnotationIndex(AnswerScore.type);
    AnnotationIndex<Annotation> question = aJCas.getAnnotationIndex(Question.type);

    Iterator<Annotation> qIterator = question.iterator();
    while (qIterator.hasNext()) {
      System.out.println("Question: " + qIterator.next().getCoveredText());
    }
    double casPrecision=evaluate(scores);

    numberOfCAS++;
    sumOfPrecision+=casPrecision;
  }

  @Override
  public void destroy()
  {
    System.out.println("Average Precision: "+formatScore(sumOfPrecision/numberOfCAS));
  }
  
  /**
   * Abstract method that evaluates the list of AnswerScores.
   * 
   * @param answerScores
   *          An AnnotationIndex for AnswerScores
   */
  public abstract double evaluate(AnnotationIndex<Annotation> answerScores);

  /**
   * Can be used by any inheriting Evaluator to round scores to two decimal points.
   * 
   * @param d
   *          The double to format
   * @return
   */
  protected double formatScore(double d) {
    double rounded = Math.round(d * 100D) / 100D;
    return rounded;
  }

  /**
   * AnswerScoreComparator is used to order two AnswerScores, ranking the highest numbers first.
   * 
   * @author jeff
   * 
   */
  protected class AnswerScoreComparator implements Comparator<AnswerScore> {
    @Override
    public int compare(AnswerScore as1, AnswerScore as2) {

      // Compare AnswerScores, returning the highest scores first.
      if (as1.getScore() > as2.getScore()) {
        return -1;
      } else if (as1.getScore() < as2.getScore()) {
        return 1;
      }
      return 0;
    }
  }
}
