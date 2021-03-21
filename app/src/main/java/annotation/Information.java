package annotation;

/** Describes class info; needs to be placed above the class
 * An example would be
 * @Information(
 *         author = "Richard Jiang",
 *         date = "3/18/2021",
 *         numberOfRevision = 3,
 *         lastModified = "2/21/2021",
 *         lastModifiedBy = "Puneet",
 *         reviewers = {"Richard", "Puneet", "Shahmat", "Jason"}
 * )
 * class sampleClass{}
 * */
public @interface Information {
    String author() default "N/A";
    String date() default "N/A";
    int numberOfRevision() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    String[] reviewers();
}
