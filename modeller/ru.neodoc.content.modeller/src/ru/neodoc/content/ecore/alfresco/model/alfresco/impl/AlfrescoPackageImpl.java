/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoFactory;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AlfrescoPackageImpl extends EPackageImpl implements AlfrescoPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass alfrescoEClass = null;

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
	 * @see ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AlfrescoPackageImpl() {
		super(eNS_URI, AlfrescoFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link AlfrescoPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AlfrescoPackage init() {
		if (isInited) return (AlfrescoPackage)EPackage.Registry.INSTANCE.getEPackage(AlfrescoPackage.eNS_URI);

		// Obtain or create and register package
		AlfrescoPackageImpl theAlfrescoPackage = (AlfrescoPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AlfrescoPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AlfrescoPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		DictionariesPackageImpl theDictionariesPackage = (DictionariesPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DictionariesPackage.eNS_URI) instanceof DictionariesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DictionariesPackage.eNS_URI) : DictionariesPackage.eINSTANCE);

		// Create package meta-data objects
		theAlfrescoPackage.createPackageContents();
		theDictionariesPackage.createPackageContents();

		// Initialize created meta-data
		theAlfrescoPackage.initializePackageContents();
		theDictionariesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAlfrescoPackage.freeze();

  
		// Update the profileRegistry and return the package
		EPackage.Registry.INSTANCE.put(AlfrescoPackage.eNS_URI, theAlfrescoPackage);
		return theAlfrescoPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAlfresco() {
		return alfrescoEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAlfresco_Dictionaries() {
		return (EReference)alfrescoEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAlfresco_DefaultCss() {
		return (EAttribute)alfrescoEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlfrescoFactory getAlfrescoFactory() {
		return (AlfrescoFactory)getEFactoryInstance();
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
		alfrescoEClass = createEClass(ALFRESCO);
		createEReference(alfrescoEClass, ALFRESCO__DICTIONARIES);
		createEAttribute(alfrescoEClass, ALFRESCO__DEFAULT_CSS);
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

		// Obtain other dependent packages
		DictionariesPackage theDictionariesPackage = (DictionariesPackage)EPackage.Registry.INSTANCE.getEPackage(DictionariesPackage.eNS_URI);

		// Add subpackages
		getESubpackages().add(theDictionariesPackage);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(alfrescoEClass, Alfresco.class, "Alfresco", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAlfresco_Dictionaries(), theDictionariesPackage.getDictionaries(), null, "dictionaries", null, 1, 1, Alfresco.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAlfresco_DefaultCss(), ecorePackage.getEString(), "defaultCss", null, 0, 1, Alfresco.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //AlfrescoPackageImpl
