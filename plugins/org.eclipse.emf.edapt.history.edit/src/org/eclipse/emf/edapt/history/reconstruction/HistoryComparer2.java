package org.eclipse.emf.edapt.history.reconstruction;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.EMFCompare;
import org.eclipse.emf.compare.match.DefaultComparisonFactory;
import org.eclipse.emf.compare.match.DefaultEqualityHelperFactory;
import org.eclipse.emf.compare.match.DefaultMatchEngine;
import org.eclipse.emf.compare.match.IComparisonFactory;
import org.eclipse.emf.compare.match.IMatchEngine;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryImpl;
import org.eclipse.emf.compare.match.impl.MatchEngineFactoryRegistryImpl;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.compare.utils.UseIdentifiers;

public class HistoryComparer2 {
	/**
	 * Source metamodel version within the history
	 */
	private final EcoreForwardReconstructor sourceReconstructor;

	/**
	 * Target metamodel version within the history
	 */
	private final EcoreForwardReconstructor targetReconstructor;

	public HistoryComparer2(EcoreForwardReconstructor source,
		EcoreForwardReconstructor target) {

		sourceReconstructor = source;
		targetReconstructor = target;
	}

	/**
	 * Getter for the first state
	 */
	public EcoreForwardReconstructor getSourceReconstructor() {
		return sourceReconstructor;
	}

	/**
	 * Getter for the second state
	 */
	public EcoreForwardReconstructor getTargetReconstructor() {
		return targetReconstructor;
	}

	public Comparison compare() {

		// Configure EMF Compare
		final IEObjectMatcher matcher = DefaultMatchEngine
			.createDefaultEObjectMatcher(UseIdentifiers.NEVER);
		final IComparisonFactory comparisonFactory = new DefaultComparisonFactory(
			new DefaultEqualityHelperFactory());
		final IMatchEngine.Factory matchEngineFactory = new MatchEngineFactoryImpl(
			matcher, comparisonFactory);
		matchEngineFactory.setRanking(20);
		final IMatchEngine.Factory.Registry matchEngineRegistry = new MatchEngineFactoryRegistryImpl();
		matchEngineRegistry.add(matchEngineFactory);
		final EMFCompare comparator = EMFCompare.builder()
			.setMatchEngineFactoryRegistry(matchEngineRegistry).build();

		// Compare the two models
		final IComparisonScope scope = EMFCompare.createDefaultScope(
			sourceReconstructor.getResourceSet(),
			targetReconstructor.getResourceSet());
		return comparator.compare(scope);
	}
}
