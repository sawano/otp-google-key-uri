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

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static se.sawano.java.security.otp.google.keyuri.parameters.TestObjectFactory.secretFromBase32;

public class HOTPParametersTests {

    @Test
    public void should_fail_creation_if_counter_is_missing() throws Exception {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parametersWithoutCounter()
                .create());

        assertThat(exception.getMessage(), is("'Counter' is required for type HOTP"));
    }

    @Test
    public void should_create_for_HOTP() throws Exception {

        assertNotNull(parametersForHotp().create());
    }

    @Test
    public void should_create_URI_encoded_string() throws Exception {
        final HOTPParameters parameters = parametersForHotp().create();

        assertEquals("?algorithm=SHA1&counter=123&digits=6&issuer=Example%20Co&secret=ENJDVNXVNESP7N2VIOHSQG5RVID77N7P", parameters.asUriString());
    }

    private static ParametersBuilder.HotpParametersBuilder parametersWithoutCounter() {
        return completeBuilder()
                .withCounter(null);
    }

    private static ParametersBuilder.HotpParametersBuilder parametersWithCounter() {
        return completeBuilder();
    }

    private static ParametersBuilder.HotpParametersBuilder parametersForHotp() {
        return parametersWithCounter();
    }

    private static ParametersBuilder.HotpParametersBuilder completeBuilder() {
        return ParametersBuilder.hotpBuilder()
                                .withSecret(secret())
                                .withAlgorithm(algorithm())
                                .withIssuer(issuer())
                                .withCounter(counter())
                                .withDigits(Digits.SIX);
    }

    private static Secret secret() {
        return secretFromBase32("ENJDVNXVNESP7N2VIOHSQG5RVID77N7P");
    }

    private static Algorithm algorithm() {
        return Algorithm.SHA1;
    }

    private static Issuer issuer() {
        return Issuer.issuer("Example Co");
    }

    private static Counter counter() {
        return Counter.counter(123);
    }

}