package org.jeecg.config.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 单数据源配置（jeecg.datasource.open = false时生效）
 * @Author wwq
 *
 */
@Configuration
@MapperScan(value={"org.jeecg.modules.**.mapper*","com.review.**.mapper*"})
public class MybatisPlusSaasConfig {
    /**
     * tenant_id 字段名
     */
    private static final String TENANT_FIELD_NAME = "tenant_id";
    /**
     * 哪些表需要做多租户 表需要添加一个字段 tenant_id
     */
    public static final List<String> TENANT_TABLE = new ArrayList<String>();

    public static final Boolean OPEN_SYSTEM_TENANT_CONTROL = true;

    static {

        if (OPEN_SYSTEM_TENANT_CONTROL) {
            //系统管理表
            TENANT_TABLE.add("demo");
            //TENANT_TABLE.add("sys_role_permission");
            //TENANT_TABLE.add("sys_permission");
            //TENANT_TABLE.add("sys_permission_v2");
            //TENANT_TABLE.add("sys_role");
            TENANT_TABLE.add("sys_depart");
            TENANT_TABLE.add("sys_data_log");
            TENANT_TABLE.add("sys_log");
            TENANT_TABLE.add("sys_sms");
           // TENANT_TABLE.add("sys_user");
            TENANT_TABLE.add("sys_announcement");

            //测评业务表
            //TENANT_TABLE.add("review_answer");
            TENANT_TABLE.add("review_banner");
            //TENANT_TABLE.add("review_class");
            TENANT_TABLE.add("review_eval_code");
            TENANT_TABLE.add("review_expert");
            TENANT_TABLE.add("review_notice");
            TENANT_TABLE.add("review_order");
            TENANT_TABLE.add("review_project");
            //TENANT_TABLE.add("review_question");
            //TENANT_TABLE.add("review_report");
            //TENANT_TABLE.add("review_report_grade");
            TENANT_TABLE.add("review_report_result");
            //TENANT_TABLE.add("review_report_variate");
            TENANT_TABLE.add("review_result");
            TENANT_TABLE.add("review_subject");
            TENANT_TABLE.add("review_user");
            //TENANT_TABLE.add("review_variate");
            TENANT_TABLE.add("review_video_analysis");
        }

    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String tenantId = oConvertUtils.getString(TenantContext.getTenant(),"-1");
                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn(){
                return TENANT_FIELD_NAME;
            }

            // 返回 true 表示不走租户逻辑
            @Override
            public boolean ignoreTable(String tableName) {
                //小程序用户必须按照 租户ID统一查询
                LongValue TenantId = (LongValue)getTenantId();
                //后端用户 只有admin用户 才能看所有的数据
                if (TenantId.getValue() == -1 && !tableName.startsWith("review_") && JwtUtil.userIsAdmin()) {
                    return true;
                }

                for(String temp: TENANT_TABLE){
                    if(temp.equalsIgnoreCase(tableName)){
                        return false;
                    }
                }
                return true;
            }
        }));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //update-begin-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor());
        //update-end-author:zyf date:20220425 for:【VUEN-606】注入动态表名适配拦截器解决多表名问题
        return interceptor;
    }

    /**
     * 动态表名切换拦截器,用于适配vue2和vue3同一个表有多个的情况,如sys_role_index在vue3情况下表名为sys_role_index_v3
     * @return
     */
    private DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor() {
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler((sql, tableName) -> {
            //获取需要动态解析的表名
            String dynamicTableName = ThreadLocalDataHelper.get(CommonConstant.DYNAMIC_TABLE_NAME);
            //当dynamicTableName不为空时才走动态表名处理逻辑,否则返回原始表名
            if (ObjectUtil.isNotEmpty(dynamicTableName) && dynamicTableName.equals(tableName)) {
                // 获取前端传递的版本号标识
                Object version = ThreadLocalDataHelper.get(CommonConstant.VERSION);
                if (ObjectUtil.isNotEmpty(version)) {
                    //拼接表名规则(原始表名+下划线+前端传递的版本号)
                    return tableName + "_" + version;
                }
            }
            return tableName;
        });
        return dynamicTableNameInnerInterceptor;
    }

//    /**
//     * 下个版本会删除，现在为了避免缓存出现问题不得不配置
//     * @return
//     */
//    @Bean
//    public ConfigurationCustomizer configurationCustomizer() {
//        return configuration -> configuration.setUseDeprecatedExecutor(false);
//    }
//    /**
//     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
//     */
//    @Bean
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

}
