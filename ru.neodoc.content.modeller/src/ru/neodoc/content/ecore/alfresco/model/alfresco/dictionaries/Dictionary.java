/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dictionary</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation <em>Location</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#isIsReadOnly <em>Is Read Only</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getNamespaces <em>Namespaces</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getName <em>Name</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getImports <em>Imports</em>}</li>
 * </ul>
 *
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary()
 * @model
 * @generated
 */
public interface Dictionary extends EObject {
	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #isSetLocation()
	 * @see #unsetLocation()
	 * @see #setLocation(String)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary_Location()
	 * @model unique="false" unsettable="true"
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #isSetLocation()
	 * @see #unsetLocation()
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

	/**
	 * Unsets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetLocation()
	 * @see #getLocation()
	 * @see #setLocation(String)
	 * @generated
	 */
	void unsetLocation();

	/**
	 * Returns whether the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation <em>Location</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Location</em>' attribute is set.
	 * @see #unsetLocation()
	 * @see #getLocation()
	 * @see #setLocation(String)
	 * @generated
	 */
	boolean isSetLocation();

	/**
	 * Returns the value of the '<em><b>Is Read Only</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Read Only</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Read Only</em>' attribute.
	 * @see #setIsReadOnly(boolean)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary_IsReadOnly()
	 * @model default="false"
	 * @generated
	 */
	boolean isIsReadOnly();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#isIsReadOnly <em>Is Read Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Read Only</em>' attribute.
	 * @see #isIsReadOnly()
	 * @generated
	 */
	void setIsReadOnly(boolean value);

	/**
	 * Returns the value of the '<em><b>Namespaces</b></em>' containment reference list.
	 * The list contents are of type {@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Namespaces</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Namespaces</em>' containment reference list.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary_Namespaces()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<Namespace> getNamespaces();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Imports</b></em>' containment reference list.
	 * The list contents are of type {@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Imports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imports</em>' containment reference list.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getDictionary_Imports()
	 * @model containment="true"
	 * @generated
	 */
	EList<Import> getImports();

} // Dictionary
