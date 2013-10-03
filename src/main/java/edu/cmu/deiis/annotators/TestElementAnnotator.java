package edu.cmu.deiis.annotators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

/**
 * TestElementAnnotator takes document text and locates Question and Answer spans.
 * 
 * @author jeff
 * 
 */
public class TestElementAnnotator extends JCasAnnotator_ImplBase {

  private static final String PROCESSOR_ID = "edu.cmu.deiis.annotators.TestElementAnnotator";

  /**
   * Questions are characterized by lines starting with "Q "
   */
  private static String QUESTION_PATTERN = "^Q\\s(.*\\?)";

  /**
   * Answers are characterized by lines starting with an "A <score>"
   */
  private static String ANSWER_PATTERN = "^A\\s(\\d)\\s(.*\\.)";

  private Pattern questionPattern;

  private Pattern answerPattern;

  /**
   * Compiles question and answer patterns
   */
  public void initialize(UimaContext uimaContext) throws ResourceInitializationException {
    super.initialize(uimaContext);
    questionPattern = Pattern.compile(QUESTION_PATTERN, Pattern.MULTILINE);
    answerPattern = Pattern.compile(ANSWER_PATTERN, Pattern.MULTILINE);
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {

    String inputDocument = aJCas.getDocumentText();

    Matcher questionMatcher = questionPattern.matcher(inputDocument);

    // Find all question patterns, and create Question annotations
    while (questionMatcher.find()) {
      Question newQuestion = new Question(aJCas);
      newQuestion.setBegin(questionMatcher.start(1));
      newQuestion.setEnd(questionMatcher.end(1));
      newQuestion.setCasProcessorId(PROCESSOR_ID);
      newQuestion.setConfidence(1D);
      newQuestion.addToIndexes();
    }

    Matcher answerMatcher = answerPattern.matcher(inputDocument);

    // Find all answer patterns, and create Answer annotations
    while (answerMatcher.find()) {
      Answer newAnswer = new Answer(aJCas);
      newAnswer.setBegin(answerMatcher.start(2));
      newAnswer.setEnd(answerMatcher.end(2));
      newAnswer.setCasProcessorId(PROCESSOR_ID);
      newAnswer.setConfidence(1D);

      if (answerMatcher.group(1).equals("1")) {
        newAnswer.setIsCorrect(true);
      } else if (answerMatcher.group(1).equals("0")) {
        newAnswer.setIsCorrect(false);
      }

      newAnswer.addToIndexes();
    }
  }

}
