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
package io.gravitee.gateway.core.definition;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
public class ApiDefinition {

    private String name;

    @JsonProperty("proxy")
    private ProxyDefinition proxy;

    @JsonProperty("paths")
    private Map<String, Object> paths;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProxyDefinition getProxy() {
        return proxy;
    }

    public void setProxy(ProxyDefinition proxy) {
        this.proxy = proxy;
    }

    public Map<String, Object> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Object> paths) {
        this.paths = paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiDefinition api = (ApiDefinition) o;
        return Objects.equals(name, api.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}