package com.review.manage.question.vo;

import com.review.front.frontReviewClass.vo.SelectVO;
import com.review.manage.question.entity.ReviewAnswerEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionVO {

    private Integer questionId;   	//题目ID

    private String isImportant;  	//是否为重要题目

    private String isRight;		    //是否为正确答案

    private String rightAnswer;     //正确答案

    private String questionType;   	//题目类型

    private String classId;         //分类ID

    private String nextClassId;    //下个量表id

    private Long projectId;    //关联项目ID

    private String answerId;

    private String variateName;     //因子名称

    private String variateId;       //变量因子ID

    private String resultId;

    private String reviewType;		//测评类型

    private String content;   		//题目内容

    private Integer questionNum;   	//题目序号

    private String createTime;   	//创建时间

    private String createBy; 		//创建人

    private String selCode;

    private String selectContent;

    private String selectGrade;

    private String isAttach;

    private String pictureAttach;

    private String isLastQuestion;

    private String multiple;     //可多选

    private List<SelectVO> selectList = new ArrayList<SelectVO>();

    private List<ReviewAnswerEntity> answerList = new ArrayList<ReviewAnswerEntity>();
}
