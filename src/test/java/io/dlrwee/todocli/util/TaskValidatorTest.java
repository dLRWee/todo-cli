package io.dlrwee.todocli.util;

import io.dlrwee.todocli.common.task.TaskDescriptionValidationTests;
import io.dlrwee.todocli.common.task.TaskTitleValidationTests;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Nested;

class TaskValidatorTest {

    @Nested
    class ValidateTitle implements TaskTitleValidationTests {

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithTitle(String title) {
            return () -> TaskValidator.validateTitle(title);
        }
    }

    @Nested
    class ValidateDescription implements TaskDescriptionValidationTests {

        @Override
        public ThrowableAssert.ThrowingCallable provideActionWithDescription(String description) {
            return () -> TaskValidator.validateDescription(description);
        }
    }
}