package com.atjixue.common.exception;

/**
 * @Author LiuJixue
 * @Date 2022/9/12 23:05
 * @PackageName:com.atjixue.gulimall.ware.exception
 * @ClassName: NoStockException
 * @Description: TODO
 * @Version 1.0
 */

public class NoStockException extends RuntimeException{
    private Long SkuId;
    public NoStockException(Long SkuId){
        super("商品ID：" +  SkuId + "：没有足够的库存了");
    }

    public Long getSkuId() {
        return SkuId;
    }

    public void setSkuId(Long skuId) {
        this.SkuId = skuId;
    }

    public NoStockException(String msg){
        super(msg + "没有足够的库存了");
    }
}
