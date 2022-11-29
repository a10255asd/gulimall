package com.atjixue.gulimall.product.vo;

import com.atjixue.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/8/11 23:07
 * @PackageName:com.atjixue.gulimall.product.vo
 * @ClassName: AttrGroupWithAttrsVo
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    @TableId
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 完整路径
     * */
    @TableField(exist = false)
    private Long[] catelogPath;

    private List<AttrEntity> attrs;
}
