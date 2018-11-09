/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Namespace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getAlias <em>Alias</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getUrl <em>Url</em>}</li>
 * </ul>
 *
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getBaseNamespace()
 * @model abstract="true"
 * @generated
 */
public interface BaseNamespace extends EObject {
	/**
	 * Returns the value of the '<em><b>Alias</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alias</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alias</em>' attribute.
	 * @see #setAlias(String)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getBaseNamespace_Alias()
	 * @model
	 * @generated
	 */
	String getAlias();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getAlias <em>Alias</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alias</em>' attribute.
	 * @see #getAlias()
	 * @generated
	 */
	void setAlias(String value);

	/**
	 * Returns the value of the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Url</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getBaseNamespace_Url()
	 * @model
	 * @generated
	 */
	String getUrl();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getUrl <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
	void setUrl(String value);

} // BaseNamespace
