package eu.solven.cleanthat.engine.java.refactorer.cases.do_not_format_me;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.solven.cleanthat.engine.java.refactorer.annotations.CompareMethods;
import eu.solven.cleanthat.engine.java.refactorer.meta.IMutator;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UseDiamondOperatorJdk8;
import eu.solven.cleanthat.engine.java.refactorer.test.ARefactorerCases;

//TODO Have a maven module per version of Java, to ensure the post is valid
public class UseDiamondOperatorJdk8Cases extends ARefactorerCases {

	@Override
	public IMutator getTransformer() {
		return new UseDiamondOperatorJdk8();
	}

	@CompareMethods
	public static class CaseCollection {
		private <T> List<T> genericMethod(List<T> list) {
			return list;
		}

		public Object pre() {
			return genericMethod(new ArrayList<Number>());
		}

		public Object post(Collection<?> input) {
			return genericMethod(new ArrayList<>());
		}
	}
}
