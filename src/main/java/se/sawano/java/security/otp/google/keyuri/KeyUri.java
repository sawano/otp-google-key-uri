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

import se.sawano.java.security.otp.google.keyuri.parameters.HOTPParameters;
import se.sawano.java.security.otp.google.keyuri.parameters.Issuer;
import se.sawano.java.security.otp.google.keyuri.parameters.TOTPParameters;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * otpauth://TYPE/LABEL?PARAMETERS <p> See https://github.com/google/google-authenticator/wiki/Key-Uri-Format. </p> <p> Note: The issuer parameter is
 * a string value indicating the provider or service this account is associated with, URL-encoded according to RFC 3986. If the issuer parameter is
 * absent, issuer information may be taken from the issuer prefix of the label. If both issuer parameter and issuer label prefix are present, they
 * should be equal. </p>
 */
public final class KeyUri {

    private static final String OTPAUTH_SCHEME = "otpauth://";

    private final Type type;
    private final Label label;
    private final Either<TOTPParameters, HOTPParameters> either;

    public KeyUri(final Label label, final TOTPParameters parameters) {
        notNull(label);
        notNull(parameters);

        this.type = Type.TOTP;
        this.label = label;
        either = Either.left(parameters);

        validateIssuer();
    }

    public KeyUri(final Label label, final HOTPParameters parameters) {
        notNull(label);
        notNull(parameters);

        this.type = Type.HOTP;
        this.label = label;
        either = Either.right(parameters);

        validateIssuer();
    }

    public URI toURI() {
        return URI.create(OTPAUTH_SCHEME + type.value() + "/" + label.asUriString() + parametersUriString());
    }

    public Optional<TOTPParameters> totpParameters() {
        return either.left();
    }

    public Optional<HOTPParameters> hotpParameters() {
        return either.right();
    }

    public Type type() {
        return type;
    }

    public Label label() {
        return label;
    }

    private void validateIssuer() {
        label.issuer()
             .map(labelIssuer -> (Consumer<Issuer>) parameterIssuer -> verifyEqual(labelIssuer, parameterIssuer))
             .ifPresent(issuerConsumer -> issuer().ifPresent(issuerConsumer));
    }

    private void verifyEqual(final Label.Issuer issuer, final Issuer parameterIssuer) {
        isTrue(issuer.value().equals(parameterIssuer.value()), "Issuer must be same in Label and parameters");
    }

    private Optional<Issuer> issuer() {
        return either.map(TOTPParameters::issuer, HOTPParameters::issuer);
    }

    private String parametersUriString() {
        return either.map(TOTPParameters::asUriString, HOTPParameters::asUriString);
    }

    private static abstract class Either<L, R> {

        public static <L, R> Either<L, R> left(final L value) {
            notNull(value);
            return new Either<L, R>() {

                @Override
                public <T> T map(final Function<L, T> leftFunction, final Function<R, T> rightFunction) {
                    return leftFunction.apply(value);
                }

                @Override
                public Optional<L> left() {
                    return Optional.of(value);
                }

                @Override
                public Optional<R> right() {
                    return Optional.empty();
                }
            };
        }

        public static <L, R> Either<L, R> right(final R value) {
            notNull(value);
            return new Either<L, R>() {

                @Override
                public <T> T map(final Function<L, T> leftFunction, final Function<R, T> rightFunction) {
                    return rightFunction.apply(value);
                }

                @Override
                public Optional<L> left() {
                    return Optional.empty();
                }

                @Override
                public Optional<R> right() {
                    return Optional.of(value);
                }
            };
        }

        public abstract <T> T map(final Function<L, T> leftFunction, final Function<R, T> rightFunction);

        public abstract Optional<L> left();

        public abstract Optional<R> right();
    }

}
