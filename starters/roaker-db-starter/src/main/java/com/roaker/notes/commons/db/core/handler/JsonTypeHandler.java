package com.roaker.notes.commons.db.core.handler;

import com.roaker.notes.commons.utils.JacksonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lei.rao
 * @since 1.0
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.CHAR})
@MappedTypes({
        //todo 补充需要反序列的json数据库类型
})
public class JsonTypeHandler extends BaseTypeHandler<Object> {
    private final Class<?> jsonObjectType;

    public JsonTypeHandler(Class<?> type){
        this.jsonObjectType = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        Object amlInfoDO = JacksonUtils.from(JacksonUtils.toJSON(parameter), jsonObjectType);
        ps.setString(i, JacksonUtils.toJSON(amlInfoDO));
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return JacksonUtils.from(rs.getString(columnName), jsonObjectType);
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return JacksonUtils.from(rs.getString(columnIndex), jsonObjectType);
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return JacksonUtils.from(cs.getString(columnIndex), jsonObjectType);
    }
}
