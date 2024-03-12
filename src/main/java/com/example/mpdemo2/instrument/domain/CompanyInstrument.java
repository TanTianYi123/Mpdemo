package com.example.mpdemo2.instrument.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName company_instrument
 */
@TableName(value ="company_instrument")
@Data
public class CompanyInstrument implements Serializable {
    /**
     * 
     */
    @TableId
    private String instrumentId;

    /**
     * 
     */
    private String instrumentName;

    /**
     * 
     */
    private String instrumentDescription;

    /**
     * 
     */
    private Integer companyId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CompanyInstrument other = (CompanyInstrument) that;
        return (this.getInstrumentId() == null ? other.getInstrumentId() == null : this.getInstrumentId().equals(other.getInstrumentId()))
            && (this.getInstrumentName() == null ? other.getInstrumentName() == null : this.getInstrumentName().equals(other.getInstrumentName()))
            && (this.getInstrumentDescription() == null ? other.getInstrumentDescription() == null : this.getInstrumentDescription().equals(other.getInstrumentDescription()))
            && (this.getCompanyId() == null ? other.getCompanyId() == null : this.getCompanyId().equals(other.getCompanyId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getInstrumentId() == null) ? 0 : getInstrumentId().hashCode());
        result = prime * result + ((getInstrumentName() == null) ? 0 : getInstrumentName().hashCode());
        result = prime * result + ((getInstrumentDescription() == null) ? 0 : getInstrumentDescription().hashCode());
        result = prime * result + ((getCompanyId() == null) ? 0 : getCompanyId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", instrumentId=").append(instrumentId);
        sb.append(", instrumentName=").append(instrumentName);
        sb.append(", instrumentDescription=").append(instrumentDescription);
        sb.append(", companyId=").append(companyId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}