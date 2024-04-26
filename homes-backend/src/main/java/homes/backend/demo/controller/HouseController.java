package homes.backend.demo;

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
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/house")
public class HouseController {
    Logger logger = LoggerFactory.getLogger(HouseController.class);

    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity houseAll() {
        Map<String, Object> response = new HashMap<>();
        try {
            Path path = Paths.get("data/db.json");
            String json = Files.readString(path);
            JsonParser jsp = JsonParserFactory.getJsonParser();
            response = jsp.parseMap(json);
            return new ResponseEntity<>(response.get("locations"), HttpStatus.OK);
        }
        catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/one")
    @ResponseBody
    public ResponseEntity houseOne(
        @RequestParam("id") Long id
    ) {
        Map<String, String> response = new HashMap<>();
        try {
            Path path = Paths.get("data/db.json");
            String json = Files.readString(path);
            JsonParser jsp = JsonParserFactory.getJsonParser();
            Map<String, Object> mjson = jsp.parseMap(json);
            List<Map> housesList = (java.util.ArrayList<Map>) mjson.get("locations");
            for (int i = 0; i < housesList.size(); ++i)
            {
                Map item = housesList.get(i);
                if (item.get("id").toString().equals(id.toString()))
                {
                    return new ResponseEntity<>(item, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
