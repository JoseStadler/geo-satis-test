package ec.com.jasr.geosatisws.modules.offenders.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ec.com.jasr.geosatisws.core.model.entity.BaseId;
import ec.com.jasr.geosatisws.modules.offenders.util.OffenderConstants;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "offender_position", schema = OffenderConstants.SCHEMA)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffenderPosition extends BaseId {

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 5) DEFAULT 0.0")
    private BigDecimal latitude;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 5) DEFAULT 0.0")
    private BigDecimal longitude;

    @JsonBackReference("OffenderOffenderPosition")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, optional = false)
    @JoinColumn(name = "offender", nullable = false, foreignKey = @ForeignKey(name = "offe_posi_offender_offe_fk"))
    private Offender offender;

}
