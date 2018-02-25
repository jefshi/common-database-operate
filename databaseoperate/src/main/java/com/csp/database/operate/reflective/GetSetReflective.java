package com.csp.database.operate.reflective;


import com.csp.database.operate.annotations.TableFieldName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: table field value get or set by reflective
 * <p>Create Date: 2018/02/25
 * <p>Modify Date: nothing
 *
 * @author csp
 * @version 1.0.0
 * @since common-database-operate 1.0.0
 */
@SuppressWarnings({"unused"})
public class GetSetReflective<T> {
    private final Class<T> mClass;
    private final Map<String, Field> mFieldMap = new HashMap<>();
    private final Map<String, Method> mGetMethodMap = new HashMap<>();
    private final Map<String, Method> mSetMethodMap = new HashMap<>();

    public GetSetReflective(Class<T> cls) {
        mClass = cls;
        initFieldMap();
        initMethodMap();
    }

    private void initFieldMap() {
        Class<? super T> cls = mClass;
        Field[] fields;
        TableFieldName annotation;
        do {
            fields = cls.getDeclaredFields();
            for (Field field : fields) {
                annotation = field.getAnnotation(TableFieldName.class);
                mFieldMap.put(annotation == null ? field.getName() : annotation.value(), field);
            }
            cls = cls.getSuperclass();
        } while (!Object.class.equals(cls));
    }

    private void initMethodMap() {
        Method[] methods = mClass.getMethods();
        TableFieldName annotation;
        for (Method method : methods) {
            if (method.isAnnotationPresent(TableFieldName.class)) {
                annotation = method.getAnnotation(TableFieldName.class);
                if (void.class.equals(method.getReturnType())
                        && method.getParameterTypes().length == 1) {
                    mSetMethodMap.put(annotation.value(), method);
                } else if (!void.class.equals(method.getReturnType())
                        && method.getParameterTypes().length == 0) {
                    mGetMethodMap.put(annotation.value(), method);
                }
            }
        }
    }

    public Object getValue(T object, String key)
            throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (key == null || key.isEmpty()) {
            throw new NullPointerException();
        }

        Field field = mFieldMap.get(key);
        if (field == null) {
            return getValueByMethod(object, key);
        }

        field.setAccessible(true);
        return field.get(object);
    }

    private Object getValueByMethod(T object, String key)
            throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method method = mGetMethodMap.get(key);
        if (method == null) {
            throw new NoSuchFieldException(key);
        }

        return method.invoke(object);
    }

    public void setValue(T object, String key, Object value)
            throws IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        Field field = mFieldMap.get(key);
        if (field == null) {
            setValueByMethod(object, key, value);
            return;
        }

        field.setAccessible(true);
        field.set(object, value);
    }

    private void setValueByMethod(T object, String key, Object value)
            throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Method method = mSetMethodMap.get(key);
        if (method == null) {
            throw new NoSuchFieldException(key);
        }

        method.invoke(object, value);
    }
}
