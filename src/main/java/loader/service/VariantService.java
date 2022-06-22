package loader.service;

import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.nio.file.Paths;

import loader.entity.Variant;
import loader.repository.VariantRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class VariantService {

    VariantRepository variantRepository;
    public VariantService(VariantRepository variantRepository){
        this.variantRepository = variantRepository;
    }

    public List<Variant> getAllVariants(){
        return variantRepository.findAll();
    }

    public Variant getVariantById(Long id){
        if(id == null){
            throw new NullPointerException("Id is null");
        }
        Optional<Variant> variant = variantRepository.findById(id);
        if(variant.isPresent()){
            return variant.get();
        }
        else{
            throw new NullPointerException("Variant with this id = " + id + " does not exist");
        }
    }

    public void deleteVariant(Long variantId) {
        Optional<Variant> variantOptional = variantRepository.findById(variantId);

        if(variantOptional.isPresent()){

            String path = variantOptional.get().getPhotoLink1();
            path = path.substring(0, path.lastIndexOf("/"));

            try {
                FileSystemUtils.deleteRecursively(Paths.get(path));
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            variantRepository.deleteById(variantId);
        }
    }

    public void deleteVariants(List<Long> variantId){
        variantId.forEach(this::deleteVariant);
    }
}