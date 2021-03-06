/*
 * Copyright 2014-2018 Lukas Krejci
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.revapi.basic;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

import java.util.Set;

import org.revapi.AnalysisContext;
import org.revapi.ApiAnalyzer;
import org.revapi.DifferenceTransform;
import org.revapi.ElementFilter;
import org.revapi.PipelineConfiguration;
import org.revapi.Reporter;
import org.revapi.Revapi;

/**
 * @author Lukas Krejci
 * @since 0.6.0
 */
final class Util {

    private Util() {

    }

    static AnalysisContext getAnalysisContextFromFullConfig(Class<?> extensionType, String fullConfig) {
        AnalysisContext fullCtx = AnalysisContext.builder().withConfigurationFromJSON(fullConfig).build();
        return dummyRevapi(extensionType).prepareAnalysis(fullCtx).getFirstConfigurationOrNull(extensionType);
    }

    static AnalysisContext setAnalysisContextFullConfig(AnalysisContext.Builder bld, Class<?> extensionType, String fullConfig) {
        AnalysisContext fullCtx =bld.withConfigurationFromJSON(fullConfig).build();
        return dummyRevapi(extensionType).prepareAnalysis(fullCtx).getFirstConfigurationOrNull(extensionType);
    }

    private static Revapi dummyRevapi(Class<?> extensionType) {
        Set<Class<? extends ApiAnalyzer>> analyzers = setOrEmpty(ApiAnalyzer.class, extensionType);
        Set<Class<? extends ElementFilter>> filters = setOrEmpty(ElementFilter.class, extensionType);
        @SuppressWarnings("unchecked") Set<Class<? extends DifferenceTransform<?>>> transforms
                = setOrEmpty((Class) DifferenceTransform.class, extensionType);
        Set<Class<? extends Reporter>> reporters = setOrEmpty(Reporter.class, extensionType);

        return new Revapi(PipelineConfiguration.builder().withAnalyzers(analyzers).withReporters(reporters)
                .withTransforms(transforms).withFilters(filters).build());
    }

    @SuppressWarnings("unchecked")
    private static <T> Set<Class<? extends T>> setOrEmpty(Class<T> expectedType, Class<?> actualType) {
        if (expectedType.isAssignableFrom(actualType)) {
            return singleton((Class<? extends T>) actualType);
        } else {
            return emptySet();
        }
    }
}
