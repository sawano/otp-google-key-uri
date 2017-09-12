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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.of;
import static se.sawano.java.security.otp.google.keyuri.Label.AccountName.accountName;
import static se.sawano.java.security.otp.google.keyuri.Label.Issuer.issuer;

public class LabelValueTests {

    public static Stream<Arguments> data() {
        return Stream.of(
                of(givenAcountName("john.doe@example.com"), andIssuer("My Company"), thenValueShouldBe("john.doe%40example.com%3AMy%20Company")),
                of(givenAcountName("john.doe@example.com"), andNoIssuer(), thenValueShouldBe("john.doe%40example.com")),
                of(givenAcountName("John Doe"), andNoIssuer(), thenValueShouldBe("John%20Doe")),
                of(givenAcountName("John Doe"), andIssuer("My@Company"), thenValueShouldBe("John%20Doe%3AMy%40Company"))
        );
    }

    private static String givenAcountName(final String accountName) {
        return accountName;
    }

    private static String andIssuer(final String issuer) {
        return issuer;
    }

    private static String andNoIssuer() {
        return null;
    }

    private static String thenValueShouldBe(final String expectedValue) {
        return expectedValue;
    }

    @ParameterizedTest
    @MethodSource("data")
    public void should_create_correct_value(final String accountName, final String issuer, final String expectedValue) throws Exception {
        assertEquals(expectedValue, getValue(issuer, accountName));
    }

    private String getValue(final String issuer, final String accountName) {
        if (issuer == null) {
            return new Label(accountName(accountName)).asUriString();
        }
        return new Label(accountName(accountName), issuer(issuer)).asUriString();
    }

}
