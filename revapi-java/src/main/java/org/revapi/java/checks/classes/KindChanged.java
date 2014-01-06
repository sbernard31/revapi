/*
 * Copyright 2014 Lukas Krejci
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
 * limitations under the License
 */

package org.revapi.java.checks.classes;

import java.util.Collections;
import java.util.List;

import javax.lang.model.element.TypeElement;

import org.revapi.MatchReport;
import org.revapi.MismatchSeverity;
import org.revapi.java.checks.AbstractJavaCheck;
import org.revapi.java.checks.Code;

/**
 * @author Lukas Krejci
 * @since 1.0
 */
public final class KindChanged extends AbstractJavaCheck {
    @Override
    protected void doVisitClass(TypeElement oldType, TypeElement newType) {
        if (oldType != null && newType != null) {
            pushActive(oldType, newType);
        }
    }

    @Override
    protected List<MatchReport.Problem> doEnd() {
        ActiveElements<TypeElement> types = popIfActive();
        if (types != null) {
            TypeElement o = types.oldElement;
            TypeElement n = types.newElement;

            if (o.getKind() != n.getKind()) {
                MatchReport.Problem p = createProblem(Code.CLASS_KIND_CHANGED, MismatchSeverity.ERROR,
                    MismatchSeverity.ERROR, kind(o), kind(n));

                return Collections.singletonList(p);
            }
        }

        return null;
    }

    private String kind(TypeElement e) {
        switch (e.getKind()) {
        case CLASS:
            return "class";
        case INTERFACE:
            return "interface";
        case ANNOTATION_TYPE:
            return "@interface";
        case ENUM:
            return "enum";
        default:
            return "unexpected (" + e.getKind() + ")";
        }
    }
}