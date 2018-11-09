/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsUndefined <em>Is Undefined</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsIgnored <em>Is Ignored</em>}</li>
 * </ul>
 *
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getImport()
 * @model
 * @generated
 */
public interface Import extends BaseNamespace {
	/**
	 * Returns the value of the '<em><b>Is Undefined</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Undefined</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Undefined</em>' attribute.
	 * @see #setIsUndefined(boolean)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getImport_IsUndefined()
	 * @model default="false" required="true"
	 *        extendedMetaData="name='isUndefined' namespace=''"
	 * @generated
	 */
	boolean isIsUndefined();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsUndefined <em>Is Undefined</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Undefined</em>' attribute.
	 * @see #isIsUndefined()
	 * @generated
	 */
	void setIsUndefined(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Ignored</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Ignored</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Ignored</em>' attribute.
	 * @see #setIsIgnored(boolean)
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#getImport_IsIgnored()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isIsIgnored();

	/**
	 * Sets the value of the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsIgnored <em>Is Ignored</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Ignored</em>' attribute.
	 * @see #isIsIgnored()
	 * @generated
	 */
	void setIsIgnored(boolean value);

} // Import
