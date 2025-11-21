package coderandomizer.service;

import coderandomizer.model.CodeEntity;
import coderandomizer.repository.CodeRepository;
import coderandomizer.util.RandomCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeService {
    private final CodeRepository repo;

    public CodeService(CodeRepository repo) { this.repo = repo; }

    public CodeEntity generateAndSave() {
        String code = RandomCodeGenerator.generate();
        CodeEntity e = new CodeEntity(code);
        e.setCleaned(RandomCodeGenerator.isGood(code));
        return repo.save(e);
    }

    public Optional<CodeEntity> findById(Long id) {
        return repo.findById(id);
    }

    public List<CodeEntity> listAll() {
        return repo.findAll();
    }

    public boolean testCode(Long id) {
        Optional<CodeEntity> opt = repo.findById(id);
        if (opt.isEmpty()) throw new IllegalArgumentException("Not found");
        return RandomCodeGenerator.isGood(opt.get().getCode());
    }

    public CodeEntity cleanup(Long id) {
        CodeEntity e = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        String cleaned = RandomCodeGenerator.cleanup(e.getCode());
        e.setCode(cleaned);
        e.setCleaned(true);
        return repo.save(e);
    }
}
