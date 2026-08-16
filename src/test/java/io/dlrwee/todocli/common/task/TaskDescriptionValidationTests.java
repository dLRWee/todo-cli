package io.dlrwee.todocli.common.task;

import io.dlrwee.todocli.exception.impl.IllegalDescriptionTodoException;
import io.dlrwee.todocli.util.CsvResources;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public interface TaskDescriptionValidationTests {

    @ParameterizedTest(name = "description: {0}")
    @CsvFileSource(resources = CsvResources.TASK_VALID_DESCRIPTIONS)
    @DisplayName("Valid descriptions should pass")
    default void validDescriptionsShouldPass(String description) {
        assertThatNoException().isThrownBy(provideActionWithDescription(description));
    }

    @ParameterizedTest(name = "description: {0}")
    @CsvFileSource(resources = CsvResources.TASK_INVALID_DESCRIPTIONS)
    @DisplayName("Invalid descriptions should not pass")
    default void invalidDescriptionsShouldNotPass(String description) {
        assertThatThrownBy(provideActionWithDescription(description))
                .isInstanceOf(IllegalDescriptionTodoException.class);
    }

    ThrowableAssert.ThrowingCallable provideActionWithDescription(String description);
}
