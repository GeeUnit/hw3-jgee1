package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Token;


/**
 * BigramAnnotator takes Annotation spans and outputs NGram annotations for each bigram in the span.
 * @author jeff
 *
 */
public class BigramAnnotator extends NGramAnnotator {

  private static String PROCESSOR_ID = "edu.cmu.deiis.annotator.BigramAnnotator";

  /**
   * Outputs Bigrams for an input Annotation.
   */
  public void extractNgramsFromAnnotation(Annotation annotation, JCas aJCas) {

    AnnotationIndex<Annotation> tokens = aJCas.getAnnotationIndex(Token.type);
    Iterator<Annotation> tIterator = tokens.subiterator(annotation);

    Annotation token = null;
    Annotation prev = null;

    while (tIterator.hasNext()) {

      prev = token;
      token = tIterator.next();

      if (tokenIsInAnnotation(annotation, prev)) {

        NGram ngram = new NGram(aJCas);
        FSArray ngArray = new FSArray(aJCas, 2);

        ngArray.set(0, prev);
        ngArray.set(1, token);

        ngram.setBegin(prev.getBegin());
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
