package com.broad.common.annotation;

import com.broad.common.utils.poi.ExcelHandlerAdapter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

/**
 * 自定义导出Excel数据注解
 *
 * @author XingGao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {
    /**
     * 导出时在excel中排序
     *
     * @return the int
     */
    int sort() default Integer.MAX_VALUE;

    /**
     * 导出到Excel中的名字.
     *
     * @return the string
     */
    String name() default "";

    /**
     * 日期格式, 如: yyyy-MM-dd
     *
     * @return the string
     */
    String dateFormat() default "";

    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     *
     * @return the string
     */
    String readConverterExp() default "";

    /**
     * 分隔符，读取字符串组内容
     *
     * @return the string
     */
    String separator() default ",";

    /**
     * BigDecimal 精度 默认:-1(默认不开启BigDecimal格式化)
     *
     * @return the int
     */
    int scale() default -1;

    /**
     * BigDecimal 舍入规则 默认:BigDecimal.ROUND_HALF_EVEN
     *
     * @return the int
     */
    int roundingMode() default BigDecimal.ROUND_HALF_EVEN;

    /**
     * 导出时在excel中每个列的高度 单位为字符
     *
     * @return the double
     */
    double height() default 14;

    /**
     * 导出时在excel中每个列的宽 单位为字符
     *
     * @return the double
     */
    double width() default 16;

    /**
     * 文字后缀,如% 90 变成90%
     *
     * @return the string
     */
    String suffix() default "";

    /**
     * 当值为空时,字段的默认值
     *
     * @return the string
     */
    String defaultValue() default "";

    /**
     * 提示信息
     *
     * @return the string
     */
    String prompt() default "";

    /**
     * 设置只能选择不能输入的列内容.
     *
     * @return the string [ ]
     */
    String[] combo() default {};

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写.
     *
     * @return the boolean
     */
    boolean isExport() default true;

    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开
     *
     * @return the string
     */
    String targetAttr() default "";

    /**
     * 是否自动统计数据,在最后追加一行统计数据总和
     *
     * @return the boolean
     */
    boolean isStatistics() default false;

    /**
     * 导出类型（0数字 1字符串）
     *
     * @return the column type
     */
    ColumnType cellType() default ColumnType.STRING;

    /**
     * 导出列头背景色
     *
     * @return the indexed colors
     */
    IndexedColors headerBackgroundColor() default IndexedColors.GREY_50_PERCENT;

    /**
     * 导出列头字体颜色
     *
     * @return the indexed colors
     */
    IndexedColors headerColor() default IndexedColors.WHITE;

    /**
     * 导出单元格背景色
     *
     * @return the indexed colors
     */
    IndexedColors backgroundColor() default IndexedColors.WHITE;

    /**
     * 导出单元格字体颜色
     *
     * @return the indexed colors
     */
    IndexedColors color() default IndexedColors.BLACK;

    /**
     * 导出字段对齐方式
     *
     * @return the horizontal alignment
     */
    HorizontalAlignment align() default HorizontalAlignment.CENTER;

    /**
     * 自定义数据处理器
     *
     * @return the class
     */
    Class<?> handler() default ExcelHandlerAdapter.class;

    /**
     * 自定义数据处理器参数
     *
     * @return the string [ ]
     */
    String[] args() default {};

    /**
     * 字段类型（0：导出导入；1：仅导出；2：仅导入）
     *
     * @return the type
     */
    Type type() default Type.ALL;

    /**
     * The enum Type.
     */
    enum Type {
        /**
         * All type.
         */
        ALL(0),
        /**
         * Export type.
         */
        EXPORT(1),
        /**
         * Import type.
         */
        IMPORT(2);
        private final int value;

        Type(int value) {
            this.value = value;
        }

        /**
         * Value int.
         *
         * @return the int
         */
        public int value() {
            return this.value;
        }
    }

    /**
     * The enum Column type.
     */
    enum ColumnType {
        /**
         * Numeric column type.
         */
        NUMERIC(0),
        /**
         * String column type.
         */
        STRING(1),
        /**
         * Image column type.
         */
        IMAGE(2);
        private final int value;

        ColumnType(int value) {
            this.value = value;
        }

        /**
         * Value int.
         *
         * @return the int
         */
        public int value() {
            return this.value;
        }
    }
}