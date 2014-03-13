package pl.jsolve.sweetener.mapper.builder.strategies;

import pl.jsolve.sweetener.converter.TypeConverter;
import pl.jsolve.sweetener.exception.ConversionException;
import pl.jsolve.sweetener.mapper.annotationdriven.exception.MappingException;

public class TypeConverterStrategy implements MapperBuilderStrategy {

	@Override
	public Object apply(Object object, Class<?> targetType) {
		if (object != null && !object.getClass().equals(targetType)) {
			return tryToConvertType(object, targetType);
		}
		return object;
	}

	private Object tryToConvertType(Object object, Class<?> targetType) {
		try {
			return TypeConverter.convert(object, targetType);
		} catch (ConversionException ce) {
			throw new MappingException(ce, "Type conversion between fields of type %s and %s failed.", object.getClass(), targetType);
		}
	}
}