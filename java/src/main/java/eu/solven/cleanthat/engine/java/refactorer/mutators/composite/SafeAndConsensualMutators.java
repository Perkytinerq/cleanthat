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
package eu.solven.cleanthat.engine.java.refactorer.mutators.composite;

import java.util.List;
import java.util.Optional;

import org.codehaus.plexus.languages.java.version.JavaVersion;

import com.google.common.collect.ImmutableList;

import eu.solven.cleanthat.engine.java.refactorer.JavaRefactorerProperties;
import eu.solven.cleanthat.engine.java.refactorer.meta.IConstructorNeedsJdkVersion;
import eu.solven.cleanthat.engine.java.refactorer.meta.IMutator;
import eu.solven.cleanthat.engine.java.refactorer.mutators.LocalVariableTypeInference;
import eu.solven.cleanthat.engine.java.refactorer.mutators.ModifierOrder;
import eu.solven.cleanthat.engine.java.refactorer.mutators.OptionalNotEmpty;
import eu.solven.cleanthat.engine.java.refactorer.mutators.PrimitiveBoxedForString;
import eu.solven.cleanthat.engine.java.refactorer.mutators.StreamAnyMatch;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UnnecessaryFullyQualifiedName;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UnnecessaryModifier;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UseIndexOfChar;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UseIsEmptyOnCollections;

/**
 * This mutator will apply all {@link IMutator} considered safe (e.g. by not impacting the {@link Runtime}, or only with
 * ultra-safe changes). It is also restricted to changes considered as consensual.
 * 
 * Example of not consensual mutator: {@link LocalVariableTypeInference} as some people prefer manipulating an object
 * through its interface.
 * 
 * @author Benoit Lacelle
 *
 */
public class SafeAndConsensualMutators extends CompositeMutator implements IConstructorNeedsJdkVersion {
	// BEWARE: Could we add `EmptyControlStatement`?
	public static final List<IMutator> SAFE_AND_CONSENSUAL = ImmutableList.<IMutator>builder()
			.add(new ModifierOrder(),
					new UseIndexOfChar(),
					new UseIsEmptyOnCollections(),
					new OptionalNotEmpty(),
					new StreamAnyMatch(),
					new PrimitiveBoxedForString(),
					new UnnecessaryModifier(),
					new UnnecessaryFullyQualifiedName())
			.build();

	public SafeAndConsensualMutators(JavaVersion sourceJdkVersion) {
		super(filterWithJdk(sourceJdkVersion, SAFE_AND_CONSENSUAL));
	}

	@Override
	public Optional<String> getCleanthatId() {
		return Optional.of(JavaRefactorerProperties.SAFE_AND_CONSENSUAL);
	}
}
