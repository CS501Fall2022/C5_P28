package com.example.autocorrect;

public class Correct {
  static int id;
  static String misspelled;
  static String correct;

  public Correct( int newId, String newMisspelled, String newCorrect ) {
//    setId( newId );
//    setMisspelled(newMisspelled);
//    setCorrect(newCorrect);

    this.id = newId;
    this.misspelled = newMisspelled;
    this.correct = newCorrect;
  }

  public void setId( int newId ) {
    id = newId;
  }

  public void setMisspelled( String newMisspelled ) {
    misspelled = newMisspelled;
  }
  public void setCorrect( String newCorrect ) { correct = newCorrect; }

  public int getId( ) {
    return id;
  }

  public static String getMisspelled( ) {
    return misspelled;
  }

  public static String getCorrect( ) {
    return correct;
  }

  public String toString() {
    return id + " " + misspelled + " " + correct;
  }
}