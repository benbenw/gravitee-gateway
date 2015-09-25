/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.gateway.core.definition.validator;

import io.gravitee.gateway.core.definition.ApiDefinition;
import io.gravitee.gateway.core.definition.ProxyDefinition;
import org.apache.commons.validator.routines.UrlValidator;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class ProxyValidator implements Validator {

    private static final String CONTEXT_PATH_PATTERN = "^\\\\/([a-zA-Z0-9_-]+\\\\/?+)++";

    private static final UrlValidator urlValidator = new UrlValidator(new String []{"http","https"}, UrlValidator.ALLOW_LOCAL_URLS);

    @Override
    public void validate(ApiDefinition definition) throws ValidationException {
        ProxyDefinition proxyDefinition = definition.getProxy();

        if (proxyDefinition == null) {
            throw new ValidationException("An API must have a proxy part");
        }

        if (proxyDefinition.getTarget() == null) {
            throw new ValidationException("An API must have a valid target");
        }

        if (! urlValidator.isValid(proxyDefinition.getTarget().toString())) {
            throw new ValidationException("An API must have a valid target");
        }

        if (proxyDefinition.getContextPath() == null || proxyDefinition.getContextPath().matches(CONTEXT_PATH_PATTERN)) {
            throw new ValidationException("An API must have a valid context path");
        }

        if (! proxyDefinition.getContextPath().startsWith("/")) {
            throw new ValidationException("An API must have a valid context-path starting with '/'");
        }
    }
}