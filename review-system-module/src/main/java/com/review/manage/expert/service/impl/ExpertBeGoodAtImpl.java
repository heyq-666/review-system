package com.review.manage.expert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.manage.expert.entity.ExpertBeGoodAt;
import com.review.manage.expert.mapper.ExpertBeGoodAtMapper;
import com.review.manage.expert.model.ExpertBeGoodAtTreeModel;
import com.review.manage.expert.service.IExpertBeGoodAtService;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author javabage
 * @date 2023/5/17
 */
@Service
public class ExpertBeGoodAtImpl extends ServiceImpl<ExpertBeGoodAtMapper, ExpertBeGoodAt> implements IExpertBeGoodAtService {
    @Override
    public List<ExpertBeGoodAtTreeModel> queryTreeListByPid(String parentId,String ids, String primaryKey) {
        Consumer<LambdaQueryWrapper<ExpertBeGoodAt>> square = i -> {
            if (oConvertUtils.isNotEmpty(ids)) {
                if (CommonConstant.DEPART_KEY_ORG_CODE.equals(primaryKey)) {
                    i.in(ExpertBeGoodAt::getOrgCode, ids.split(SymbolConstant.COMMA));
                } else {
                    i.in(ExpertBeGoodAt::getId, ids.split(SymbolConstant.COMMA));
                }
            } else {
                if(oConvertUtils.isEmpty(parentId)){
                    i.and(q->q.isNull(true,ExpertBeGoodAt::getParentId).or().eq(true,ExpertBeGoodAt::getParentId,""));
                }else{
                    i.eq(true,ExpertBeGoodAt::getParentId,parentId);
                }
            }
        };
        LambdaQueryWrapper<ExpertBeGoodAt> lqw=new LambdaQueryWrapper<>();
        lqw.eq(true,ExpertBeGoodAt::getDelFlag,CommonConstant.DEL_FLAG_0.toString());
        lqw.func(square);
        lqw.orderByAsc(ExpertBeGoodAt::getDepartOrder);
        List<ExpertBeGoodAt> list = list(lqw);
        //设置用户id,让前台显示
        //this.setUserIdsByDepList(list);
        List<ExpertBeGoodAtTreeModel> records = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ExpertBeGoodAt depart = list.get(i);
            ExpertBeGoodAtTreeModel treeModel = new ExpertBeGoodAtTreeModel(depart);
            //TODO 异步树加载key拼接__+时间戳,以便于每次展开节点会刷新数据
            treeModel.setKey(treeModel.getKey());
            Integer count=this.baseMapper.queryCountByPid(depart.getId());
            if(count>0){
                treeModel.setIsLeaf(false);
            }else{
                treeModel.setIsLeaf(true);
            }
            records.add(treeModel);
        }
        return records;
    }
}
