package homes.backend.demo;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/apply")
public class ApplyController {
    Logger logger = LoggerFactory.getLogger(ApplyController.class);

    @PostMapping
    @ResponseBody
    public ResponseEntity registerApply(
        @RequestParam("first-name") String first_name,
        @RequestParam("last-name") String last_name,
        @RequestParam("email") String email,
        @RequestParam("date") String date,
        @RequestParam("id") Long id
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Path path = Paths.get("data/applies.json");
            String json = Files.readString(path);
            JsonParser jsp = JsonParserFactory.getJsonParser();
            Map<String, Object> mjson = jsp.parseMap(json);
            List<Map> appliesList = (java.util.ArrayList<Map>) mjson.get("applies");
            Map item = new HashMap<>();
            item.put("firstName", first_name);
            item.put("lastName", last_name);
            item.put("email", email);
            item.put("date", date);
            item.put("id", id);
            appliesList.add(item);
            Map<String, Object> newJson = new HashMap<>();
            newJson.put("applies", appliesList);
            ObjectMapper objectMapper = new ObjectMapper();
            String strJson = objectMapper.writeValueAsString(newJson);
            byte[] bytes = strJson.getBytes();
            Files.write(path, bytes);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/last")
    @ResponseBody
    public ResponseEntity lastApply(
        @RequestParam("id") Long id
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Path path = Paths.get("data/applies.json");
            String json = Files.readString(path);
            JsonParser jsp = JsonParserFactory.getJsonParser();
            Map<String, Object> mjson = jsp.parseMap(json);
            List<Map> appliesList = (java.util.ArrayList<Map>) mjson.get("applies");
            Map item = null;
            String date = null;
            for (int i = 0; i < appliesList.size(); ++i)
            {
                Map itemI = appliesList.get(i);
                String dateI = (String) itemI.get("date");
                if (itemI.get("id").toString().equals(id.toString()))
                {
                    if (date == null || date.compareTo(dateI) < 0)
                    {
                        date = dateI;
                        item = itemI;
                    }
                }
            }
            if (item == null)
            {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(item, HttpStatus.OK);
        }
        catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
