package ec.com.jasr.geosatisws.modules.offenders.model.dto;

import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OffenderDTO {

    private Long id;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public OffenderDTO(Offender offender) {
        this.id = offender.getId();
        this.latitude = offender.getPosition().getLatitude();
        this.longitude = offender.getPosition().getLongitude();
    }
}
