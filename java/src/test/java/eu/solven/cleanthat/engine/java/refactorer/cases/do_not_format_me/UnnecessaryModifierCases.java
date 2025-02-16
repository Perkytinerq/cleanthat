package eu.solven.cleanthat.engine.java.refactorer.cases.do_not_format_me;

import eu.solven.cleanthat.engine.java.refactorer.annotations.CompareInnerAnnotations;
import eu.solven.cleanthat.engine.java.refactorer.annotations.CompareInnerClasses;
import eu.solven.cleanthat.engine.java.refactorer.meta.IMutator;
import eu.solven.cleanthat.engine.java.refactorer.mutators.UnnecessaryModifier;
import eu.solven.cleanthat.engine.java.refactorer.test.ARefactorerCases;

public class UnnecessaryModifierCases extends ARefactorerCases {
	@Override
	public IMutator getTransformer() {
		return new UnnecessaryModifier();
	}

	@CompareInnerClasses
	public static class SomeInterface {

		public interface Pre {
			// both abstract and public are ignored by the compiler
			public abstract void bar();

			// public, static, and final all ignored
			public static final int X = 0;

			// public, static ignored
			public static class Bar {
			}

			// ditto
			public static interface Baz {
			}
		}

		public interface Post {
			// both abstract and public are ignored by the compiler
			void bar();

			// public, static, and final all ignored
			int X = 0;

			// public, static ignored
			class Bar {
			}

			// ditto
			interface Baz {
			}
		}
	}

	@CompareInnerAnnotations
	public static class SomeAnnotation {

		public @interface Pre {
			// both abstract and public are ignored by the compiler
			public abstract String bar();

			// public, static, and final all ignored
			public static final int X = 0;

			// public, static ignored
			public static class Bar {
			}

			// ditto
			public static interface Baz {
			}
		}

		public @interface Post {
			// both abstract and public are ignored by the compiler
			String bar();

			// public, static, and final all ignored
			int X = 0;

			// public, static ignored
			class Bar {
			}

			// ditto
			interface Baz {
			}
		}
	}
}
