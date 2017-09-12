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

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static se.sawano.java.security.otp.google.keyuri.parameters.Period.period;
import static se.sawano.java.security.otp.google.keyuri.parameters.TestObjectFactory.secretFromBase32;

public class TOTPParametersTests {

    @Test
    public void should_fail_creation_if_period_is_missing() throws Exception {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> parametersWithoutPeriod()
                .create());

        assertThat(exception.getMessage(), is("'Period' is required for type TOTP"));
    }

    @Test
    public void should_create_for_TOTP() throws Exception {

        assertNotNull(parametersForTotp().create());
    }

    @Test
    public void should_create_URI_encoded_string() throws Exception {
        final TOTPParameters parameters = parametersForTotp().create();

        assertEquals("?algorithm=SHA1&digits=6&issuer=Example%20Co&period=30&secret=ENJDVNXVNESP7N2VIOHSQG5RVID77N7P", parameters.asUriString());
    }

    private ParametersBuilder.TotpParametersBuilder parametersWithoutPeriod() {
        return completeBuilder()
                .withPeriod(null);
    }

    private ParametersBuilder.TotpParametersBuilder parametersForTotp() {
        return completeBuilder();
    }

    private ParametersBuilder.TotpParametersBuilder completeBuilder() {
        return ParametersBuilder.totpBuilder()
                                .withSecret(secret())
                                .withAlgorithm(algorithm())
                                .withIssuer(issuer())
                                .withDigits(Digits.SIX)
                                .withPeriod(period(ofSeconds(30)));
    }

    private Secret secret() {
        return secretFromBase32("ENJDVNXVNESP7N2VIOHSQG5RVID77N7P");
    }

    private Algorithm algorithm() {
        return Algorithm.SHA1;
    }

    private Issuer issuer() {
        return Issuer.issuer("Example Co");
    }

}