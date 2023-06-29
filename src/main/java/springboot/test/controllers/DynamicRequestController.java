package springboot.test.controllers;


import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.models.Condition;
import springboot.test.models.Request;
import springboot.test.models.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    public void Postrequest(@Validated @RequestBody Request request, HttpServletResponse response) throws IOException {

        String columns = String.join(",", request.getColumns());
        String tables = String.join(",", request.getTables());

        String query = "SELECT " + columns + " FROM " + tables;

        if (request.getWhere() != null && !request.getWhere().isEmpty()) {
            query += " WHERE";
            int size = request.getWhere().size();
            int counter = 1;

            for (Condition condition : request.getWhere()) {
                query += " " + condition.getColumn() + " " + condition.getOperation() + " " + condition.getValue() + "";

                if (size > counter)
                {
                    query += " AND";
                    counter += 1;
                }

            }

        }

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=result.csv");

        PrintWriter writer = response.getWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        // Écriture des en-têtes CSV
        if (!result.isEmpty()) {
            Map<String, Object> firstRow = result.get(0);
            for (String column : firstRow.keySet()) {
                csvPrinter.print(column);
            }
            csvPrinter.println();
        }

        // Écriture des données CSV
        for (Map<String, Object> row : result) {
            for (Object value : row.values()) {
                csvPrinter.print(value);
            }
            csvPrinter.println();
        }

        csvPrinter.flush();
        csvPrinter.close();

        //System.out.println(query);

        //List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        //return ResponseEntity.ok(result);
    }


}
