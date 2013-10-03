package edu.cmu.deiis.annotators;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;

/**
 * NGramOverlapScorer scores an Answer against a Question based on how many overlapping NGrams they
 * contain
 * 
 * @author jeff
 * 
 */
public class NGramOverlapScorer extends AbstractSingleQuestionScorer {

  public static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.NGramOverlapScorer";

  /**
   * Scores a Question against an Answer, based on the overlap of NGram annotations they have.
   */
  public void scoreAnswer(Question q, Answer ans, JCas jcas) {

    AnnotationIndex<Annotation> ngrams = jcas.getAnnotationIndex(NGram.type);

    //get set of Question NGrams
    Set<String> qNGrams = getNgramsForAnnotationSpan(q, ngrams);

    //get set of Answer NGrams
    Set<String> ansNGrams = getNgramsForAnnotationSpan(ans, ngrams);

    AnswerScore as = new AnswerScore(jcas);
    as.setAnswer(ans);
    as.setBegin(ans.getBegin());
    as.setEnd(ans.getEnd());
    as.setCasProcessorId(PROCESSOR_ID);
    as.setConfidence(1D);

    double numANGrams = ansNGrams.size();

    //get intersection of NGrams set
    ansNGrams.retainAll(qNGrams);
    double numOverlapNGrams = ansNGrams.size();
    
    //divide size of the intersection over the size of the answer
    as.setScore(numOverlapNGrams / numANGrams);

    as.addToIndexes();

  }

  /**
   * Retrieve a set of NGrams for a specific annotation span.
   * @param annotation Input Annotation span.
   * @param ngrams NGram AnnotationIndex
   * @return
   */
  private Set<String> getNgramsForAnnotationSpan(Annotation annotation,
          AnnotationIndex<Annotation> ngrams) {
    Set<String> aNGrams = new HashSet<String>();
    Iterator<Annotation> ngIterator = ngrams.subiterator(annotation);

    while (ngIterator.hasNext()) {
      aNGrams.add(ngIterator.next().getCoveredText());
    }
    return aNGrams;
  }
}
