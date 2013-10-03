package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Token;

/**
 * TrigramAnnotator takes Annotation spans and outputs NGram annotations for each trigram in the span.
 * @author jeff
 *
 */
public class TrigramAnnotator extends NGramAnnotator {

  private static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.TrigramAnnotator";
  
  /**
   * Outputs Trigrams for an input Annotation.
   */
  public void extractNgramsFromAnnotation(Annotation annotation, JCas aJCas) {

    AnnotationIndex<Annotation> tokens = aJCas.getAnnotationIndex(Token.type);
    Iterator<Annotation> tIterator = tokens.subiterator(annotation);

    Annotation token = null;
    Annotation prev = null;
    Annotation prev_prev = null;

    while (tIterator.hasNext()) {

      prev_prev = prev;
      prev = token;
      token = tIterator.next();

      if (tokenIsInAnnotation(annotation, prev) && tokenIsInAnnotation(annotation, prev_prev)) {

        NGram ngram = new NGram(aJCas);
        FSArray ngArray = new FSArray(aJCas, 3);

        ngArray.set(0, prev_prev);
        ngArray.set(1, prev);
        ngArray.set(2, token);

        ngram.setBegin(prev_prev.getBegin());
        ngram.setEnd(token.getEnd());

        ngram.setElements(ngArray);
        ngram.setElementType("edu.cmu.deiis.types.Token");

        ngram.setConfidence(1D);
        ngram.setCasProcessorId(PROCESSOR_ID);
        ngram.addToIndexes();
      } 
    }

  }

}
