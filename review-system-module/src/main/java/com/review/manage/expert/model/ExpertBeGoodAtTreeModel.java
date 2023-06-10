package com.review.manage.expert.model;

import com.review.manage.expert.entity.ExpertBeGoodAt;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 * 
 * @Author Steve
 * @Since 2019-01-22 
 */
public class ExpertBeGoodAtTreeModel implements Serializable{
	
    private static final long serialVersionUID = 1L;
    
    /** 对应SysDepart中的id字段,前端数据树中的key*/
    private String key;

    /** 对应SysDepart中的id字段,前端数据树中的value*/
    private String value;

    /** 对应depart_name字段,前端数据树中的title*/
    private String title;


    private boolean isLeaf;
    // 以下所有字段均与SysDepart相同
    
    private String id;

    private String parentId;

    private String beGoodAtName;

    private String beGoodAtNameEn;

    private String beGoodAtNameAbbr;

    private Integer departOrder;

    private String description;
    
    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String status;

    private String delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    private List<ExpertBeGoodAtTreeModel> children = new ArrayList<>();


    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     * @param expertBeGoodAt
     */
	public ExpertBeGoodAtTreeModel(ExpertBeGoodAt expertBeGoodAt) {
		this.key = expertBeGoodAt.getId();
        this.value = expertBeGoodAt.getId();
        this.title = expertBeGoodAt.getBeGoodAtName();
        this.id = expertBeGoodAt.getId();
        this.parentId = expertBeGoodAt.getParentId();
        this.beGoodAtName = expertBeGoodAt.getBeGoodAtName();
        this.beGoodAtNameEn = expertBeGoodAt.getBeGoodAtNameEn();
        this.beGoodAtNameAbbr = expertBeGoodAt.getBeGoodAtNameAbbr();
        this.departOrder = expertBeGoodAt.getDepartOrder();
        this.description = expertBeGoodAt.getDescription();
        this.orgCategory = expertBeGoodAt.getOrgCategory();
        this.orgType = expertBeGoodAt.getOrgType();
        this.orgCode = expertBeGoodAt.getOrgCode();
        this.status = expertBeGoodAt.getStatus();
        this.delFlag = expertBeGoodAt.getDelFlag();
        this.createBy = expertBeGoodAt.getCreateBy();
        this.createTime = expertBeGoodAt.getCreateTime();
        this.updateBy = expertBeGoodAt.getUpdateBy();
        this.updateTime = expertBeGoodAt.getUpdateTime();
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isleaf) {
         this.isLeaf = isleaf;
    }

    public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ExpertBeGoodAtTreeModel> getChildren() {
        return children;
    }

    public void setChildren(List<ExpertBeGoodAtTreeModel> children) {
        if (children==null){
            this.isLeaf=true;
        }
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
    public String getOrgCategory() {
		return orgCategory;
	}

	public void setOrgCategory(String orgCategory) {
		this.orgCategory = orgCategory;
	}

	public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getDepartOrder() {
        return departOrder;
    }

    public void setDepartOrder(Integer departOrder) {
        this.departOrder = departOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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
        ExpertBeGoodAtTreeModel model = (ExpertBeGoodAtTreeModel) o;
        return Objects.equals(id, model.id) &&
                Objects.equals(parentId, model.parentId) &&
                Objects.equals(beGoodAtName, model.beGoodAtName) &&
                Objects.equals(beGoodAtNameEn, model.beGoodAtNameEn) &&
                Objects.equals(beGoodAtNameAbbr, model.beGoodAtNameAbbr) &&
                Objects.equals(departOrder, model.departOrder) &&
                Objects.equals(description, model.description) &&
                Objects.equals(orgCategory, model.orgCategory) &&
                Objects.equals(orgType, model.orgType) &&
                Objects.equals(orgCode, model.orgCode) &&
                Objects.equals(status, model.status) &&
                Objects.equals(delFlag, model.delFlag) &&
                Objects.equals(createBy, model.createBy) &&
                Objects.equals(createTime, model.createTime) &&
                Objects.equals(updateBy, model.updateBy) &&
                Objects.equals(updateTime, model.updateTime) &&
                Objects.equals(children, model.children);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, beGoodAtName, beGoodAtNameEn, beGoodAtNameAbbr,
        		departOrder, description, orgCategory, orgType, orgCode, status, delFlag, createBy, createTime, updateBy, updateTime,
        		children);
    }

}
