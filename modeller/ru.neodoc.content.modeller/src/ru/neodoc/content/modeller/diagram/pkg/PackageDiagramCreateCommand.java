/*****************************************************************************
 * Copyright (c) 2011 CEA LIST.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		
 *		CEA LIST - Initial API and implementation
 *
 *****************************************************************************/
package ru.neodoc.content.modeller.diagram.pkg;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.infra.core.log.LogHelper;
import org.eclipse.papyrus.infra.gmfdiag.common.AbstractPapyrusGmfCreateDiagramCommandHandler;
import org.eclipse.papyrus.infra.gmfdiag.common.helper.DiagramPrototype;

public class PackageDiagramCreateCommand extends AbstractPapyrusGmfCreateDiagramCommandHandler {

	/** The plug-in ID */
	public static final String ORIGIN_PLUGIN_ID = "org.eclipse.papyrus.uml.diagram.package";

	/** The logging helper */
	public static LogHelper log;

	/** The plug-in Preference store */
	public static final PreferencesHint DIAGRAM_PREFERENCES_HINT = new PreferencesHint(ORIGIN_PLUGIN_ID);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDefaultDiagramName() {
		return "PkgDiagram"; // //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getDiagramNotationID() {
		return "Package";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PreferencesHint getPreferenceHint() {
		return DIAGRAM_PREFERENCES_HINT;
	}
	
	@Override
	protected Diagram doCreateDiagram(Resource diagramResource, 
			EObject owner, EObject element, DiagramPrototype prototype,
			String name) {
		
		if(owner instanceof org.eclipse.uml2.uml.Package) 
			return super.doCreateDiagram(diagramResource, owner, element, prototype, name);
		
		return null;
		
	}
	
}
