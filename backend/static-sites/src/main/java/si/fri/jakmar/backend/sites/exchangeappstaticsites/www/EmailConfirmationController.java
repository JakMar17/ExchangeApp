package si.fri.jakmar.backend.sites.exchangeappstaticsites.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import si.fri.jakmar.backend.sites.exchangeappstaticsites.constants.UrlConstants;

import java.util.HashMap;
import java.util.Map;

@Controller
public class EmailConfirmationController {

    @Autowired
    UrlConstants urlConstants;

    @GetMapping("/email-confirmation")
    public ModelAndView emailConfirmation(@RequestParam String email, @RequestParam String confirmationId) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("confirmation_api_link_base", urlConstants.EMAIL_CONFIRMATION_BASE_URL);
        model.put("confirmation_id", confirmationId);
        model.put("frontend_link", urlConstants.FRONTEND_BASE_URL);
        return new ModelAndView("email-confirmation/confirmation", model);
    }
}
