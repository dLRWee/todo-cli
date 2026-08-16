package io.dlrwee.todocli.util;

public final class CsvResources {

    private CsvResources() {
        throw new AssertionError("CsvResources is a util class and cannot be instantiated");
    }

    private static final String TASK_ROOT = "/task";
    private static final String TASK_TITLE_ROOT = TASK_ROOT + "/title";
    private static final String TASK_DESCRIPTION_ROOT = TASK_ROOT + "/description";

    public static final String TASK_VALID_TITLES = TASK_TITLE_ROOT + "/valid-titles.csv";
    public static final String TASK_INVALID_TITLES = TASK_TITLE_ROOT + "/invalid-titles.csv";
    public static final String TASK_VALID_DESCRIPTIONS = TASK_DESCRIPTION_ROOT + "/valid-descriptions.csv";
    public static final String TASK_INVALID_DESCRIPTIONS = TASK_DESCRIPTION_ROOT + "/invalid-descriptions.csv";
}
