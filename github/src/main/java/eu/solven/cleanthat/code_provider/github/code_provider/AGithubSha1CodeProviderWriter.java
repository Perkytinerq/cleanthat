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
package eu.solven.cleanthat.code_provider.github.code_provider;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;

import eu.solven.cleanthat.code_provider.github.refs.GithubRefWriterLogic;
import eu.solven.cleanthat.codeprovider.ICodeProvider;
import eu.solven.cleanthat.codeprovider.ICodeProviderWriter;

/**
 * An {@link ICodeProvider} for Github pull-requests
 *
 * @author Benoit Lacelle
 */
public abstract class AGithubSha1CodeProviderWriter extends AGithubSha1CodeProvider
		implements ICodeProviderWriter, IGithubSha1CodeProvider {
	final String eventKey;

	public AGithubSha1CodeProviderWriter(Path repositoryRoot, String token, String eventKey, GHRepository repo) {
		super(repositoryRoot, token, repo);

		this.eventKey = eventKey;
	}

	protected abstract GHRef getAsGHRef();

	@Override
	public void persistChanges(Map<Path, String> pathToMutatedContent,
			List<String> prComments,
			Collection<String> prLabels) {
		GithubRefWriterLogic githubRefWriterLogic = new GithubRefWriterLogic(eventKey, repo, getAsGHRef(), getSha1());
		githubRefWriterLogic.persistChanges(pathToMutatedContent, prComments, prLabels);
	}

	@Override
	public void cleanTmpFiles() {
		helper.cleanTmpFiles();
	}

}
