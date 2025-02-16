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
package eu.solven.cleanthat.formatter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.solven.cleanthat.config.pojo.CleanthatStepProperties;
import eu.solven.cleanthat.engine.EngineAndLinters;
import eu.solven.cleanthat.engine.IEngineLintFixerFactory;
import eu.solven.cleanthat.language.IEngineProperties;

/**
 * Helps compiling CodeProcessors in the context of a repository
 * 
 * @author Benoit Lacelle
 *
 */
public class SourceCodeFormatterHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(SourceCodeFormatterHelper.class);

	public EngineAndLinters compile(IEngineProperties engineProperties,
			CleanthatSession cleanthatSession,
			IEngineLintFixerFactory lintFixerFactory) {
		List<ILintFixer> linters = prepareLintFixers(engineProperties, cleanthatSession, lintFixerFactory);

		String engine = engineProperties.getEngine();
		LOGGER.info("engine={} has prepared {} lintFixers", engine, linters.size());
		linters.forEach(lf -> LOGGER.info("engine={} relies on {}", engine, lf.getClass().getName()));

		return new EngineAndLinters(engineProperties, linters);
	}

	/**
	 * 
	 * @param engineProperties
	 * @param cleanthatSession
	 *            necessary if some configuration is in the code itself
	 * @param lintFixerFactory
	 * @return
	 */
	public List<ILintFixer> prepareLintFixers(IEngineProperties engineProperties,
			CleanthatSession cleanthatSession,
			IEngineLintFixerFactory lintFixerFactory) {

		return engineProperties.getSteps()
				.stream()
				.filter(Predicate.not(CleanthatStepProperties::isSkip))
				.map(step -> lintFixerFactory.makeLintFixer(cleanthatSession, engineProperties, step))
				.collect(Collectors.toList());
	}
}
