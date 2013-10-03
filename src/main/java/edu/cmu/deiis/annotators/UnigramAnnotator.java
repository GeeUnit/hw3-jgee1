package edu.cmu.deiis.annotators;

import java.util.Iterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Token;

/**
 * UnigramAnnotator takes Annotation spans and outputs NGram annotations for each unigram in the span.
 * @author jeff
 *
 */
public class UnigramAnnotator extends NGramAnnotator {

  private static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.UnigramAnnotator";

  /**
   * Generate Unigram annotations for an input Annotation span
   */
  public void extractNgramsFromAnnotation(Annotation annotation, JCas aJCas) {

    AnnotationIndex<Annotation> tokens=aJCas.getAnnotationIndex(Token.type);
    
    Iterator<Annotation> tIterator=tokens.subiterator(annotation);

    Annotation token = null;

    while (tIterator.hasNext()) {
        token=tIterator.next();
      
        NGram ngram = new NGram(aJCas);
        FSArray ngArray = new FSArray(aJCas, 1);

        ngArray.set(0, token);

        ngram.setBegin(token.getBegin());
        ngram.setEnd(token.getEnd());

        ngram.setElements(ngArray);
        ngram.setElementType("edu.cmu.deiis.types.Token");

        ngram.setConfidence(1D);
        ngram.setCasProcessorId(PROCESSOR_ID);
        ngram.addToIndexes(); 
    }
  }
}
