/*
 * Copyright 2017 Daniel Sawano
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.sawano.java.security.otp.google.keyuri;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class AccountNameCreationTests {

    public static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(value(null), isNotOk()),
                Arguments.of(value(""), isNotOk()),
                Arguments.of(value("  "), isNotOk()),
                Arguments.of(value("john.doe@example.com"), isOk()),
                Arguments.of(value("johnd"), isOk()),
                Arguments.of(value(" johnd "), isOk()),
                Arguments.of(value("john:doe"), isNotOk()),
                Arguments.of(value("john#€%&/()=?=)(/&%€#!"), isOk()),
                Arguments.of(value(StringUtils.repeat("a", Label.AccountName.MAX_LENGTH - 1)), isOk()),
                Arguments.of(value(StringUtils.repeat("a", Label.AccountName.MAX_LENGTH)), isOk()),
                Arguments.of(value(StringUtils.repeat("a", Label.AccountName.MAX_LENGTH + 1)), isNotOk())
        );
    }

    private static boolean isOk() {
        return true;
    }

    private static boolean isNotOk() {
        return false;
    }

    private static String value(final String value) {
        return value;
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_try_to_create_an_account_name(final String value, final boolean isOk) throws Exception {
        tryCreate(value).ifPresent(message -> {
            assertFalse(isOk);
        });
    }

    private static Optional<String> tryCreate(final String value) {
        try {
            Label.AccountName.accountName(value);
            return Optional.empty();
        } catch (final IllegalArgumentException | NullPointerException e) {
            return Optional.of(e.getMessage());
        }
    }
}
