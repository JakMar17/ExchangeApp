package si.fri.jakmar.backend.sites.exchangeappstaticsites.www;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailConfirmationController {

    @GetMapping("/email-confirmation")
    public ModelAndView emailConfirmation(@RequestParam String email, @RequestParam String confirmationId) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("confirmationId", confirmationId);
        return new ModelAndView("email-confirmation/confirmation", model);
    }
}
