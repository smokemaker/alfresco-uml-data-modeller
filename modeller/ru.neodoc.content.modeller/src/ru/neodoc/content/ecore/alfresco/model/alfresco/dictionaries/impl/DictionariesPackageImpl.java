/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.BaseNamespace;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesFactory;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Namespace;
import ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DictionariesPackageImpl extends EPackageImpl implements DictionariesPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namespaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dictionaryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dictionariesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass baseNamespaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass importEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DictionariesPackageImpl() {
		super(eNS_URI, DictionariesFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link DictionariesPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DictionariesPackage init() {
		if (isInited) return (DictionariesPackage)EPackage.Registry.INSTANCE.getEPackage(DictionariesPackage.eNS_URI);

		// Obtain or create and register package
		DictionariesPackageImpl theDictionariesPackage = (DictionariesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DictionariesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DictionariesPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		AlfrescoPackageImpl theAlfrescoPackage = (AlfrescoPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AlfrescoPackage.eNS_URI) instanceof AlfrescoPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AlfrescoPackage.eNS_URI) : AlfrescoPackage.eINSTANCE);

		// Create package meta-data objects
		theDictionariesPackage.createPackageContents();
		theAlfrescoPackage.createPackageContents();

		// Initialize created meta-data
		theDictionariesPackage.initializePackageContents();
		theAlfrescoPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDictionariesPackage.freeze();

  
		// Update the profileRegistry and return the package
		EPackage.Registry.INSTANCE.put(DictionariesPackage.eNS_URI, theDictionariesPackage);
		return theDictionariesPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNamespace() {
		return namespaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDictionary() {
		return dictionaryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDictionary_Location() {
		return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDictionary_IsReadOnly() {
		return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDictionary_Namespaces() {
		return (EReference)dictionaryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDictionary_Name() {
		return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDictionary_Imports() {
		return (EReference)dictionaryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDictionaries() {
		return dictionariesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDictionaries_Dictionaries() {
		return (EReference)dictionariesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBaseNamespace() {
		return baseNamespaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBaseNamespace_Alias() {
		return (EAttribute)baseNamespaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBaseNamespace_Url() {
		return (EAttribute)baseNamespaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImport() {
		return importEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImport_IsUndefined() {
		return (EAttribute)importEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImport_IsIgnored() {
		return (EAttribute)importEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DictionariesFactory getDictionariesFactory() {
		return (DictionariesFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		namespaceEClass = createEClass(NAMESPACE);

		dictionaryEClass = createEClass(DICTIONARY);
		createEAttribute(dictionaryEClass, DICTIONARY__LOCATION);
		createEAttribute(dictionaryEClass, DICTIONARY__IS_READ_ONLY);
		createEReference(dictionaryEClass, DICTIONARY__NAMESPACES);
		createEAttribute(dictionaryEClass, DICTIONARY__NAME);
		createEReference(dictionaryEClass, DICTIONARY__IMPORTS);

		dictionariesEClass = createEClass(DICTIONARIES);
		createEReference(dictionariesEClass, DICTIONARIES__DICTIONARIES);

		baseNamespaceEClass = createEClass(BASE_NAMESPACE);
		createEAttribute(baseNamespaceEClass, BASE_NAMESPACE__ALIAS);
		createEAttribute(baseNamespaceEClass, BASE_NAMESPACE__URL);

		importEClass = createEClass(IMPORT);
		createEAttribute(importEClass, IMPORT__IS_UNDEFINED);
		createEAttribute(importEClass, IMPORT__IS_IGNORED);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		namespaceEClass.getESuperTypes().add(this.getBaseNamespace());
		importEClass.getESuperTypes().add(this.getBaseNamespace());

		// Initialize classes and features; add operations and parameters
		initEClass(namespaceEClass, Namespace.class, "Namespace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dictionaryEClass, Dictionary.class, "Dictionary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDictionary_Location(), ecorePackage.getEString(), "location", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDictionary_IsReadOnly(), ecorePackage.getEBoolean(), "isReadOnly", "false", 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDictionary_Namespaces(), this.getNamespace(), null, "namespaces", null, 1, -1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDictionary_Name(), ecorePackage.getEString(), "Name", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDictionary_Imports(), this.getImport(), null, "imports", null, 0, -1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dictionariesEClass, Dictionaries.class, "Dictionaries", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDictionaries_Dictionaries(), this.getDictionary(), null, "dictionaries", null, 0, -1, Dictionaries.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(baseNamespaceEClass, BaseNamespace.class, "BaseNamespace", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBaseNamespace_Alias(), ecorePackage.getEString(), "alias", null, 0, 1, BaseNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBaseNamespace_Url(), ecorePackage.getEString(), "url", null, 0, 1, BaseNamespace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(importEClass, Import.class, "Import", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getImport_IsUndefined(), ecorePackage.getEBoolean(), "isUndefined", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getImport_IsIgnored(), ecorePackage.getEBoolean(), "isIgnored", "false", 1, 1, Import.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";	
		addAnnotation
		  (getImport_IsUndefined(), 
		   source, 
		   new String[] {
			 "name", "isUndefined",
			 "namespace", ""
		   });
	}

} //DictionariesPackageImpl
