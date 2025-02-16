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
package eu.solven.cleanthat.engine.java.refactorer.mutators;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

import eu.solven.cleanthat.engine.java.IJdkVersionConstants;
import eu.solven.cleanthat.engine.java.refactorer.AJavaParserMutator;
import eu.solven.cleanthat.engine.java.refactorer.meta.IMutator;
import eu.solven.pepper.logging.PepperLogHelper;

/**
 * Turns '.stream(s -> {return s.subString(0, 2)})' into '.stream(s -> s.subString(0, 2))'
 *
 * @author Benoit Lacelle
 */
public class LambdaReturnsSingleStatement extends AJavaParserMutator implements IMutator {
	private static final Logger LOGGER = LoggerFactory.getLogger(LambdaReturnsSingleStatement.class);

	@Override
	public boolean isDraft() {
		// TODO CaseConflictingMethods
		return false;
	}

	@Override
	public String minimalJavaVersion() {
		return IJdkVersionConstants.JDK_8;
	}

	@Override
	public Optional<String> getCleanthatId() {
		return Optional.of("LambdaReturnsSingleStatement");
	}

	@Override
	public String pmdUrl() {
		return "https://pmd.github.io/latest/pmd_rules_java_codestyle.html#unnecessaryfullyqualifiedname";
	}

	@Override
	public Optional<String> getSonarId() {
		return Optional.of("S1602");
	}

	@SuppressWarnings({ "PMD.CognitiveComplexity", "PMD.NPathComplexity" })
	@Override
	protected boolean processNotRecursively(Node node) {
		LOGGER.debug("{}", PepperLogHelper.getObjectAndClass(node));

		if (!(node instanceof LambdaExpr)) {
			return false;
		}

		LambdaExpr lambdaExpr = (LambdaExpr) node;

		Statement body = lambdaExpr.getBody();

		if (!(body instanceof BlockStmt)) {
			return false;
		}

		BlockStmt lambdaBLockStmt = (BlockStmt) body;

		if (lambdaBLockStmt.getStatements().size() == 1) {
			if (lambdaBLockStmt.getStatement(0) instanceof ReturnStmt) {
				ReturnStmt returnStmt = (ReturnStmt) lambdaBLockStmt.getStatement(0);

				Optional<Expression> returnedExpr = returnStmt.getExpression();

				if (returnedExpr.isEmpty()) {
					return false;
				}

				lambdaExpr.setBody(new ExpressionStmt(returnedExpr.get()));

				return true;
			} else if (lambdaBLockStmt.getStatement(0) instanceof ExpressionStmt) {
				ExpressionStmt exprStmt = (ExpressionStmt) lambdaBLockStmt.getStatement(0);

				lambdaExpr.setBody(new ExpressionStmt(exprStmt.getExpression()));

				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
