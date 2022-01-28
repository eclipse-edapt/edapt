/*******************************************************************************
 * Copyright (c) 2011-2022 EclipseSource Muenchen GmbH and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * jfaltermeier - initial API and implementation
 * lkoehler - Override basicCreate to create non-dynamic EObjects
 ******************************************************************************/
package org.eclipse.emf.edapt.migration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * {@link org.eclipse.emf.ecore.EFactory EFactory} implementation for creating objects for the custom data types of the
 * {@link org.eclipse.emf.ecore.EcoreFactory EcoreFactory}.
 *
 * Since edapt uses dynamic EMF the {@link org.eclipse.emf.ecore.EcoreFactory EcoreFactory} is not used when loading a
 * model, we need a custom implementation. Moreover the switch should be based on the data type names, since the ids
 * might differ as well.
 *
 * @author jfaltermeier
 * @since 1.5
 *
 */
public class DynamicEcoreFactory extends EFactoryImpl {

	private static final String E_BIG_DECIMAL = "EBigDecimal";//$NON-NLS-1$
	private static final String E_BIG_INTEGER = "EBigInteger";//$NON-NLS-1$
	private static final String E_BOOLEAN = "EBoolean";//$NON-NLS-1$
	private static final String E_BOOLEAN_OBJECT = "EBooleanObject";//$NON-NLS-1$
	private static final String E_BYTE = "EByte";//$NON-NLS-1$
	private static final String E_BYTE_ARRAY = "EByteArray";//$NON-NLS-1$
	private static final String E_BYTE_OBJECT = "EByteObject";//$NON-NLS-1$
	private static final String E_CHAR = "EChar";//$NON-NLS-1$
	private static final String E_CHARACTER_OBJECT = "ECharacterObject";//$NON-NLS-1$
	private static final String E_DATE = "EDate";//$NON-NLS-1$
	private static final String E_DOUBLE = "EDouble";//$NON-NLS-1$
	private static final String E_DOUBLE_OBJECT = "EDoubleObject";//$NON-NLS-1$
	private static final String E_FLOAT = "EFloat";//$NON-NLS-1$
	private static final String E_FLOAT_OBJECT = "EFloatObject";//$NON-NLS-1$
	private static final String E_INT = "EInt";//$NON-NLS-1$
	private static final String E_INTEGER_OBJECT = "EIntegerObject";//$NON-NLS-1$
	private static final String E_JAVA_CLASS = "EJavaClass";//$NON-NLS-1$
	private static final String E_JAVA_OBJECT = "EJavaObject";//$NON-NLS-1$
	private static final String E_LONG = "ELong";//$NON-NLS-1$
	private static final String E_LONG_OBJECT = "ELongObject";//$NON-NLS-1$
	private static final String E_SHORT = "EShort";//$NON-NLS-1$
	private static final String E_SHORT_OBJECT = "EShortObject";//$NON-NLS-1$
	private static final String E_STRING = "EString";//$NON-NLS-1$

	/**
	 *
	 * {@inheritDoc}
	 *
	 * @see org.eclipse.emf.ecore.impl.EFactoryImpl#createFromString(org.eclipse.emf.ecore.EDataType, java.lang.String)
	 */
	// BEGIN COMPLEX CODE
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		final String id = eDataType.getName();

