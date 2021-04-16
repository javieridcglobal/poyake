package com.jsantos.common.constraint;

import com.jsantos.common.util.ListValues;
import com.jsantos.common.util.MTMapValues;
import com.jsantos.orm.exceptions.IValidationError;
import com.jsantos.orm.mt.MTDataType;
import com.jsantos.orm.mt.MTField;

public interface IConstraintsBuilder {
	public <T> ListValues<IValidationError> validate(MTField mtField, Object value, MTMapValues<T> values);
	public default   MTField forField() {return null;};
	public default   MTDataType forModelType() {return null;};
}
