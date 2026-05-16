package it.aulab.progetto_finale_michele_macis.services;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CrudService<ReadDto, Model, Key> {
    List<ReadDto> readAll();
    ReadDto read(Key key);
    ReadDto create(Model model, MultipartFile file, Principal principal);
    ReadDto update(Key key, Model model, MultipartFile file);
    void delete(Key key);

}
