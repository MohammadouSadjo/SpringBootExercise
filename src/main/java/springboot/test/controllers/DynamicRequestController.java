package springboot.test.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.models.Condition;
import springboot.test.models.Request;
import springboot.test.models.User;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/dynamicrequest")
//@CrossOrigin(origins = "*")
public class DynamicRequestController {

    @Autowired
    //private DynamicRequestRepository dynamicRequestRepository;
    private JdbcTemplate jdbcTemplate;

    @PostMapping()
    public ResponseEntity<?> Postrequest(@Validated @RequestBody Request request) {

        String columns = String.join(",", request.getColumns());
        String tables = String.join(",", request.getTables());

        String query = "SELECT " + columns + " FROM " + tables;

        if (request.getWhere() != null && !request.getWhere().isEmpty()) {
            query += " WHERE";

            for (Condition condition : request.getWhere()) {
                query += " " + condition.getColumn() + " " + condition.getOperation() + " '" + condition.getValue() + "'";
            }

        }

        //List<User> result = dynamicRequestRepository.executeQuery(query);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        //jdbcTemplate.execute(query);

        return ResponseEntity.ok(result);
    }


}