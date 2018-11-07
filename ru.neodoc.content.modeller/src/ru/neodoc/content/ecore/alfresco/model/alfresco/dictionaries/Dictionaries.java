/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dictionaries</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries#getDictionaries <em>Dictionaries</em>}</li>
 * </ul>
 *
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionaries()
 * @model
 * @generated
 */
public interface Dictionaries extends EObject {
	/**
	 * Returns the value of the '<em><b>Dictionaries</b></em>' containment reference list.
	 * The list contents are of type {@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dictionaries</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dictionaries</em>' containment reference list.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionaries_Dictionaries()
	 * @model containment="true"
	 * @generated
	 */
	EList<Dictionary> getDictionaries();

} // Dictionaries
