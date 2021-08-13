/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.nycode.github.request

import de.nycode.github.GitHubClient
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom

public suspend inline fun <reified T> GitHubClient.request(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    httpClient.request {
        RequestBuilder().apply(builder).requests.forEach { it() }
        url.takeFrom(URLBuilder(baseUrl).path(*path))
    }

public suspend inline fun <reified T> GitHubClient.get(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Get
        }
    }

public suspend inline fun <reified T> GitHubClient.post(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Post
        }
    }

public suspend inline fun <reified T> GitHubClient.put(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Put
        }
    }

public suspend inline fun <reified T> GitHubClient.patch(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Patch
        }
    }

public suspend inline fun <reified T> GitHubClient.delete(
    vararg path: String,
    builder: RequestBuilder.() -> Unit = {}
): T =
    request(*path) {
        builder()
        request {
            method = HttpMethod.Delete
        }
    }

/**
 * Implements parameters for [pagination](https://docs.github.com/en/rest/overview/resources-in-the-rest-api#pagination) in the GitHub API.
 */
public suspend inline fun <reified T> GitHubClient.paginatedRequest(
    vararg path: String,
    builder: PaginatedRequestBuilder.() -> Unit
): T =
    request(*path) {
        val (page, perPage, builders) = PaginatedRequestBuilder().apply(builder)
        require(page == null || perPage in 1..100) { "perPage must be between 1 and 100" }
        require(page == null || page > 0) { "page mustn't be negative" }
        request {
            parameter("per_page", perPage)
            parameter("page", page)
            builders.forEach { it() }
        }
    }

/**
 * Performs a paginated GET Request.
 */
public suspend inline fun <reified T> GitHubClient.paginatedGet(
    vararg path: String,
    builder: PaginatedRequestBuilder.() -> Unit
): T =
    paginatedRequest(*path) {
        builder()
        request {
            method = HttpMethod.Get
        }
    }