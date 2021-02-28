package si.fri.jakmar.backend.sites.exchangeappstaticsites.www;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import si.fri.jakmar.backend.sites.exchangeappstaticsites.constants.UrlConstants;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PasswordRestController {

    @Autowired
    UrlConstants urlConstants;

    @GetMapping("/password-reset")
    public ModelAndView resetPassword(@RequestParam String passwordResetId, @RequestParam String email) {
        Map<String, Object> model = new HashMap<>();
        model.put("password_reset_id", passwordResetId);
        model.put("email", email);
        model.put("frontend_link", urlConstants.FRONTEND_BASE_URL);
        model.put("api_url", urlConstants.PASSWORD_RESET_BASE_URL);
        return new ModelAndView("password-reset/password", model);
    }
}
