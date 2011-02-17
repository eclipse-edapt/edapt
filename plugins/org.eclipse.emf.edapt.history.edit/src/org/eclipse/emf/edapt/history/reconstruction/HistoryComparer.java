/*******************************************************************************
 * Copyright (c) 2007, 2010 BMW Car IT, Technische Universitaet Muenchen, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BMW Car IT - Initial API and implementation
 *     Technische Universitaet Muenchen - Major refactoring and extension
 *******************************************************************************/
package org.eclipse.emf.edapt.history.reconstruction;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.compare.diff.metamodel.ComparisonResourceSetSnapshot;
import org.eclipse.emf.compare.diff.metamodel.DiffFactory;
import org.eclipse.emf.compare.diff.metamodel.DiffResourceSet;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.Match2Elements;
import org.eclipse.emf.compare.match.metamodel.MatchFactory;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.eclipse.emf.compare.match.metamodel.MatchResourceSet;
import org.eclipse.emf.compare.match.metamodel.Side;
import org.eclipse.emf.compare.match.metamodel.UnmatchElement;
import org.eclipse.emf.compare.match.metamodel.UnmatchModel;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edapt.history.reconstruction.CompositeMapping;
import org.eclipse.emf.edapt.history.reconstruction.EcoreForwardReconstructor;

/**
 * Helper class to compare two metamodel versions from the history
 * 
 * @author herrmama
 * @author $Author$
 * @version $Rev$
 * @levd.rating RED Rev:
 */
public class HistoryComparer {

	/**
	 * Source metamodel version within the history
	 */
	private final EcoreForwardReconstructor sourceReconstructor;

	/**
	 * Target metamodel version within the history
	 */
	private final EcoreForwardReconstructor targetReconstructor;

	/**
	 * Mapping between the two metamodel versions
	 */
	private final CompositeMapping mapping;

	/**
	 * Map of elements of the source metamodel version to matches
	 */
	private final Map<EObject, Match2Elements> matches;

	/**
	 * Constructor
	 */
	public HistoryComparer(EcoreForwardReconstructor source,
			EcoreForwardReconstructor target) {
		this.sourceReconstructor = source;
		this.targetReconstructor = target;
		this.mapping = new CompositeMapping(source.getMapping(), target
				.getMapping());
		matches = new IdentityHashMap<EObject, Match2Elements>();
	}

	/** Compare the two metamodel versions. */
	public ComparisonResourceSetSnapshot compare() {
		MatchResourceSet matchResourceSet = createMatchResourceSet();
		DiffResourceSet diffResourceSet = DiffService.doDiff(matchResourceSet);

		ComparisonResourceSetSnapshot snapshot = DiffFactory.eINSTANCE
				.createComparisonResourceSetSnapshot();
		snapshot.setMatchResourceSet(matchResourceSet);
		DiffModelFilterUtils.filter(diffResourceSet,
				DiffModelOrderFilter.INSTANCE);
		snapshot.setDiffResourceSet(diffResourceSet);

		return snapshot;
	}

	/** Create a matching between the two metamodel versions. */
	private MatchResourceSet createMatchResourceSet() {
		matches.clear();
		MatchResourceSet matchResourceSet = MatchFactory.eINSTANCE
				.createMatchResourceSet();

		createSourceMatches(matchResourceSet);
		createTargetUnmatches(matchResourceSet);

		return matchResourceSet;
	}

	/** Create matches between source and target resources. */
	private void createSourceMatches(MatchResourceSet matchResourceSet) {
		for (Resource sourceResource : sourceReconstructor.getResourceSet()
				.getResources()) {
			List<EObject> sourceMatches = sourceResource.getContents();
			Resource targetResource = null;
			for (EObject source : sourceMatches) {
				EObject target = mapping.getTarget(source);
				if (target != null && target.eResource() != null) {
					targetResource = target.eResource();
				}
			}
			if (targetResource != null) {
				List<EObject> targetMatches = targetResource.getContents();
				MatchModel matchModel = createMatchModel(sourceMatches,
						targetMatches);
				matchResourceSet.getMatchModels().add(matchModel);
			} else {
				UnmatchModel unmatchModel = MatchFactory.eINSTANCE
						.createUnmatchModel();
				unmatchModel.getRoots().addAll(sourceMatches);
				unmatchModel.setSide(Side.LEFT);
				matchResourceSet.getUnmatchedModels().add(unmatchModel);
			}
		}
	}

	/**
	 * Search for target resources which do not have a corresponding source and
	 * add them as {@link UnmatchModel}s.
	 */
	private void createTargetUnmatches(MatchResourceSet matchResourceSet) {
		for (Resource targetResource : targetReconstructor.getResourceSet()
				.getResources()) {
			List<EObject> targetMatches = targetResource.getContents();
			Resource sourceResource = null;
			for (EObject target : targetMatches) {
				EObject source = mapping.getSource(target);
				if (source != null && source.eResource() != null) {
					sourceResource = source.eResource();
				}
			}
			if (sourceResource == null) {
				UnmatchModel unmatchModel = MatchFactory.eINSTANCE
						.createUnmatchModel();
				unmatchModel.getRoots().addAll(targetMatches);
				unmatchModel.setSide(Side.RIGHT);
				matchResourceSet.getUnmatchedModels().add(unmatchModel);
			}
		}
	}

