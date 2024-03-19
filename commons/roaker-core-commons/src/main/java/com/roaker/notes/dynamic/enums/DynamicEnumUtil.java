package com.roaker.notes.dynamic.enums;

import com.google.common.collect.Sets;
import jdk.internal.reflect.ConstructorAccessor;
import jdk.internal.reflect.FieldAccessor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import jdk.internal.reflect.ReflectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DynamicEnumUtil {

    private static Set<String> DYNAMIC_ENUM_FILED_SET = null;

    static {
        DYNAMIC_ENUM_FILED_SET = Sets.newHashSet("code", "name", "originCode", "originName");
    }

    private static final ReflectionFactory reflectionFactory = ReflectionFactory.getReflectionFactory();


    /**
     * 将enumValue的枚举值动态添加到enumType中
     */
    public static void addEnumBatch(Class clazz, List<DynamicDictDO> dynamicDictList) {
        log.info("init dynamic enum,class name is:[{}]", clazz.getName());
        if (CollectionUtils.isEmpty(dynamicDictList)) {
            log.warn("init dynamic enum,dynamicDict is empty");
            return;
        }
        if (!CommonEnum.class.isAssignableFrom(clazz) || !CommonOriginalEnum.class.isAssignableFrom(clazz)) {
            log.warn("init dynamic enum,class is not an instance of CommonEnum & CommonOriginalEnum");
            return;
        }
        //1、获取枚举实例名
        List<String> bizNameList = dynamicDictList.stream().map(DynamicDictDO::getBizName).collect(Collectors.toList());
        //2、获取枚举实例code
        List<Integer> bizCodeList = dynamicDictList.stream().map(DynamicDictDO::getCode).collect(Collectors.toList());
        //3、枚举实例参数类型
        Class<?>[] additionalTypes = new Class<?>[]{Integer.class, String.class, String.class, String.class};
        //4、枚举实例参数值
        List<Object[]> additionalValuesList = dynamicDictList.stream().map(dictDO ->
                        new Object[]{dictDO.getCode(), dictDO.getName(),
                                dictDO.getLabel(), dictDO.getValue()})
                .collect(Collectors.toList());

        Field[] allFields = clazz.getDeclaredFields();
        List<String> fieldNameList = Arrays.stream(allFields).map(Field::getName).collect(Collectors.toList());
        if (!fieldNameList.contains("originCode") && !fieldNameList.contains("originName")) {
            additionalTypes = new Class<?>[]{Integer.class, String.class};
            additionalValuesList = dynamicDictList.stream().map(dictDO ->
                            new Object[]{dictDO.getCode(), dictDO.getName()})
                    .collect(Collectors.toList());
        }
        addEnumBatch(clazz, bizNameList, bizCodeList, additionalTypes, additionalValuesList);
    }


    /**
     * 批量添加枚举类型实例
     *
     * @param enumType             枚举类型
     * @param enumNames            名字名字列表
     * @param additionalTypes      枚举构造器参数类型
     * @param additionalValuesList 枚举构造器入参参数值列表
     * @return Collection<T> 枚举列表值
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> Collection<T> addEnumBatch(Class<T> enumType, List<String> enumNames, List<Integer> enumCodes,
                                                                 Class<?>[] additionalTypes,
                                                                 List<Object[]> additionalValuesList) {

        //0、 Sanity checks
        if (!Enum.class.isAssignableFrom(enumType)) {
            throw new RuntimeException("class " + enumType + " is not an instance of Enum");
        }

        //1、获取枚举类中已经存在的枚举实例的字段
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().contains("$VALUES")) {
                valuesField = field;
                break;
            }
        }
        if (valuesField == null) {
            return null;
        }

        AccessibleObject.setAccessible(new Field[]{valuesField}, true);

        try {

            //2、获取枚举类中已经存在的枚举实例
            T[] previousValues = (T[]) valuesField.get(enumType);
            Collection<T> values = new ArrayList<>(Arrays.asList(previousValues));
            Map<String, T> valuesMap = new LinkedHashMap<>();
            Map<Integer, T> codeMap = new LinkedHashMap<>();
            values.forEach(e -> valuesMap.put(String.valueOf(e), e));
            valuesMap.values().forEach(e -> codeMap.put(CommonEnum.class.cast(e).getCode(), e));
            //3、构建组合新的枚举实例
            for (int i = 0; i < enumNames.size(); i++) {
                String enumName = enumNames.get(i);
                T findByName = valuesMap.get(enumName);
                T findByCode = codeMap.get(enumCodes.get(i));
                T value = null;
                if (findByName == null && findByCode == null) {
                    value = (T) makeEnum(enumType, enumName, values.size(), additionalTypes,
                            additionalValuesList.get(i));
                } else {
                    value = findByCode == null ? findByName : findByCode;
                    rewriteDynamicEnumField(enumType, value, additionalValuesList.get(i));
                    continue;
                }

                //4、添加新的枚举实例
                valuesMap.put(enumName, value);
                codeMap.put(enumCodes.get(i), value);
            }
            values = valuesMap.values();

            //5、Set new values field
            setFailsafeFieldValue(valuesField, null, values.toArray((T[]) Array.newInstance(enumType, 0)));

            //6、Clean enum cache
            cleanEnumCache(enumType);
            return values;
        } catch (Exception e) {
            log.error("addEnumBatch error", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SneakyThrows
    private static void setFailsafeFieldValue(Field field, Object target, Object value) {
        field.setAccessible(true);

        //1、获取修饰符字段
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        //2、获取字段修饰符
        int modifiers = modifiersField.getInt(field);
        //3、置空Final位
        modifiers &= ~Modifier.FINAL;
        modifiersField.setInt(field, modifiers);
        FieldAccessor fa = reflectionFactory.newFieldAccessor(field, false);
        fa.set(target, value);
    }

    /**
     * 反射调用枚举的set方法去写入枚举的属性
     *
     * @param enumType         枚举类型
     * @param value            枚举实例
     * @param additionalValues 枚举构造器入参参数值
     */
    private static <T extends Enum<?>> void rewriteDynamicEnumField(Class<T> enumType, T value, Object[] additionalValues) {
        //1、获取枚举类中对应的字段
        List<Field> fields = Arrays.stream(enumType.getDeclaredFields())
                .filter(e -> DYNAMIC_ENUM_FILED_SET.contains(e.getName()))
                .collect(Collectors.toList());

        for (int i = 0; i < fields.size(); i++) {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(fields.get(i).getName(), enumType);
                Method writeMethod = pd.getWriteMethod();
                writeMethod.invoke(value, additionalValues[i]);
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                log.error("rewriteDynamicEnumField error", e);
            }
        }
    }

    @SneakyThrows
    private static <T extends Enum<?>> Object makeEnum(Class<T> enumType, String enumName, int ordinal, Class<?>[] additionalTypes, Object[] additionalValues) {
        Object[] params = new Object[additionalValues.length + 2];
        params[0] = enumName;
        params[1] = ordinal;
        System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
        return enumType.cast(getConstructorAccessor(enumType, additionalTypes).newInstance(params));
    }

    private static ConstructorAccessor getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes)
            throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = int.class;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return reflectionFactory.newConstructorAccessor(enumClass.getDeclaredConstructor(parameterTypes));
    }

    private static void cleanEnumCache(Class<?> enumClass) {
        blankField(enumClass, "enumConstantDirectory"); // Sun (Oracle?!?) JDK 1.5/6
        blankField(enumClass, "enumConstants"); // IBM JDK
    }

    private static void blankField(Class<?> enumClass, String fieldName) {
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                AccessibleObject.setAccessible(new Field[]{field}, true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }
    }
}
