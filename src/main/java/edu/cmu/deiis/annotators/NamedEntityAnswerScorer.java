package edu.cmu.deiis.annotators;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.cleartk.ne.type.NamedEntityMention;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

/**
 * NamedEntityScorer scores an Answer against a Question based on the % of overlapping
 * NamedEntityMentions
 * 
 * @author jeff
 * 
 */
public class NamedEntityAnswerScorer extends AbstractSingleQuestionScorer {

  private static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.NamedEntityAnswerScorer";

  @Override
  public void scoreAnswer(Question q, Answer ans, JCas jcas) {
    AnnotationIndex<Annotation> namedEntities = jcas.getAnnotationIndex(NamedEntityMention.type);
    // get all Question NamedEntityMentions
    Set<String> qNEs = getTokensForAnnotationSpan(q, namedEntities);

    // get all Answer NamedEntityMentions
    Set<String> aNEs = getTokensForAnnotationSpan(ans, namedEntities);

    AnswerScore as = new AnswerScore(jcas);
    as.setAnswer(ans);
    as.setBegin(ans.getBegin());
    as.setEnd(ans.getEnd());
    as.setCasProcessorId(PROCESSOR_ID);
    as.setConfidence(1D);

    double numANE = aNEs.size();

    // calculate the union of named entities
    aNEs.retainAll(qNEs);
    double numOverlapNEs = aNEs.size();

    // divide the size of the union by the size of the answer.
    as.setScore(numOverlapNEs / numANE);
    as.addToIndexes();

  }

  /**
   * Get all NamedEntityMentions for an annotation span as a String set.
   * 
   * @param annotation
   *          Input Annotation span
   * @param tokens
   *          AnnotationIndex for the tokens
   * @return
   */
  private Set<String> getTokensForAnnotationSpan(Annotation annotation,
          AnnotationIndex<Annotation> namedEntities) {
    Set<String> neSet = new HashSet<String>();
    Iterator<Annotation> neIterator = namedEntities.subiterator(annotation);

    while (neIterator.hasNext()) {
      NamedEntityMention ne = (NamedEntityMention) neIterator.next();
      neSet.add(ne.getCoveredText());
    }
    return neSet;
  }

}
