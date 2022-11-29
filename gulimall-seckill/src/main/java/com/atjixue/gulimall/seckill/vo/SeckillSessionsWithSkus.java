package com.atjixue.gulimall.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author LiuJixue
 * @Date 2022/9/25 22:08
 * @PackageName:com.atjixue.gulimall.seckill.vo
 * @ClassName: SeckillSessionsWithSkus
 * @Description: TODO
 * @Version 1.0
 */
@Data
public class SeckillSessionsWithSkus {
    private Long id;
    /**
     * 场次名称
     */
    private String name;
    /**
     * 每日开始时间
     */
    private Date startTime;
    /**
     * 每日结束时间
     */
    private Date endTime;
    /**
     * 启用状态
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;

    private List<SeckillSkuVo> relationSkus;
}
