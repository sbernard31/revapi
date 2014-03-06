/*
 * Copyright $year Lukas Krejci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.revapi.java.transforms.methods;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;

import org.revapi.Configuration;
import org.revapi.Element;
import org.revapi.MatchReport;
import org.revapi.ProblemTransform;
import org.revapi.java.JavaModelElement;
import org.revapi.java.checks.Code;

/**
 * @author Lukas Krejci
 * @since 0.1
 */
public final class AnnotationTypeAttributeAdded implements ProblemTransform {
    private Locale locale;

    @Override
    public void initialize(@Nonnull Configuration configuration) {
        locale = configuration.getLocale();
    }

    @Nullable
    @Override
    public MatchReport.Problem transform(@Nullable Element oldElement, @Nullable Element newElement,
        @Nonnull MatchReport.Problem problem) {

        if (Code.METHOD_ABSTRACT_METHOD_ADDED != Code.fromCode(problem.code)) {
            return problem;
        }

        @SuppressWarnings("ConstantConditions")
        ExecutableElement method = (ExecutableElement) ((JavaModelElement) newElement).getModelElement();

        if (method.getEnclosingElement().getKind() == ElementKind.ANNOTATION_TYPE) {
            AnnotationValue defaultValue = method.getDefaultValue();

            if (defaultValue == null) {
                return Code.METHOD_ATTRIBUTE_WITH_NO_DEFAULT_ADDED_TO_ANNOTATION_TYPE.createProblem(locale);
            } else {
                return Code.METHOD_ATTRIBUTE_WITH_DEFAULT_ADDED_TO_ANNOTATION_TYPE.createProblem(locale);
            }
        }

        return problem;
    }

    @Override
    public void close() {
    }
}