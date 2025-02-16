/*
 * Copyright 2023 Benoit Lacelle - SOLVEN
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
package eu.solven.cleanthat.engine.java.refactorer.meta;

import java.util.Optional;

/**
 * Helps understand why a rule is relevant, given other systems implementing the rule
 *
 * @author Benoit Lacelle
 */
public interface IRuleExternalReferences {

	/**
	 * 
	 * @return an id crafted by CleanThat. Useful when no 3rd-party linter has already a rule ID
	 */
	default Optional<String> getCleanthatId() {
		return Optional.empty();
	}

	default Optional<String> getPmdId() {
		return Optional.empty();
	}

	default String pmdUrl() {
		return "";
	}

	default Optional<String> getSonarId() {
		return Optional.empty();
	}

	default String sonarUrl() {
		return getSonarId().map(id -> "https://rules.sonarsource.com/java/" + id).orElse("");
	}

	default Optional<String> getCheckstyleId() {
		return Optional.empty();
	}

	default String checkstyleUrl() {
		return "";
	}

	default String jsparrowUrl() {
		return "";
	}
}
