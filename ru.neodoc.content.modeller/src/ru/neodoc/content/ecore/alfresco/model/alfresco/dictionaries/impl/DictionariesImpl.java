/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.DictionariesPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionary;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dictionaries</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.impl.DictionariesImpl#getDictionaries <em>Dictionaries</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DictionariesImpl extends EObjectImpl implements Dictionaries {
	/**
	 * The cached value of the '{@link #getDictionaries() <em>Dictionaries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDictionaries()
	 * @generated
	 * @ordered
	 */
	protected EList<Dictionary> dictionaries;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DictionariesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionariesPackage.Literals.DICTIONARIES;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Dictionary> getDictionaries() {
		if (dictionaries == null) {
			dictionaries = new EObjectContainmentEList<Dictionary>(Dictionary.class, this, DictionariesPackage.DICTIONARIES__DICTIONARIES);
		}
		return dictionaries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionariesPackage.DICTIONARIES__DICTIONARIES:
				return ((InternalEList<?>)getDictionaries()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DictionariesPackage.DICTIONARIES__DICTIONARIES:
				return getDictionaries();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DictionariesPackage.DICTIONARIES__DICTIONARIES:
				getDictionaries().clear();
				getDictionaries().addAll((Collection<? extends Dictionary>)newValue);
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
			case DictionariesPackage.DICTIONARIES__DICTIONARIES:
				getDictionaries().clear();
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
			case DictionariesPackage.DICTIONARIES__DICTIONARIES:
				return dictionaries != null && !dictionaries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DictionariesImpl
