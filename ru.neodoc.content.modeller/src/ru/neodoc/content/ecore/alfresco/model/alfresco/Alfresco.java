/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco;

import org.eclipse.emf.ecore.EObject;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Alfresco</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDictionaries <em>Dictionaries</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDefaultCss <em>Default Css</em>}</li>
 * </ul>
 *
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage#getAlfresco()
 * @model
 * @generated
 */
public interface Alfresco extends EObject {
	
	String getDefaultCss();
	
	void setDefaultCss(String value);
	
	/**
	 * Returns the value of the '<em><b>Dictionaries</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dictionaries</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dictionaries</em>' containment reference.
	 * @see #setDictionaries(Dictionaries)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage#getAlfresco_Dictionaries()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Dictionaries getDictionaries();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDictionaries <em>Dictionaries</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dictionaries</em>' containment reference.
	 * @see #getDictionaries()
	 * @generated
	 */
	void setDictionaries(Dictionaries value);

} // Alfresco
