package com.review.manage.expert.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * 部门表
 * <p>
 * 
 * @Author Steve
 * @Since  2019-01-22
 */
@Data
@TableName("expert_be_good_at")
public class ExpertBeGoodAt implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
	private String id;
	/**父机构ID*/
	private String parentId;
	/**机构/部门名称*/
	@Excel(name="机构/部门名称",width=15)
	private String beGoodAtName;
	/**英文名*/
	@Excel(name="英文名",width=15)
	private String beGoodAtNameEn;
	/**缩写*/
	private String beGoodAtNameAbbr;
	/**排序*/
	@Excel(name="排序",width=15)
	private Integer departOrder;
	/**描述*/
	@Excel(name="描述",width=15)
	private String description;
	/**机构类别 1=公司，2=组织机构，3=岗位*/
	@Excel(name="机构类别",width=15,dicCode="org_category")
	private String orgCategory;
	/**机构类型*/
	private String orgType;
	/**机构编码*/
	@Excel(name="机构编码",width=15)
	private String orgCode;
	/**状态（1启用，0不启用）*/
	@Dict(dicCode = "depart_status")
	private String status;
	/**删除状态（0，正常，1已删除）*/
	@Dict(dicCode = "del_flag")
	private String delFlag;
	/**创建人*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	/**
	 * 重写equals方法
	 */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        if (!super.equals(o)) {
			return false;
		}
        ExpertBeGoodAt depart = (ExpertBeGoodAt) o;
        return Objects.equals(id, depart.id) &&
                Objects.equals(parentId, depart.parentId) &&
                Objects.equals(beGoodAtName, depart.beGoodAtName) &&
                Objects.equals(beGoodAtNameEn, depart.beGoodAtNameEn) &&
                Objects.equals(beGoodAtNameAbbr, depart.beGoodAtNameAbbr) &&
                Objects.equals(departOrder, depart.departOrder) &&
                Objects.equals(description, depart.description) &&
                Objects.equals(orgCategory, depart.orgCategory) &&
                Objects.equals(orgType, depart.orgType) &&
                Objects.equals(orgCode, depart.orgCode) &&
                Objects.equals(status, depart.status) &&
                Objects.equals(delFlag, depart.delFlag) &&
                Objects.equals(createBy, depart.createBy) &&
                Objects.equals(createTime, depart.createTime) &&
                Objects.equals(updateBy, depart.updateBy) &&
                Objects.equals(updateTime, depart.updateTime);
    }

    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, parentId, beGoodAtName,
				beGoodAtNameEn, beGoodAtNameAbbr, departOrder, description,orgCategory,
        		orgType, orgCode, status,
        		delFlag, createBy, createTime, updateBy, updateTime);
    }
}
