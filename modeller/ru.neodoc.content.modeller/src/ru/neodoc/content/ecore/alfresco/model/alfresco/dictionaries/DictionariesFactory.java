/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage
 * @generated
 */
public interface DictionariesFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DictionariesFactory eINSTANCE = ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Namespace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Namespace</em>'.
	 * @generated
	 */
	Namespace createNamespace();

	/**
	 * Returns a new object of class '<em>Dictionary</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dictionary</em>'.
	 * @generated
	 */
	Dictionary createDictionary();

	/**
	 * Returns a new object of class '<em>Dictionaries</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dictionaries</em>'.
	 * @generated
	 */
	Dictionaries createDictionaries();

	/**
	 * Returns a new object of class '<em>Import</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Import</em>'.
	 * @generated
	 */
	Import createImport();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DictionariesPackage getDictionariesPackage();

} //DictionariesFactory
