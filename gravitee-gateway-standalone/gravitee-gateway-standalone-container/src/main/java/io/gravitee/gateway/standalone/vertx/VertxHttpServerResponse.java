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
package io.gravitee.gateway.standalone.vertx;

import io.gravitee.common.http.HttpHeaders;
import io.gravitee.common.http.HttpHeadersValues;
import io.gravitee.gateway.api.Response;
import io.gravitee.gateway.api.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;

/**
 * @author David BRASSELY (brasseld at gmail.com)
 */
class VertxHttpServerResponse implements Response {

    private final HttpServerResponse httpServerResponse;

    private final HttpHeaders headers = new HttpHeaders();

    private boolean headersWritten = false;

    VertxHttpServerResponse(HttpServerResponse httpServerResponse) {
        this.httpServerResponse = httpServerResponse;
    }

    @Override
    public int status() {
        return httpServerResponse.getStatusCode();
    }

    @Override
    public Response status(int statusCode) {
        httpServerResponse.setStatusCode(statusCode);
        return this;
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public Response write(Buffer chunk) {
        if (! headersWritten) {
            writeHeaders();

            // Vertx requires to set the chunked flag if transfer_encoding header as the "chunked" value
            String transferEncodingHeader = headers().getFirst(HttpHeaders.TRANSFER_ENCODING);
            if (HttpHeadersValues.TRANSFER_ENCODING_CHUNKED.equalsIgnoreCase(transferEncodingHeader)) {
                httpServerResponse.setChunked(true);
            } else if (transferEncodingHeader == null) {
                String connectionHeader = headers().getFirst(HttpHeaders.CONNECTION);
                String contentLengthHeader = headers().getFirst(HttpHeaders.CONTENT_LENGTH);
                if (HttpHeadersValues.CONNECTION_CLOSE.equalsIgnoreCase(connectionHeader)
                        && contentLengthHeader == null) {
                    httpServerResponse.setChunked(true);
                }
            }
        }

        httpServerResponse.write(io.vertx.core.buffer.Buffer.buffer(chunk.getBytes()));
        return this;
    }

    @Override
    public void end() {
        if (! headersWritten) {
            writeHeaders();
        }

        httpServerResponse.end();
    }

    private void writeHeaders() {
        headers.entrySet().forEach(
                headerEntry -> httpServerResponse.putHeader(headerEntry.getKey(), headerEntry.getValue()));
        headersWritten = true;
    }
}