	/** Create the matching between metamodel resources of different versions. */
	private MatchModel createMatchModel(List<EObject> sourceMatches,
			List<EObject> targetMatches) {
		MatchModel matchModel = MatchFactory.eINSTANCE.createMatchModel();

		matchModel.getRightRoots().addAll(sourceMatches);
		matchModel.getLeftRoots().addAll(targetMatches);

		// alternation between identifying matches and unmatches
		while (!sourceMatches.isEmpty() || !targetMatches.isEmpty()) {
			List<EObject> sourceUnmatches = createSourceMatches(sourceMatches,
					matchModel);
			List<EObject> targetUnmatches = createTargetMatches(targetMatches,
					matchModel);

			sourceMatches = createSourceUnmatches(sourceUnmatches, matchModel);
			targetMatches = createTargetUnmatches(targetUnmatches, matchModel);
		}

		return matchModel;
	}

	/**
	 * Create matches for elements of the source metamodel version. Collect and
	 * return all unmatches that are identified on the way.
	 */
	private List<EObject> createSourceMatches(List<EObject> sourceMatches,
			MatchModel matchModel) {
		List<EObject> sourceUnmatches = new ArrayList<EObject>();

		for (EObject source : sourceMatches) {
			EObject target = getTarget(source);

			if (exists(target)) {
				EObject parent = source.eContainer();
				Match2Elements match = null;
				if (parent != null) {
					Match2Elements parentMatch = matches.get(parent);
					if (parentMatch == null) {
						parent = getSource(target.eContainer());
						parentMatch = matches.get(parent);
					}
					match = createOrGetMatch(source, target, parentMatch);
				} else {
					match = createOrGetMatch(source, target, matchModel);
				}
				createSourceMatches(source, match, sourceUnmatches);
			} else {
				sourceUnmatches.add(source);
			}
		}

		return sourceUnmatches;
	}

	/**
	 * Recursively create matches for elements of the source metamodel version.
	 */
	private void createSourceMatches(EObject parent,
			Match2Elements parentMatch, List<EObject> sourceUnmatches) {
		for (EObject source : parent.eContents()) {
			EObject target = getTarget(source);

			if (exists(target)) {
				Match2Elements match1 = newMatch(source, target);
				parentMatch.getSubMatchElements().add(match1);
				Match2Elements match = match1;
				createSourceMatches(source, match, sourceUnmatches);
			} else {
				sourceUnmatches.add(source);
			}
		}
	}

	/**
	 * Create matches for elements of the target metamodel version. Collect and
	 * return all unmatches that are identified on the way.
	 */
	private List<EObject> createTargetMatches(List<EObject> targetMatches,
			MatchModel matchModel) {
		List<EObject> targetUnmatches = new ArrayList<EObject>();

		for (EObject target : targetMatches) {
			EObject source = getSource(target);

			if (exists(source)) {
				EObject parent = getSource(target.eContainer());
				if (parent == null) {
					parent = source.eContainer();
				}
				Match2Elements match = null;
				if (parent != null) {
					Match2Elements parentMatch = matches.get(parent);
					match = createOrGetMatch(source, target, parentMatch);
				} else {
					match = createOrGetMatch(source, target, matchModel);
				}
				createTargetMatches(target, match, targetUnmatches);
			} else {
				targetUnmatches.add(target);
			}
		}

		return targetUnmatches;
	}

	/**
	 * Recursively create matches for elements of the target metamodel version.
	 */
	private void createTargetMatches(EObject parent,
			Match2Elements parentMatch, List<EObject> targetUnmatches) {
		for (EObject target : parent.eContents()) {
			EObject source = getSource(target);

			if (exists(source)) {
				Match2Elements match = createOrGetMatch(source, target,
						parentMatch);
				createTargetMatches(target, match, targetUnmatches);
			} else {
				targetUnmatches.add(target);
			}
		}
	}

	/**
	 * Create unmatches for elements of the source metamodel version. Collect
	 * and return all matches that are identified on the way.
	 */
	private List<EObject> createSourceUnmatches(List<EObject> sourceUnmatches,
			MatchModel matchModel) {
		List<EObject> sourceMatches = new ArrayList<EObject>();

		for (EObject source : sourceUnmatches) {
			createUnmatch(source, Side.RIGHT, matchModel);
			createSourceUnmatches(source, matchModel, sourceMatches);
		}

		return sourceMatches;
	}

	/**
	 * Collect all matches that are hidden by unmatches in the source metamodel
	 * version.
	 */
	private void createSourceUnmatches(EObject parent, MatchModel matchModel,
			List<EObject> sourceMatches) {
		for (EObject source : parent.eContents()) {
			EObject target = getTarget(source);

			if (exists(target)) {
				sourceMatches.add(source);
			} else {
				createSourceUnmatches(source, matchModel, sourceMatches);
			}
		}
	}

