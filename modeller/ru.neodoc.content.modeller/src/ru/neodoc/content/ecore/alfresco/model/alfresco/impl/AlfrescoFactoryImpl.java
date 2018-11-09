/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoFactory;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AlfrescoFactoryImpl extends EFactoryImpl implements AlfrescoFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AlfrescoFactory init() {
		try {
			AlfrescoFactory theAlfrescoFactory = (AlfrescoFactory)EPackage.Registry.INSTANCE.getEFactory(AlfrescoPackage.eNS_URI);
			if (theAlfrescoFactory != null) {
				return theAlfrescoFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AlfrescoFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlfrescoFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AlfrescoPackage.ALFRESCO: return createAlfresco();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Alfresco createAlfresco() {
		AlfrescoImpl alfresco = new AlfrescoImpl();
		return alfresco;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlfrescoPackage getAlfrescoPackage() {
		return (AlfrescoPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AlfrescoPackage getPackage() {
		return AlfrescoPackage.eINSTANCE;
	}

} //AlfrescoFactoryImpl
