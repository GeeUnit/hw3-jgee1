package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

/**
 * AbstractSingleQuestionScorer is an abstract class for AnswerScorers designed to target documents
 * with a single question.
 * 
 * @author jeff
 * 
 */
public abstract class AbstractSingleQuestionScorer extends AbstractQAAnnotator {

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    AnnotationIndex<Annotation> questions = aJCas.getAnnotationIndex(Question.type);
    AnnotationIndex<Annotation> answers = aJCas.getAnnotationIndex(Answer.type);

    Iterator<Annotation> qIterator = questions.iterator();

    //Uses an 'if'-statement instead of a 'while'-statement, as it assumes there is only a single question per document.
    if (qIterator.hasNext()) {
      Question q = (Question) qIterator.next();

      Iterator<Annotation> aIterator = answers.iterator();

      while (aIterator.hasNext()) {
        scoreAnswer(q, (Answer) aIterator.next(), aJCas);
      }
    }
  }

  /**
   * Abstract method that scores a single answer against a single question.
   * @param question an input Question
   * @param answer an input Answer
   * @param jcas JCas object for this document.
   */
  public abstract void scoreAnswer(Question q, Answer answer, JCas jcas);

}