		if (E_BIG_DECIMAL.equals(id)) {
			return createEBigDecimalFromString(eDataType, initialValue);
		} else if (E_BIG_INTEGER.equals(id)) {
			return createEBigIntegerFromString(eDataType, initialValue);
		} else if (E_BOOLEAN.equals(id)) {
			return createEBooleanFromString(eDataType, initialValue);
		} else if (E_BOOLEAN_OBJECT.equals(id)) {
			return createEBooleanObjectFromString(eDataType, initialValue);
		} else if (E_BYTE.equals(id)) {
			return createEByteFromString(eDataType, initialValue);
		} else if (E_BYTE_ARRAY.equals(id)) {
			return createEByteArrayFromString(eDataType, initialValue);
		} else if (E_BYTE_OBJECT.equals(id)) {
			return createEByteObjectFromString(eDataType, initialValue);
		} else if (E_CHAR.equals(id)) {
			return createECharFromString(eDataType, initialValue);
		} else if (E_CHARACTER_OBJECT.equals(id)) {
			return createECharacterObjectFromString(eDataType, initialValue);
		} else if (E_DATE.equals(id)) {
			return createEDateFromString(eDataType, initialValue);
		} else if (E_DOUBLE.equals(id)) {
			return createEDoubleFromString(eDataType, initialValue);
		} else if (E_DOUBLE_OBJECT.equals(id)) {
			return createEDoubleObjectFromString(eDataType, initialValue);
		} else if (E_FLOAT.equals(id)) {
			return createEFloatFromString(eDataType, initialValue);
		} else if (E_FLOAT_OBJECT.equals(id)) {
			return createEFloatObjectFromString(eDataType, initialValue);
		} else if (E_INT.equals(id)) {
			return createEIntFromString(eDataType, initialValue);
		} else if (E_INTEGER_OBJECT.equals(id)) {
			return createEIntegerObjectFromString(eDataType, initialValue);
		} else if (E_JAVA_CLASS.equals(id)) {
			return createEJavaClassFromString(eDataType, initialValue);
		} else if (E_JAVA_OBJECT.equals(id)) {
			return createEJavaObjectFromString(eDataType, initialValue);
		} else if (E_LONG.equals(id)) {
			return createELongFromString(eDataType, initialValue);
		} else if (E_LONG_OBJECT.equals(id)) {
			return createELongObjectFromString(eDataType, initialValue);
		} else if (E_SHORT.equals(id)) {
			return createEShortFromString(eDataType, initialValue);
		} else if (E_SHORT_OBJECT.equals(id)) {
			return createEShortObjectFromString(eDataType, initialValue);
		} else if (E_STRING.equals(id)) {
			return createEStringFromString(eDataType, initialValue);
		}
		return super.createFromString(eDataType, initialValue);
	}

	// END COMPLEX CODE

	@Override
	protected EObject basicCreate(EClass eClass) {
		if (eClass.getInstanceClassName() == "java.util.Map$Entry") { //$NON-NLS-1$
			return super.basicCreate(eClass);
		}
		// Create real objects instead of dynamic EObjects (like EFactory) because references to meta objects (e.g.
		// EStructuralFeatures) can break the migration when they are dynamic EObjects.
		final EObject eObject = EcoreFactory.eINSTANCE.create(eClass);
		// Explicitly set the EClass because it is not set by default. When it is not set, .eClass() calls on the
		// EObject fall back to the EClass in the registered Ecore EPackage. However, the migration might not use the
		// registered EPackage. Thus, the EClass is set here.
		BasicEObjectImpl.class.cast(eObject).eSetClass(eClass);
		return eObject;
	}

	private Boolean booleanValueOf(String initialValue) {
		if ("true".equalsIgnoreCase(initialValue)) { //$NON-NLS-1$
			return Boolean.TRUE;
		} else if ("false".equalsIgnoreCase(initialValue)) { //$NON-NLS-1$
			return Boolean.FALSE;
		} else {
			throw new IllegalArgumentException("Expecting true or false"); //$NON-NLS-1$
		}
	}

	private Boolean createEBooleanObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : booleanValueOf(initialValue);
	}

	private Character createECharacterObjectFromString(EDataType metaObject,
		String initialValue) {
		if (initialValue == null) {
			return null;
		}

		char charValue = 0;
		try {
			charValue = (char) Integer.parseInt(initialValue);
		} catch (final NumberFormatException e) {
			final char[] carray = initialValue.toCharArray();
			charValue = carray[0];
		}
		return charValue;
	}

	private Date createEDateFromString(EDataType eDataType, String initialValue) {
		if (initialValue == null) {
			return null;
		}

		Exception exception = null;
		for (int i = 0; i < EDATE_FORMATS.length; ++i) {
			try {
				return EDATE_FORMATS[i].parse(initialValue);
			} catch (final ParseException parseException) {
				exception = parseException;
			}
		}
		throw new WrappedException(exception);
	}

	private Double createEDoubleObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Double.valueOf(initialValue);
	}

	private Float createEFloatObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Float.valueOf(initialValue);
	}

	private Integer createEIntegerObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Integer.valueOf(initialValue);
	}

	private BigDecimal createEBigDecimalFromString(EDataType eDataType,
		String initialValue) {
		return initialValue == null ? null : new BigDecimal(initialValue);
	}

	private BigInteger createEBigIntegerFromString(EDataType eDataType,
		String initialValue) {
		return initialValue == null ? null : new BigInteger(initialValue);
	}

	private String createEStringFromString(EDataType metaObject,
		String initialValue) {
		return initialValue;
	}

	private Integer createEIntFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Integer.valueOf(initialValue);
	}

	private Boolean createEBooleanFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : booleanValueOf(initialValue);
	}

	private Byte createEByteObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Byte.valueOf(initialValue);
	}

	private Float createEFloatFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Float.valueOf(initialValue);
	}

	private Character createECharFromString(EDataType metaObject,
		String initialValue) {
		if (initialValue == null) {
			return null;
		}
		char charValue = 0;
		try {
			charValue = (char) Integer.parseInt(initialValue);
		} catch (final NumberFormatException e) {
			final char[] carray = initialValue.toCharArray();
			charValue = carray[0];
		}
		return charValue;
	}

	private Long createELongFromString(EDataType metaObject, String initialValue) {
		return initialValue == null ? null : Long.valueOf(initialValue);
	}

	private Double createEDoubleFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Double.valueOf(initialValue);
	}

	private Byte createEByteFromString(EDataType metaObject, String initialValue) {
		return initialValue == null ? null : Byte.valueOf(initialValue);
	}

	private Short createEShortFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Short.valueOf(initialValue);
	}

	private Class<?> createEJavaClassFromString(EDataType metaObject,
		String initialValue) {
		try {
			if (initialValue == null) {
				return null;
			} else if ("boolean".equals(initialValue)) { //$NON-NLS-1$
				return boolean.class;
			} else if ("byte".equals(initialValue)) { //$NON-NLS-1$
				return byte.class;
			} else if ("char".equals(initialValue)) { //$NON-NLS-1$
				return char.class;
			} else if ("double".equals(initialValue)) { //$NON-NLS-1$
				return double.class;
			} else if ("float".equals(initialValue)) { //$NON-NLS-1$
				return float.class;
			} else if ("int".equals(initialValue)) { //$NON-NLS-1$
				return int.class;
			} else if ("long".equals(initialValue)) { //$NON-NLS-1$
				return long.class;
			} else if ("short".equals(initialValue)) { //$NON-NLS-1$
				return short.class;
			} else {
				return Class.forName(initialValue);
			}
		} catch (final ClassNotFoundException e) {
			throw new WrappedException(e);
		}
	}

	private Object createEJavaObjectFromString(EDataType eDataType,
		String initialValue) {
		return createFromString(initialValue);
	}

	private Long createELongObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Long.valueOf(initialValue);
	}

	private Short createEShortObjectFromString(EDataType metaObject,
		String initialValue) {
		return initialValue == null ? null : Short.valueOf(initialValue);
	}

	private byte[] createEByteArrayFromString(EDataType eDataType,
		String initialValue) {
		return hexStringToByteArray(initialValue);
	}

	private byte[] hexStringToByteArray(String initialValue) {
		if (initialValue == null) {
			return null;
		}

		final int size = initialValue.length();
		int limit = (size + 1) / 2;
		final byte[] result = new byte[limit];
		if (size % 2 != 0) {
			result[--limit] = hexCharToByte(initialValue.charAt(size - 1));
		}

		for (int i = 0, j = 0; i < limit; ++i) {
			final byte high = hexCharToByte(initialValue.charAt(j++));
			final byte low = hexCharToByte(initialValue.charAt(j++));
			result[i] = (byte) (high << 4 | low);
		}
		return result;
	}

	// BEGIN COMPLEX CODE
	private byte hexCharToByte(char character) {
		switch (character) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9': {
			return (byte) (character - '0');
		}
		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f': {
			return (byte) (character - 'a' + 10);
		}
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F': {
			return (byte) (character - 'A' + 10);
		}
		default: {
			throw new NumberFormatException("Invalid hexadecimal"); //$NON-NLS-1$
		}
		}
	}

	// END COMPLEX CODE

	// BEGIN COMPLEX CODE
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {

		final String id = eDataType.getName();

		if (E_BIG_DECIMAL.equals(id)) {
			return convertEBigDecimalToString(eDataType, instanceValue);
		} else if (E_BIG_INTEGER.equals(id)) {
			return convertEBigIntegerToString(eDataType, instanceValue);
		} else if (E_BOOLEAN.equals(id)) {
			return convertEBooleanToString(eDataType, instanceValue);
		} else if (E_BOOLEAN_OBJECT.equals(id)) {
			return convertEBooleanObjectToString(eDataType, instanceValue);
		} else if (E_BYTE.equals(id)) {
			return convertEByteToString(eDataType, instanceValue);
		} else if (E_BYTE_ARRAY.equals(id)) {
			return convertEByteArrayToString(eDataType, instanceValue);
		} else if (E_BYTE_OBJECT.equals(id)) {
			return convertEByteObjectToString(eDataType, instanceValue);
		} else if (E_CHAR.equals(id)) {
			return convertECharToString(eDataType, instanceValue);
		} else if (E_CHARACTER_OBJECT.equals(id)) {
			return convertECharacterObjectToString(eDataType, instanceValue);
		} else if (E_DATE.equals(id)) {
			return convertEDateToString(eDataType, instanceValue);
		} else if (E_DOUBLE.equals(id)) {
			return convertEDoubleToString(eDataType, instanceValue);
		} else if (E_DOUBLE_OBJECT.equals(id)) {
			return convertEDoubleObjectToString(eDataType, instanceValue);
		} else if (E_FLOAT.equals(id)) {
			return convertEFloatToString(eDataType, instanceValue);
		} else if (E_FLOAT_OBJECT.equals(id)) {
			return convertEFloatObjectToString(eDataType, instanceValue);
		} else if (E_INT.equals(id)) {
			return convertEIntToString(eDataType, instanceValue);
		} else if (E_INTEGER_OBJECT.equals(id)) {
			return convertEIntegerObjectToString(eDataType, instanceValue);
		} else if (E_JAVA_CLASS.equals(id)) {
			return convertEJavaClassToString(eDataType, instanceValue);
		} else if (E_JAVA_OBJECT.equals(id)) {
			return convertEJavaObjectToString(eDataType, instanceValue);
		} else if (E_LONG.equals(id)) {
			return convertELongToString(eDataType, instanceValue);
		} else if (E_LONG_OBJECT.equals(id)) {
			return convertELongObjectToString(eDataType, instanceValue);
		} else if (E_SHORT.equals(id)) {
			return convertEShortToString(eDataType, instanceValue);
		} else if (E_SHORT_OBJECT.equals(id)) {
			return convertEShortObjectToString(eDataType, instanceValue);
		} else if (E_STRING.equals(id)) {
			return convertEStringToString(eDataType, instanceValue);
		}
		return super.convertToString(eDataType, instanceValue);
	}

	// END COMPLEX CODE

	private String convertEBooleanObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertECharacterObjectToString(EDataType metaObject, Object instanceValue) {
		if (instanceValue instanceof Character) {
			return Integer.toString((Character) instanceValue);
		}

		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEDateToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return null;
		}

		return EDATE_FORMATS[0].format((Date) instanceValue);

	}

	private String convertEDoubleObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEFloatObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEIntegerObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEBigDecimalToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEBigIntegerToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEStringToString(EDataType metaObject, Object instanceValue) {
		return (String) instanceValue;
	}

	private String convertEIntToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEBooleanToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEByteObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEFloatToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertECharToString(EDataType metaObject, Object instanceValue) {
		if (instanceValue instanceof Character) {
			return Integer.toString((Character) instanceValue);
		}

		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertELongToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEDoubleToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEByteToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEByteArrayToString(EDataType eDataType, Object instanceValue) {
		if (instanceValue == null) {
			return null;
		}
		final byte[] bytes = (byte[]) instanceValue;
		return convertBytesToHexString(bytes, bytes.length);

	}

	private String convertEShortToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEJavaClassToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? "" : ((Class<?>) instanceValue).getName(); //$NON-NLS-1$
	}

	private String convertEJavaObjectToString(EDataType eDataType, Object instanceValue) {
		return convertToString(instanceValue);
	}

	private String convertELongObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private String convertEShortObjectToString(EDataType metaObject, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
		'E', 'F' };

	private String convertBytesToHexString(byte[] bytes, int count) {
		if (bytes == null) {
			return null;
		}
		final char[] result = new char[2 * count];
		for (int i = 0, j = 0; i < count; ++i) {
			final int high = bytes[i] >> 4 & 0xF;
			final int low = bytes[i] & 0xF;
			result[j++] = HEX_DIGITS[high];
			result[j++] = HEX_DIGITS[low];
		}
		return new String(result);

	}

}
