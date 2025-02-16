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
package eu.solven.cleanthat.spotless;

import java.util.Set;

import eu.solven.cleanthat.codeprovider.ICodeProvider;
import eu.solven.cleanthat.spotless.pojo.SpotlessFormatterProperties;

/**
 * Common behavior to any Spotless engine steps factory
 * 
 * @author Benoit Lacelle
 *
 */
// see com.diffplug.spotless.maven.FormatterFactory
public abstract class AFormatterFactory {

	public abstract Set<String> defaultIncludes();

	public abstract AFormatterStepFactory makeStepFactory(ICodeProvider codeProvider,
			SpotlessFormatterProperties formatterProperties);

}
