package edu.cmu.deiis.annotators;


import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.types.Token;
import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

/**
 * TokenAnnotator is responsible for taking text spans and outputting tokens for them.
 * @author jeff
 *
 */
public class TokenAnnotator extends JCasAnnotator_ImplBase{

  private static final String PROCESSOR_ID="edu.cmu.deiis.annotators.TokenAnnotator";
  private static String TOKEN_PATTERN = "(\\b\\S+\\b)";
  
  private Matcher matcher;
  private Pattern tokenPattern;
 
  /**
   * Compile pattern
   */
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    this.tokenPattern=Pattern.compile(TOKEN_PATTERN, Pattern.MULTILINE);

  }
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    AnnotationIndex<Annotation> questionAnnotations=aJCas.getAnnotationIndex(Question.type);
    Iterator<Annotation> qIterator=questionAnnotations.iterator();
    
    //tokenize all Questions
    while(qIterator.hasNext())
    {
      tokenize(qIterator.next(), aJCas);
    }
    
    AnnotationIndex<Annotation> answerAnnotations=aJCas.getAnnotationIndex(Answer.type);
    Iterator<Annotation> aIterator=answerAnnotations.iterator();
   
    //tokenize all Answers
    while(aIterator.hasNext())
    {
      tokenize(aIterator.next(), aJCas);
    } 
    
  }
  
  /**
   * Generate Tokens for a given Annotation span
   * @param annotation Input Annotation span
   * @param aJCas Document CAS to add annotations to.
   */
  private void tokenize(Annotation annotation, JCas aJCas)
  {
    matcher = tokenPattern.matcher(annotation.getCoveredText());
    int annotationStart=annotation.getBegin();
    
    while (matcher.find()) {
      Token token=new Token(aJCas);
      token.setBegin(annotationStart+matcher.start());
      token.setEnd(annotationStart+matcher.end());
      token.setCasProcessorId(PROCESSOR_ID);
      token.setConfidence(1D);
      token.addToIndexes();
    }
  }

}
