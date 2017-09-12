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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.sawano.java.security.otp.google.keyuri.parameters.Issuer.issuer;
import static se.sawano.java.security.otp.google.keyuri.parameters.TestObjectFactory.secretFromBase32;

public class GenericParametersTest {

    @Test
    public void should_fail_on_duplicate_parameter_type() throws Exception {

        final IllegalStateException exception = assertThrows(IllegalStateException.class, this::tryCreateWIthDuplicateParameterType);

        assertThat(exception.getMessage(), startsWith("Duplicate key"));
    }

    private void tryCreateWIthDuplicateParameterType() throws Exception {
        final Constructor<?> constructor = GenericParameters.class.getDeclaredConstructor(Secret.class, List.class);
        constructor.setAccessible(true);
        try {
            constructor.newInstance(secret(), asList(Algorithm.SHA1, issuer("Example Co"), period(), period()));
        } catch (final InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }

    private Secret secret() {
        return secretFromBase32("ENJDVNXVNESP7N2VIOHSQG5RVID77N7P");
    }

    private Period period() {
        return Period.period(Duration.ofSeconds(30));
    }

}