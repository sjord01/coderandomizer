package coderandomizer.controller;

import coderandomizer.model.CodeEntity;
import coderandomizer.service.CodeService;
import com.opencsv.CSVWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.List;

@RestController
@RequestMapping("/api/codes")
public class CodeController {
    private final CodeService service;

    public CodeController(CodeService service) { this.service = service; }

    @PostMapping("/generate")
    public CodeEntity generate() {
        return service.generateAndSave();
    }

    @PostMapping("/{id}/test")
    public ResponseEntity<?> test(@PathVariable Long id) {
        boolean good = service.testCode(id);
        if (good) {
            return ResponseEntity.ok().body(
                    new MessageResponse("The code is GOOD. The code has been saved to the database")
            );
        } else {
            return ResponseEntity.ok().body(
                    new MessageResponse("The code is BAD")
            );
        }
    }

    @PostMapping("/{id}/cleanup")
    public ResponseEntity<?> cleanup(@PathVariable Long id) {
        CodeEntity updated = service.cleanup(id);
        return ResponseEntity.ok().body(new MessageResponse("The code has been cleaned up and saved to the database"));
    }

    @GetMapping
    public List<CodeEntity> list() {
        return service.listAll();
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportCsv() {
        List<CodeEntity> all = service.listAll();
        StringWriter sw = new StringWriter();
        CSVWriter csv = new CSVWriter(sw);
        csv.writeNext(new String[] {"id","code","cleaned","createdAt"});
        for (CodeEntity e : all) {
            csv.writeNext(new String[] {
                    String.valueOf(e.getId()),
                    e.getCode(),
                    String.valueOf(e.getCleaned()),
                    e.getCreatedAt().toString()
            });
        }
        try { csv.close(); } catch (Exception ignored) {}
        String filename = "codes_export.csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(sw.toString());
    }

    // simple wrapper for messages
    public static record MessageResponse(String message) {}
}
