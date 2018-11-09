/**
 */
package ru.neodoc.content.ecore.alfresco.model.alfresco.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import ru.neodoc.content.ecore.alfresco.model.alfresco.Alfresco;
import ru.neodoc.content.ecore.alfresco.model.alfresco.AlfrescoPackage;
import ru.neodoc.content.ecore.alfresco.model.alfresco.dictionaries.Dictionaries;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Alfresco</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl#getDictionaries <em>Dictionaries</em>}</li>
 *   <li>{@link ru.neodoc.content.ecore.alfresco.model.alfresco.impl.AlfrescoImpl#getDefaultCss <em>Default Css</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AlfrescoImpl extends EObjectImpl implements Alfresco {
	/**
	 * The cached value of the '{@link #getDictionaries() <em>Dictionaries</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDictionaries()
	 * @generated
	 * @ordered
	 */
	protected Dictionaries dictionaries;

	/**
	 * The default value of the '{@link #getDefaultCss() <em>Default Css</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultCss()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_CSS_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getDefaultCss() <em>Default Css</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultCss()
	 * @generated
	 * @ordered
	 */
	protected String defaultCss = DEFAULT_CSS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AlfrescoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AlfrescoPackage.Literals.ALFRESCO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Dictionaries getDictionaries() {
		return dictionaries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDictionaries(Dictionaries newDictionaries, NotificationChain msgs) {
		Dictionaries oldDictionaries = dictionaries;
		dictionaries = newDictionaries;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlfrescoPackage.ALFRESCO__DICTIONARIES, oldDictionaries, newDictionaries);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDictionaries(Dictionaries newDictionaries) {
		if (newDictionaries != dictionaries) {
			NotificationChain msgs = null;
			if (dictionaries != null)
				msgs = ((InternalEObject)dictionaries).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlfrescoPackage.ALFRESCO__DICTIONARIES, null, msgs);
			if (newDictionaries != null)
				msgs = ((InternalEObject)newDictionaries).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlfrescoPackage.ALFRESCO__DICTIONARIES, null, msgs);
			msgs = basicSetDictionaries(newDictionaries, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlfrescoPackage.ALFRESCO__DICTIONARIES, newDictionaries, newDictionaries));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultCss() {
		return defaultCss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultCss(String newDefaultCss) {
		String oldDefaultCss = defaultCss;
		defaultCss = newDefaultCss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlfrescoPackage.ALFRESCO__DEFAULT_CSS, oldDefaultCss, defaultCss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AlfrescoPackage.ALFRESCO__DICTIONARIES:
				return basicSetDictionaries(null, msgs);
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
			case AlfrescoPackage.ALFRESCO__DICTIONARIES:
				return getDictionaries();
			case AlfrescoPackage.ALFRESCO__DEFAULT_CSS:
				return getDefaultCss();
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
			case AlfrescoPackage.ALFRESCO__DICTIONARIES:
				setDictionaries((Dictionaries)newValue);
				return;
			case AlfrescoPackage.ALFRESCO__DEFAULT_CSS:
				setDefaultCss((String)newValue);
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
			case AlfrescoPackage.ALFRESCO__DICTIONARIES:
				setDictionaries((Dictionaries)null);
				return;
			case AlfrescoPackage.ALFRESCO__DEFAULT_CSS:
				setDefaultCss(DEFAULT_CSS_EDEFAULT);
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
			case AlfrescoPackage.ALFRESCO__DICTIONARIES:
				return dictionaries != null;
			case AlfrescoPackage.ALFRESCO__DEFAULT_CSS:
				return DEFAULT_CSS_EDEFAULT == null ? defaultCss != null : !DEFAULT_CSS_EDEFAULT.equals(defaultCss);
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
		result.append(" (defaultCss: ");
		result.append(defaultCss);
		result.append(')');
		return result.toString();
	}

} //AlfrescoImpl
