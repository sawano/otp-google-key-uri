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

package se.sawano.java.security.otp.google.keyuri.parameters;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static se.sawano.java.security.otp.google.keyuri.parameters.Issuer.issuer;

public class IssuerCreationTests {

    public static Stream<Arguments> data() {
        return Stream.of(
                of(value("Example Co"), isOk()),
                of(value(":"), isOk()),
                of(value("My:Company"), isOk()),
                of(value(""), isNotOk()),
                of(value("   "), isNotOk()),
                of(value(StringUtils.repeat("a", Issuer.MAX_LENGTH - 1)), isOk()),
                of(value(StringUtils.repeat("a", Issuer.MAX_LENGTH)), isOk()),
                of(value(StringUtils.repeat("a", Issuer.MAX_LENGTH + 1)), isNotOk())
        );
    }

    private static String value(final String value) {
        return value;
    }

    private static boolean isNotOk() {
        return false;
    }

    private static boolean isOk() {
        return true;
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_test_creation(final String value, final boolean isValid) throws Exception {
        final Optional<String> errorMessage = tryCreate(value);

        assertEquals(isValid, !errorMessage.isPresent());
    }

    private Optional<String> tryCreate(final String value) {
        try {
            issuer(value);
            return Optional.empty();
        } catch (final IllegalArgumentException e) {
            return Optional.of(e.getMessage());
        }
    }
}