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
package eu.solven.cleanthat.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.solven.cleanthat.code_provider.CleanthatPathHelpers;
import eu.solven.cleanthat.codeprovider.ICodeProvider;
import eu.solven.cleanthat.config.RepoInitializerResult.RepoInitializerResultBuilder;
import eu.solven.cleanthat.engine.IEngineLintFixerFactory;
import eu.solven.pepper.resource.PepperResourceHelper;

/**
 * This will help configuration CleanThat by proposing a reasonnable default configuration.
 * 
 * @author Benoit Lacelle
 *
 */
public class CleanthatConfigInitializer {
	public static final String TEMPLATES_FOLDER = "/templates";

	final ICodeProvider codeProvider;
	final ObjectMapper objectMapper;
	final Collection<IEngineLintFixerFactory> factories;

	public CleanthatConfigInitializer(ICodeProvider codeProvider,
			ObjectMapper objectMapper,
			Collection<IEngineLintFixerFactory> factories) {
		this.codeProvider = codeProvider;
		this.objectMapper = objectMapper;
		this.factories = factories;
	}

	public RepoInitializerResult prepareFile(boolean isPrivate) {
		String defaultRepoPropertiesPath = ICleanthatConfigConstants.DEFAULT_PATH_CLEANTHAT;

		// Let's follow Renovate and its configuration PR
		// https://github.com/solven-eu/agilea/pull/1
		String body = PepperResourceHelper.loadAsString(TEMPLATES_FOLDER + "/onboarding-body.md");
		// body = body.replaceAll(Pattern.quote("${REPO_FULL_NAME}"), repo.getFullName());
		body = body.replaceAll(Pattern.quote("${DEFAULT_PATH}"), defaultRepoPropertiesPath);

		if (!isPrivate) {
			body += "\r\n" + "---" + "\r\n" + "@blacelle please look at me";
		}

		String commitMessage = PepperResourceHelper.loadAsString(TEMPLATES_FOLDER + "/commit-message.txt");
		RepoInitializerResultBuilder resultBuilder =
				RepoInitializerResult.builder().prBody(body).commitMessage(commitMessage);

		GenerateInitialConfig generateInitialConfig = new GenerateInitialConfig(factories);
		try {
			EngineInitializerResult engineConfig = generateInitialConfig.prepareDefaultConfiguration(codeProvider);

			// Write the main config files (cleanthat.yaml)
			String repoPropertiesYaml = objectMapper.writeValueAsString(engineConfig.getRepoProperties());
			Path repositoryRoot = codeProvider.getRepositoryRoot();
			resultBuilder.pathToContent(CleanthatPathHelpers.makeContentPath(repositoryRoot, defaultRepoPropertiesPath),
					repoPropertiesYaml);

			// Register the custom files of the engine
			engineConfig.getPathToContents()
					.forEach((k, v) -> resultBuilder
							.pathToContent(CleanthatPathHelpers.makeContentPath(repositoryRoot, k), v));
		} catch (IOException e) {
			throw new UncheckedIOException("Issue preparing initial config given codeProvider=" + codeProvider, e);
		}

		return resultBuilder.build();
	}

}
