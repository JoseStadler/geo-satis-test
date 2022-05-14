package ec.com.jasr.geosatisws.modules.offenders.controller;

import ec.com.jasr.geosatisws.modules.offenders.model.entity.Offender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class OffendersController {

    @MessageMapping("/hello")
    @SendTo("/ws-resp/greetings")
    public Offender greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        Offender of = new Offender();
        of.setFirstName("Prueba");
        of.setLastName("ST");
        return of;
    }
}
