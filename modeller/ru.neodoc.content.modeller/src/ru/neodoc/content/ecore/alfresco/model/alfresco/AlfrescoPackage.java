/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoFactory
 * @model kind="package"
 * @generated
 */
public interface AlfrescoPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "alfresco";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://neodoc.neologica.ru/tools/alfresco/modeller";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ndalf";

	/**
	 * The package content type ID.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eCONTENT_TYPE = "ru.neodoc.modeller.alfresco";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AlfrescoPackage eINSTANCE = ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoPackageImpl.init();

	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl <em>Alfresco</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoPackageImpl#getAlfresco()
	 * @generated
	 */
	int ALFRESCO = 0;

	/**
	 * The feature id for the '<em><b>Dictionaries</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALFRESCO__DICTIONARIES = 0;

	/**
	 * The feature id for the '<em><b>Default Css</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALFRESCO__DEFAULT_CSS = 1;

	/**
	 * The number of structural features of the '<em>Alfresco</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALFRESCO_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco <em>Alfresco</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alfresco</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco
	 * @generated
	 */
	EClass getAlfresco();

	/**
	 * Returns the meta object for the containment reference '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDictionaries <em>Dictionaries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Dictionaries</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDictionaries()
	 * @see #getAlfresco()
	 * @generated
	 */
	EReference getAlfresco_Dictionaries();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDefaultCss <em>Default Css</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Css</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco#getDefaultCss()
	 * @see #getAlfresco()
	 * @generated
	 */
	EAttribute getAlfresco_DefaultCss();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AlfrescoFactory getAlfrescoFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl <em>Alfresco</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoPackageImpl#getAlfresco()
		 * @generated
		 */
		EClass ALFRESCO = eINSTANCE.getAlfresco();

		/**
		 * The meta object literal for the '<em><b>Dictionaries</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALFRESCO__DICTIONARIES = eINSTANCE.getAlfresco_Dictionaries();

		/**
		 * The meta object literal for the '<em><b>Default Css</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALFRESCO__DEFAULT_CSS = eINSTANCE.getAlfresco_DefaultCss();

	}

} //AlfrescoPackage
