

/* First created by JCasGen Tue Oct 01 16:02:46 EDT 2013 */
package org.cleartk.util.type;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.cleartk.score.type.ScoredAnnotation;


/** 
 * Updated by JCasGen Wed Oct 02 22:20:47 EDT 2013
 * XML source: /home/jeff/workspace/11791/hw3-jgee1/src/main/resources/hw3-jgee1-aae.xml
 * @generated */
public class Parenthetical extends ScoredAnnotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Parenthetical.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated  */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Parenthetical() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Parenthetical(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Parenthetical(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Parenthetical(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {/*default - does nothing empty block */}
     
}

    