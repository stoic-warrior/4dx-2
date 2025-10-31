package Focus._dx.controller;

import Focus._dx.domain.Wig;
import Focus._dx.repository.WigRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wigs")
@RequiredArgsConstructor
public class WigController {
    private final WigRepository repo;

    @PostMapping
    public Wig create(@RequestBody @Valid Wig w) {return repo.save(w);}

    @GetMapping
    public List<Wig> list() { return repo.findAll(); }
}
