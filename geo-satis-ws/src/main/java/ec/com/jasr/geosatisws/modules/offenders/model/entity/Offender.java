package ec.com.jasr.geosatisws.modules.offenders.model.entity;

import lombok.Data;

@Data
public class Offender {

    private Long id;
    private String firstName;
    private String lastName;
    private String picture;
    private String birthDate;
//    position: { latitude: number; longitude: number };
}
