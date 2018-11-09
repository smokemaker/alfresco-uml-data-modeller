/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries;

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
 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesFactory
 * @model kind="package"
 * @generated
 */
public interface DictionariesPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dictionaries";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://neodoc.neologica.ru/tools/alfresco/modeller/dictionaries";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ndad";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DictionariesPackage eINSTANCE = ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl.init();

	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.BaseNamespaceImpl <em>Base Namespace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.BaseNamespaceImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getBaseNamespace()
	 * @generated
	 */
	int BASE_NAMESPACE = 3;

	/**
	 * The feature id for the '<em><b>Alias</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_NAMESPACE__ALIAS = 0;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_NAMESPACE__URL = 1;

	/**
	 * The number of structural features of the '<em>Base Namespace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BASE_NAMESPACE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.NamespaceImpl <em>Namespace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.NamespaceImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getNamespace()
	 * @generated
	 */
	int NAMESPACE = 0;

	/**
	 * The feature id for the '<em><b>Alias</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__ALIAS = BASE_NAMESPACE__ALIAS;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE__URL = BASE_NAMESPACE__URL;

	/**
	 * The number of structural features of the '<em>Namespace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMESPACE_FEATURE_COUNT = BASE_NAMESPACE_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionaryImpl <em>Dictionary</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionaryImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getDictionary()
	 * @generated
	 */
	int DICTIONARY = 1;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY__LOCATION = 0;

	/**
	 * The feature id for the '<em><b>Is Read Only</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY__IS_READ_ONLY = 1;

	/**
	 * The feature id for the '<em><b>Namespaces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY__NAMESPACES = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY__NAME = 3;

	/**
	 * The feature id for the '<em><b>Imports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY__IMPORTS = 4;

	/**
	 * The number of structural features of the '<em>Dictionary</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARY_FEATURE_COUNT = 5;


	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl <em>Dictionaries</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getDictionaries()
	 * @generated
	 */
	int DICTIONARIES = 2;

	/**
	 * The feature id for the '<em><b>Dictionaries</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARIES__DICTIONARIES = 0;

	/**
	 * The number of structural features of the '<em>Dictionaries</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DICTIONARIES_FEATURE_COUNT = 1;


	/**
	 * The meta object id for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl <em>Import</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getImport()
	 * @generated
	 */
	int IMPORT = 4;

	/**
	 * The feature id for the '<em><b>Alias</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT__ALIAS = BASE_NAMESPACE__ALIAS;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT__URL = BASE_NAMESPACE__URL;

	/**
	 * The feature id for the '<em><b>Is Undefined</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT__IS_UNDEFINED = BASE_NAMESPACE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Ignored</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT__IS_IGNORED = BASE_NAMESPACE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Import</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT_FEATURE_COUNT = BASE_NAMESPACE_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Namespace</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace
	 * @generated
	 */
	EClass getNamespace();

	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary <em>Dictionary</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dictionary</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary
	 * @generated
	 */
	EClass getDictionary();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getLocation()
	 * @see #getDictionary()
	 * @generated
	 */
	EAttribute getDictionary_Location();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#isIsReadOnly <em>Is Read Only</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Read Only</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#isIsReadOnly()
	 * @see #getDictionary()
	 * @generated
	 */
	EAttribute getDictionary_IsReadOnly();

	/**
	 * Returns the meta object for the containment reference list '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getNamespaces <em>Namespaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Namespaces</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getNamespaces()
	 * @see #getDictionary()
	 * @generated
	 */
	EReference getDictionary_Namespaces();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getName()
	 * @see #getDictionary()
	 * @generated
	 */
	EAttribute getDictionary_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Imports</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary#getImports()
	 * @see #getDictionary()
	 * @generated
	 */
	EReference getDictionary_Imports();

	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries <em>Dictionaries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dictionaries</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries
	 * @generated
	 */
	EClass getDictionaries();

	/**
	 * Returns the meta object for the containment reference list '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries#getDictionaries <em>Dictionaries</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dictionaries</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries#getDictionaries()
	 * @see #getDictionaries()
	 * @generated
	 */
	EReference getDictionaries_Dictionaries();

	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace <em>Base Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Base Namespace</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace
	 * @generated
	 */
	EClass getBaseNamespace();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getAlias <em>Alias</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Alias</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getAlias()
	 * @see #getBaseNamespace()
	 * @generated
	 */
	EAttribute getBaseNamespace_Alias();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace#getUrl()
	 * @see #getBaseNamespace()
	 * @generated
	 */
	EAttribute getBaseNamespace_Url();

	/**
	 * Returns the meta object for class '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Import</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import
	 * @generated
	 */
	EClass getImport();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsUndefined <em>Is Undefined</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Undefined</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsUndefined()
	 * @see #getImport()
	 * @generated
	 */
	EAttribute getImport_IsUndefined();

	/**
	 * Returns the meta object for the attribute '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsIgnored <em>Is Ignored</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Ignored</em>'.
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import#isIsIgnored()
	 * @see #getImport()
	 * @generated
	 */
	EAttribute getImport_IsIgnored();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DictionariesFactory getDictionariesFactory();

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
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.NamespaceImpl <em>Namespace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.NamespaceImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getNamespace()
		 * @generated
		 */
		EClass NAMESPACE = eINSTANCE.getNamespace();

		/**
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionaryImpl <em>Dictionary</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionaryImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getDictionary()
		 * @generated
		 */
		EClass DICTIONARY = eINSTANCE.getDictionary();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DICTIONARY__LOCATION = eINSTANCE.getDictionary_Location();

		/**
		 * The meta object literal for the '<em><b>Is Read Only</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DICTIONARY__IS_READ_ONLY = eINSTANCE.getDictionary_IsReadOnly();

		/**
		 * The meta object literal for the '<em><b>Namespaces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DICTIONARY__NAMESPACES = eINSTANCE.getDictionary_Namespaces();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DICTIONARY__NAME = eINSTANCE.getDictionary_Name();

		/**
		 * The meta object literal for the '<em><b>Imports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DICTIONARY__IMPORTS = eINSTANCE.getDictionary_Imports();

		/**
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl <em>Dictionaries</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getDictionaries()
		 * @generated
		 */
		EClass DICTIONARIES = eINSTANCE.getDictionaries();

		/**
		 * The meta object literal for the '<em><b>Dictionaries</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DICTIONARIES__DICTIONARIES = eINSTANCE.getDictionaries_Dictionaries();

		/**
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.BaseNamespaceImpl <em>Base Namespace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.BaseNamespaceImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getBaseNamespace()
		 * @generated
		 */
		EClass BASE_NAMESPACE = eINSTANCE.getBaseNamespace();

		/**
		 * The meta object literal for the '<em><b>Alias</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_NAMESPACE__ALIAS = eINSTANCE.getBaseNamespace_Alias();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BASE_NAMESPACE__URL = eINSTANCE.getBaseNamespace_Url();

		/**
		 * The meta object literal for the '{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl <em>Import</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl
		 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl#getImport()
		 * @generated
		 */
		EClass IMPORT = eINSTANCE.getImport();

		/**
		 * The meta object literal for the '<em><b>Is Undefined</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMPORT__IS_UNDEFINED = eINSTANCE.getImport_IsUndefined();

		/**
		 * The meta object literal for the '<em><b>Is Ignored</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMPORT__IS_IGNORED = eINSTANCE.getImport_IsIgnored();

	}

} //DictionariesPackage
