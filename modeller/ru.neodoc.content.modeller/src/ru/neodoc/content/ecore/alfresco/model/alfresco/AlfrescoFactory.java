/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage
 * @generated
 */
public interface AlfrescoFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AlfrescoFactory eINSTANCE = ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Alfresco</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Alfresco</em>'.
	 * @generated
	 */
	Alfresco createAlfresco();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AlfrescoPackage getAlfrescoPackage();

} //AlfrescoFactory
