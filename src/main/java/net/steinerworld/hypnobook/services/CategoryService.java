package net.steinerworld.hypnobook.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.steinerworld.hypnobook.domain.Category;
import net.steinerworld.hypnobook.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService {
   private final CategoryRepository kategorieRepo;

   public List<Category> findAll() {
      return kategorieRepo.findAll();
   }

   public void save(Category kat) {
      kategorieRepo.save(kat);
   }
}
