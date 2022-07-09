package practice4;

public class SqlBuilder {
    static String gte(Double value, String column) {
        return value == null ? null : column + " >= " + value;
    }

    static String lte(Double value, String column) {
        return value == null ? null : column + " <= " + value;
    }

    static String endWith(String value, String column) {
        return value == null ? null : column + " like '%" + value + '\'';
    }

    static String startWith(String value, String column) {
        return value == null ? null : column + " like '" + value + "%'";
    }
}
