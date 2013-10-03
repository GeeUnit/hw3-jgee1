package edu.cmu.deiis.annotators;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Token;

/**
 * TokenOverlapScorer assigns scores to Answers based on the number of overlapping tokens Answers
 * share with Questions
 * 
 * @author jeff
 * 
 */
public class TokenOverlapScorer extends AbstractSingleQuestionScorer {

  private static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.TokenOverlapScorer";

  @Override
  public void scoreAnswer(Question q, Answer ans, JCas jcas) {

    AnnotationIndex<Annotation> tokens = jcas.getAnnotationIndex(Token.type);

    //get all Question tokens
    Set<String> qTokens = getTokensForAnnotationSpan(q, tokens);
    
    //get all Answer tokens
    Set<String> aTokens = getTokensForAnnotationSpan(ans, tokens);

    AnswerScore as = new AnswerScore(jcas);
    as.setAnswer(ans);
    as.setBegin(ans.getBegin());
    as.setEnd(ans.getEnd());
    as.setCasProcessorId(PROCESSOR_ID);
    as.setConfidence(1D);

    double numATokens = aTokens.size();

    //calculate the union of tokens
    aTokens.retainAll(qTokens);
    double numOverlapTokens = aTokens.size();
    
    //divid the size of the union by the size of the answer.
    as.setScore(numOverlapTokens / numATokens);
    as.addToIndexes();

  }

  /**
   * Get all tokens for an annotation span.
   * @param annotation Input Annotation span
   * @param tokens AnnotationIndex for the tokens
   * @return
   */
  private Set<String> getTokensForAnnotationSpan(Annotation annotation,
          AnnotationIndex<Annotation> tokens) {
    Set<String> aTokens = new HashSet<String>();
    Iterator<Annotation> tIterator = tokens.subiterator(annotation);

    while (tIterator.hasNext()) {
      Token t = (Token) tIterator.next();
      aTokens.add(t.getCoveredText());
    }
    return aTokens;
  }
}
