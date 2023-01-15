/*
 * Copyright 2021-2023 DiffPlug
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
package com.diffplug.spotless.glue.java;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.diffplug.spotless.FormatterFunc;

import eu.solven.cleanthat.config.pojo.LanguageProperties;
import eu.solven.cleanthat.formatter.LineEnding;
import eu.solven.cleanthat.language.java.refactorer.JavaRefactorer;
import eu.solven.cleanthat.language.java.refactorer.JavaRefactorerProperties;

public class JavaCleanthatRefactoringFunc implements FormatterFunc {
	private List<String> included;
	private List<String> excluded;

	public JavaCleanthatRefactoringFunc(List<String> included, List<String> excluded) {
		this.included = included == null ? Collections.emptyList() : included;
		this.excluded = excluded == null ? Collections.emptyList() : excluded;
	}

	public JavaCleanthatRefactoringFunc() {
		this(Arrays.asList(JavaRefactorerProperties.WILDCARD), Arrays.asList());
	}

	@Override
	public String apply(String input) throws Exception {
		JavaRefactorerProperties refactorerProperties = new JavaRefactorerProperties();

		refactorerProperties.setIncluded(included);
		refactorerProperties.setExcluded(excluded);

		JavaRefactorer refactorer = new JavaRefactorer(new LanguageProperties(), refactorerProperties);

		// Spotless calls steps always with LF eol.
		return refactorer.doFormat(input, LineEnding.LF);
	}

}