	/**
	 * Create unmatches for elements of the target metamodel version. Collect
	 * and return all matches that are identified on the way.
	 */
	private List<EObject> createTargetUnmatches(List<EObject> targetUnmatches,
			MatchModel matchModel) {
		List<EObject> targetMatches = new ArrayList<EObject>();

		for (EObject target : targetUnmatches) {
			createUnmatch(target, Side.LEFT, matchModel);
			createTargetUnmatches(target, matchModel, targetMatches);
		}

		return targetMatches;
	}

	/**
	 * Collect all matches that are hidden by unmatches in the target metamodel
	 * version.
	 */
	private void createTargetUnmatches(EObject parent, MatchModel matchModel,
			List<EObject> targetMatches) {
		for (EObject target : parent.eContents()) {
			EObject source = getSource(target);

			if (exists(source)) {
				targetMatches.add(target);
			} else {
				createTargetUnmatches(target, matchModel, targetMatches);
			}
		}
	}

	/**
	 * Check whether an element exists in a metamodel version
	 */
	private boolean exists(EObject element) {
		return element != null && element.eResource() != null;
	}

	/**
	 * Get the element from the target metamodel version for an element from the
	 * source metamodel version.
	 */
	public EObject getTarget(EObject source) {
		if (source instanceof EGenericType) {
			return getGenericTarget((EGenericType) source);
		}
		return mapping.getTarget(source);
	}

	/**
	 * Infer a generic type in the target metmodel version for a generic type in
	 * the source metamodel version
	 */
	@SuppressWarnings("unchecked")
	private EGenericType getGenericTarget(EGenericType source) {
		EObject parent = source.eContainer();
		EObject targetParent = mapping.getTarget(parent);
		if (targetParent != null) {
			EReference reference = source.eContainmentFeature();
			if (reference.isMany()) {
				List<EGenericType> targets = (List<EGenericType>) targetParent
						.eGet(reference);
				for (EGenericType target : targets) {
					if (target.getEClassifier() == mapping.getTarget(source
							.getEClassifier())) {
						return target;
					}
				}
			} else {
				EGenericType target = (EGenericType) targetParent
						.eGet(reference);
				return target;
			}
		}
		return null;
	}

	/**
	 * Get the element from the source metamodel version for an element from the
	 * target metamodel version.
	 */
	public EObject getSource(EObject target) {
		if (target instanceof EGenericType) {
			return getGenericSource((EGenericType) target);
		}
		return mapping.getSource(target);
	}

	/**
	 * Infer a generic type in the source metamodel version for a generic type
	 * in the target metamodel version.
	 */
	@SuppressWarnings("unchecked")
	private EGenericType getGenericSource(EGenericType target) {
		EObject parent = target.eContainer();
		EObject sourceParent = mapping.getSource(parent);
		if (sourceParent != null) {
			EReference reference = target.eContainmentFeature();
			if (reference.isMany()) {
				List<EGenericType> sources = (List<EGenericType>) sourceParent
						.eGet(reference);
				for (EGenericType source : sources) {
					if (source.getEClassifier() == mapping.getSource(target
							.getEClassifier())) {
						return source;
					}
				}
			} else {
				EGenericType source = (EGenericType) sourceParent
						.eGet(reference);
				return source;
			}
		}
		return null;
	}

	/**
	 * Create or get a match between elements from the source and target
	 * metamodel version.
	 */
	private Match2Elements createOrGetMatch(EObject source, EObject target,
			MatchModel matchModel) {
		Match2Elements match = matches.get(source);
		if (match == null) {
			match = newMatch(source, target);
			matchModel.getMatchedElements().add(match);
		}
		return match;
	}

	/**
	 * Create or get a match between elements from the source and target
	 * metamodel version.
	 */
	private Match2Elements createOrGetMatch(EObject source, EObject target,
			Match2Elements parentMatch) {
		Match2Elements match = matches.get(source);
		if (match == null) {
			match = newMatch(source, target);
			parentMatch.getSubMatchElements().add(match);
		}
		return match;
	}

	/**
	 * Create a new match between elements from source and target metamodel
	 * version.
	 */
	private Match2Elements newMatch(EObject source, EObject target) {
		Match2Elements match = MatchFactory.eINSTANCE.createMatch2Elements();
		match.setRightElement(source);
		match.setLeftElement(target);
		match.setSimilarity(1.0);
		matches.put(source, match);
		return match;
	}

	/**
	 * Create an unmatch in the match model
	 */
	private UnmatchElement createUnmatch(EObject element, Side side,
			MatchModel matchModel) {
		UnmatchElement unmatch = MatchFactory.eINSTANCE.createUnmatchElement();
		unmatch.setElement(element);
		unmatch.setSide(side);
		// FIXME: Workaround for bug in EMF Compare
		if (side == Side.LEFT) {
			matchModel.getUnmatchedElements().add(0, unmatch);
		} else {
			matchModel.getUnmatchedElements().add(unmatch);
		}
		return unmatch;
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
}
