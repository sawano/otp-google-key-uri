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

/**
 * REQUIRED if type is hotp: The counter parameter is required when provisioning a key for use with HOTP. It will set
 * the initial counter value.
 * <p>
 * See https://github.com/google/google-authenticator/wiki/Key-Uri-Format#counter
 */
public final class Counter implements Parameter {

    public static Counter counter(final long value) {
        return new Counter(value);
    }

    private final long value;

    private Counter(final long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String parameterPair() {
        return "counter=" + Long.toString(value);
    }
}
