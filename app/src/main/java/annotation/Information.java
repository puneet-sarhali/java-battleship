package annotation;

public @interface Information {
    String author() default "N/A";
    String date() default "N/A";
    int numberOfRevision() default 1;
    String lastModified() default "N/A";
    String lastModifiedBy() default "N/A";
    String[] reviewers();
}
