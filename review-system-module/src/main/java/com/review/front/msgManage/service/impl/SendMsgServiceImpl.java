package com.review.front.msgManage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.review.front.msgManage.mapper.SendMsgMapper;
import com.review.front.msgManage.service.ISendMsgService;
import com.review.manage.userManage.entity.ReviewUser;
import org.springframework.stereotype.Service;

/**
 * @author javabage
 * @date 2023/4/6
 */
@Service
public class SendMsgServiceImpl extends ServiceImpl<SendMsgMapper, ReviewUser> implements ISendMsgService {
}
