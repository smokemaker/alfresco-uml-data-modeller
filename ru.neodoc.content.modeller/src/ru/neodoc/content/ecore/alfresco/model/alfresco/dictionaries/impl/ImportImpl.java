/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Import;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl#isIsUndefined <em>Is Undefined</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.ImportImpl#isIsIgnored <em>Is Ignored</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ImportImpl extends BaseNamespaceImpl implements Import {
	/**
	 * The default value of the '{@link #isIsUndefined() <em>Is Undefined</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsUndefined()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_UNDEFINED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsUndefined() <em>Is Undefined</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsUndefined()
	 * @generated
	 * @ordered
	 */
	protected boolean isUndefined = IS_UNDEFINED_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsIgnored() <em>Is Ignored</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsIgnored()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_IGNORED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsIgnored() <em>Is Ignored</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsIgnored()
	 * @generated
	 * @ordered
	 */
	protected boolean isIgnored = IS_IGNORED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImportImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionariesPackage.Literals.IMPORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsUndefined() {
		return isUndefined;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsUndefined(boolean newIsUndefined) {
		boolean oldIsUndefined = isUndefined;
		isUndefined = newIsUndefined;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionariesPackage.IMPORT__IS_UNDEFINED, oldIsUndefined, isUndefined));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsIgnored() {
		return isIgnored;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsIgnored(boolean newIsIgnored) {
		boolean oldIsIgnored = isIgnored;
		isIgnored = newIsIgnored;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionariesPackage.IMPORT__IS_IGNORED, oldIsIgnored, isIgnored));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionariesPackage.IMPORT__IS_UNDEFINED:
				return isIsUndefined();
			case DictionariesPackage.IMPORT__IS_IGNORED:
				return isIsIgnored();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DictionariesPackage.IMPORT__IS_UNDEFINED:
				setIsUndefined((Boolean)newValue);
				return;
			case DictionariesPackage.IMPORT__IS_IGNORED:
				setIsIgnored((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DictionariesPackage.IMPORT__IS_UNDEFINED:
				setIsUndefined(IS_UNDEFINED_EDEFAULT);
				return;
			case DictionariesPackage.IMPORT__IS_IGNORED:
				setIsIgnored(IS_IGNORED_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DictionariesPackage.IMPORT__IS_UNDEFINED:
				return isUndefined != IS_UNDEFINED_EDEFAULT;
			case DictionariesPackage.IMPORT__IS_IGNORED:
				return isIgnored != IS_IGNORED_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (isUndefined: ");
		result.append(isUndefined);
		result.append(", isIgnored: ");
		result.append(isIgnored);
		result.append(')');
		return result.toString();
	}

} //ImportImpl
