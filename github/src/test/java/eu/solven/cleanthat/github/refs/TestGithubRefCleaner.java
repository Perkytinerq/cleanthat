package eu.solven.cleanthat.github.refs;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.mockito.Mockito;

import eu.solven.cleanthat.code_provider.github.event.GithubAndToken;
import eu.solven.cleanthat.code_provider.github.refs.GithubRefCleaner;
import eu.solven.cleanthat.code_provider.github.refs.all_files.GithubRefCodeProvider;
import eu.solven.cleanthat.codeprovider.ICodeProvider;
import eu.solven.cleanthat.codeprovider.git.GitRepoBranchSha1;
import eu.solven.cleanthat.config.ConfigHelpers;
import eu.solven.cleanthat.formatter.ICodeProviderFormatter;

public class TestGithubRefCleaner {
	@Test
	public void testGithubRefCleaner() throws IOException {
		GitHub gitHub = Mockito.mock(GitHub.class);

		GHRepository repository = Mockito.mock(GHRepository.class);
		Mockito.when(gitHub.getRepository("someUser/someRepoName")).thenReturn(repository);

		GHRef ref = Mockito.mock(GHRef.class);
		Mockito.when(repository.getRef("heads/someRef")).thenReturn(ref);

		GithubRefCleaner cleaner = new GithubRefCleaner(Arrays.asList(ConfigHelpers.makeJsonObjectMapper()),
				Arrays.asList(),
				Mockito.any(ICodeProviderFormatter.class),
				new GithubAndToken(gitHub, "someToken", Map.of()));

		ICodeProvider codeProvider = cleaner.getCodeProviderForRef(
				new GitRepoBranchSha1("someUser/someRepoName", "refs/heads/someRef", "someSha1"));

		Assertions.assertThat(codeProvider).isInstanceOf(GithubRefCodeProvider.class);
	}
}
