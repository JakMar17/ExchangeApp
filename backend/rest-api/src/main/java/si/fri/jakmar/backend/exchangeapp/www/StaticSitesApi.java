package si.fri.jakmar.backend.exchangeapp.www;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/static/")
@RequiredArgsConstructor
public class StaticSitesApi {

    private final StaticSitesConstants staticSitesConstants;

    @GetMapping("email-confirmation")
    public ModelAndView emailConfirmation(@RequestParam String email, @RequestParam String confirmationId) {
        Map<String, Object> model = new HashMap<>();
        model.put("email", email);
        model.put("confirmation_api_link_base", staticSitesConstants.EMAIL_CONFIRMATION_BASE_URL);
        model.put("confirmation_id", confirmationId);
        model.put("frontend_link", staticSitesConstants.FRONTEND_BASE_URL);
        return new ModelAndView("email-confirmation/confirmation", model);
    }

    @GetMapping("password-reset")
    public ModelAndView resetPassword(@RequestParam String passwordResetId, @RequestParam String email) {
        Map<String, Object> model = new HashMap<>();
        model.put("password_reset_id", passwordResetId);
        model.put("email", email);
        model.put("frontend_link", staticSitesConstants.FRONTEND_BASE_URL);
        model.put("api_url", staticSitesConstants.PASSWORD_RESET_BASE_URL);
        return new ModelAndView("password-reset/password", model);
    }
}
