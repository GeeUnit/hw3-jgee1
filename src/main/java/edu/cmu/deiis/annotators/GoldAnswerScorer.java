package edu.cmu.deiis.annotators;

import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.Question;

public class GoldAnswerScorer extends AbstractSingleQuestionScorer {

  private static final String PROCESSOR_ID = "edu.cmu.ediis.annotators.GoldAnswerScorer";

  @Override
  public void scoreAnswer(Question q, Answer answer, JCas jcas) {

    AnswerScore as = new AnswerScore(jcas);
    as.setBegin(answer.getBegin());
    as.setEnd(answer.getEnd());
    as.setCasProcessorId(PROCESSOR_ID);
    as.setConfidence(1D);
    as.setAnswer(answer);

    if (answer.getIsCorrect()) {
      as.setScore(1D);
    } else {
      as.setScore(0D);
    }

    as.addToIndexes();

  }

}
