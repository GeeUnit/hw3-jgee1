package edu.cmu.deiis.annotators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.cas.text.AnnotationIndex;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;

import org.apache.uima.jcas.tcas.Annotation;

/**
 * The TopN Evaluator evaluates Precision for the top N results of a QA system. N is defined by the
 * number of correct results.
 * 
 * @author jeff
 * 
 */
public class TopNEvaluator extends AbstractEvaluator {

  @Override
  public double evaluate(AnnotationIndex<Annotation> scores) {

    Iterator<Annotation> scoreIterator = scores.iterator();

    List<AnswerScore> ansScores = new ArrayList<AnswerScore>();

    int numberCorrect = 0;

    while (scoreIterator.hasNext()) {
      AnswerScore ansScore = (AnswerScore) scoreIterator.next();
      ansScores.add(ansScore);

      if (ansScore.getAnswer().getIsCorrect()) {
        numberCorrect++;
      }

    }
    Collections.sort(ansScores, new AnswerScoreComparator());
    int precCount = 0;

    for (int i = 0; i < ansScores.size(); i++) {
      AnswerScore score = ansScores.get(i);
      StringBuffer sb = new StringBuffer();

      Answer answer = score.getAnswer();

      sb.append(answer.getIsCorrect() ? "+ " : "- ");
      sb.append(formatScore(score.getScore()) + " ");
      sb.append(answer.getCoveredText());

      System.out.println(sb.toString());

      if (i < numberCorrect && answer.getIsCorrect()) {
        precCount++;
      }
    }

    double precision=precCount / (double) numberCorrect;
    
    System.out.println("Precision at " + (double) numberCorrect + ": "
            + formatScore(precision)+"\n");
    
    return precision;
  }
}
