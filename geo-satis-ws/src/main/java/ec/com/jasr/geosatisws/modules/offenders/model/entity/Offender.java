package ec.com.jasr.geosatisws.modules.offenders.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import ec.com.jasr.geosatisws.core.model.entity.BaseId;
import ec.com.jasr.geosatisws.modules.offenders.util.OffenderConstants;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "offender", schema = OffenderConstants.SCHEMA)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Offender extends BaseId {

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column
    private String picture;

    @Column(name="birth_date")
    private String birthDate;

    @JsonManagedReference("OffenderOffenderPosition")
    @OneToOne(mappedBy = "offender", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    protected OffenderPosition position;



}
