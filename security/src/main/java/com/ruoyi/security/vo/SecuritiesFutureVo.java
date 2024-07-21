package com.ruoyi.security.vo;

import com.ruoyi.common.core.domain.BaseEntity;
public class SecuritiesFutureVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 交易所-编码 */
    private String exchangeCode;

    /** 当前价格 */
    private Double price;

    /** 一日差价 */
    private String proportion;

    /** 一日均价 */
    private Double dailySpread;

    /** 当前偏离值（比较上下偏离后得到） */
    private Double deviation;

    /** 波动 */
    private Double undulate;

    /** 提示状态 */
    private Integer positiveNegativeFlag;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Double getDailySpread() {
        return dailySpread;
    }

    public void setDailySpread(Double dailySpread) {
        this.dailySpread = dailySpread;
    }


    public Double getUndulate() {
        return undulate;
    }

    public void setUndulate(Double undulate) {
        this.undulate = undulate;
    }


    public Integer getPositiveNegativeFlag() {
        return positiveNegativeFlag;
    }

    public void setPositiveNegativeFlag(Integer positiveNegativeFlag) {
        this.positiveNegativeFlag = positiveNegativeFlag;
    }

    public Double getDeviation() {
        return deviation;
    }

    public void setDeviation(Double deviation) {
        this.deviation = deviation;
    }

    @Override
    public String toString() {
        return "SecuritiesFutureVo{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", exchangeCode='" + exchangeCode + '\'' +
                ", price=" + price +
                ", proportion='" + proportion + '\'' +
                ", dailySpread=" + dailySpread +
                ", deviation=" + deviation +
                ", undulate=" + undulate +
                ", positiveNegativeFlag=" + positiveNegativeFlag +
                '}';
    }
}
