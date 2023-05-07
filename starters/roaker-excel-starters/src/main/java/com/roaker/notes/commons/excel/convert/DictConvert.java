package com.roaker.notes.commons.excel.convert;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.roaker.notes.commons.excel.annoatations.DictFormat;
import com.roaker.notes.dynamic.enums.CommonEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lei.rao
 * @since 1.0
 */
@Slf4j
public class DictConvert implements Converter<Object> {
    @Override
    public Class<?> supportJavaTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        throw new UnsupportedOperationException("暂不支持，也不需要");
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        Class<? extends CommonEnum> type = getType(contentProperty);
        String name = cellData.getStringValue();
        CommonEnum commonEnum = CommonEnum.of(name, type);
        if (commonEnum == null) {
            log.error("[convertToJavaData][type({}) 解析不掉 name({})]", type, name);
            return null;
        }

        // 将 String 的 value 转换成对应的属性
        Class<?> fieldClazz = contentProperty.getField().getType();
        return Convert.convert(fieldClazz, commonEnum.getCode());
    }

    @Override
    public WriteCellData<?> convertToExcelData(Object value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        // 空时，返回空
        if (value == null) {
            return new WriteCellData<>("");
        }

        // 使用字典格式化
        Class<? extends CommonEnum> type = getType(contentProperty);
        Integer code = (Integer) value;
        CommonEnum commonEnum = CommonEnum.of(code, type);
        if (commonEnum == null) {
            log.error("[convertToExcelData][type({}) 解析不掉 code({})]", type, code);
            return null;
        }
        // 生成 Excel 小表格
        return new WriteCellData<>(commonEnum.getName());
    }

    private static Class<? extends CommonEnum> getType(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getAnnotation(DictFormat.class).value();
    }
}
