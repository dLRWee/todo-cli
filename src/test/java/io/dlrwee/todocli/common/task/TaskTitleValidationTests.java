package io.dlrwee.todocli.common.task;

import io.dlrwee.todocli.exception.impl.IllegalTitleTodoException;
import io.dlrwee.todocli.util.CsvResources;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.assertj.core.api.Assertions.*;

public interface TaskTitleValidationTests {

    @ParameterizedTest(name = "title: {0}")
    @CsvFileSource(resources = CsvResources.TASK_VALID_TITLES)
    @DisplayName("Valid titles should pass")
    default void validTitlesShouldPass(String title) {
        assertThatNoException().isThrownBy(provideActionWithTitle(title));
    }

    @ParameterizedTest(name = "title: {0}")
    @CsvFileSource(resources = CsvResources.TASK_INVALID_TITLES)
    @DisplayName("Invalid titles should not pass")
    default void invalidTitlesShouldNotPass(String title) {
        assertThatThrownBy(provideActionWithTitle(title))
                .isInstanceOf(IllegalTitleTodoException.class);
    }

    ThrowableAssert.ThrowingCallable provideActionWithTitle(String title);
}
