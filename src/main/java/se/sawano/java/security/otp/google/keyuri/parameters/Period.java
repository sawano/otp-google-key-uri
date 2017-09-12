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

import java.time.Duration;

/**
 * OPTIONAL only if type is totp: The period parameter defines a period that a TOTP code will be valid for, in seconds.
 * The default value is 30.
 * <p>
 * See https://github.com/google/google-authenticator/wiki/Key-Uri-Format#period
 */
public final class Period implements Parameter {

    public static Period period(final Duration value) {
        return new Period(value);
    }

    private final Duration value;

    private Period(final Duration value) {
        this.value = value;
    }

    public long value() {
        return value.getSeconds();
    }

    @Override
    public String parameterPair() {
        return "period=" + value();
    }
}
