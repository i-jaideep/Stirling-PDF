package stirling.software.SPDF.controller.web;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Hidden;

import stirling.software.SPDF.model.ApplicationProperties;
import stirling.software.SPDF.model.Dependency;

@Controller
public class HomeWebController {

    @GetMapping("/about")
    @Hidden
    public String gameForm(Model model) {
        model.addAttribute("currentPage", "about");
        return "about";
    }

    @GetMapping("/licenses")
    @Hidden
    public String licensesForm(Model model) {
        model.addAttribute("currentPage", "licenses");
        Resource resource = new ClassPathResource("static/3rdPartyLicenses.json");
        try {
            String json = new String(Files.readAllBytes(resource.getFile().toPath()));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, List<Dependency>> data =
                    mapper.readValue(json, new TypeReference<Map<String, List<Dependency>>>() {});
            model.addAttribute("dependencies", data.get("dependencies"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "licenses";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @GetMapping("/home")
    public String root(Model model) {
        return "redirect:/";
    }

    @Autowired ApplicationProperties applicationProperties;

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    @Hidden
    public String getRobotsTxt() {
        Boolean allowGoogle = applicationProperties.getSystem().getGooglevisibility();
        if (Boolean.TRUE.equals(allowGoogle)) {
            return "User-agent: Googlebot\nAllow: /\n\nUser-agent: *\nAllow: /";
        } else {
            return "User-agent: Googlebot\nDisallow: /\n\nUser-agent: *\nDisallow: /";
        }
    }
}
