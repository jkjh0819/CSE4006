package cse4006;

/**
 * Person
 * 2017.09.14
 *
 * This class represent a person. It includes person's name and a function that returns person's name.
 */
public class Person {

  private String name;

  /**
   * Person Constructor
   *
   * if there's no parameter, it causes system exit.
   */
  public Person(){
    System.out.println("Person should have a name");
    System.exit(0);
  }

  /**
   * Person Constructor
   *
   * initiates person's name.
   */
  public Person(String name) {
    this.name = name;
  }

  /**
   * getName
   *
   * return person's name.
   */
  public String getName() {
    return name;
  }
}
