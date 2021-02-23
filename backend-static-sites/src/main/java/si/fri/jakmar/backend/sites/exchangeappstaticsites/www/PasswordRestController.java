package si.fri.jakmar.backend.sites.exchangeappstaticsites.www;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PasswordRestController {

    @GetMapping("/password-reset")
    public ModelAndView restPassword(@RequestParam String passwordResetId) {
        Map<String, Object> model = new HashMap<>();
        model.put("passwordResetId", passwordResetId);
        return new ModelAndView("password-reset/password", model);
    }
}
