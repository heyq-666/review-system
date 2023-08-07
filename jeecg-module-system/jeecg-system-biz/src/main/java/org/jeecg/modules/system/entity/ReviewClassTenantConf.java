package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author javabage
 * @date 2023/8/4
 */
@Data
@TableName("review_class_tenant_conf")
public class ReviewClassTenantConf {
    private static final long serialVersionUID = 1L;
    /**ID*/
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private Long tenantId;

    private String classId;
}
