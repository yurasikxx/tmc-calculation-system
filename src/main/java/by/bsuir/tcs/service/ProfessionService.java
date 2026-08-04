package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.repository.NormRepository;
import by.bsuir.tcs.repository.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionService {

    private final ProfessionRepository professionRepository;
    private final NormRepository normRepository;

    @Transactional(readOnly = true)
    public List<Profession> findAll() {
        return professionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Profession findById(Long id) {
        return professionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profession not found with id: " + id));
    }

    @Transactional
    public Profession create(Profession profession) {
        if (professionRepository.existsByName(profession.getName())) {
            throw new RuntimeException("Profession with name '" + profession.getName() + "' already exists");
        }
        return professionRepository.save(profession);
    }

    @Transactional
    public Profession update(Long id, Profession updatedProfession) {
        Profession existing = findById(id);
        if (!existing.getName().equals(updatedProfession.getName())
                && professionRepository.existsByName(updatedProfession.getName())) {
            throw new RuntimeException("Profession with name '" + updatedProfession.getName() + "' already exists");
        }
        existing.setName(updatedProfession.getName());
        return professionRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Profession profession = findById(id);
        if (!normRepository.findByProfessionId(id).isEmpty()) {
            throw new RuntimeException("Cannot delete profession with id " + id + " because it has associated norms");
        }
        professionRepository.delete(profession);
    }
}