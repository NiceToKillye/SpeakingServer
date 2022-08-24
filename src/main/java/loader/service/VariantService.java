package loader.service;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.nio.file.Paths;

import loader.custom.ConfigProperties;
import loader.custom.VariantForm;
import loader.entity.Variant;
import loader.exception.VariantNameExists;
import loader.repository.VariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

@Service
public class VariantService {

    ConfigProperties configProperties;
    VariantRepository variantRepository;

    public VariantService(VariantRepository variantRepository, ConfigProperties configProperties){
        this.configProperties = configProperties;
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

    public void createVariant(VariantForm variantForm, long userId) throws IOException, VariantNameExists {

        if(variantRepository.existsByVariantName(variantForm.getVariantName())){
            throw new VariantNameExists();
        }

        String packageLink = configProperties.getVariantStorage() + "/" + (int) variantRepository.count();
        Files.createDirectories(Paths.get(packageLink));

        String photo1Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFile2().getOriginalFilename();
        String audioPath = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFile3().getOriginalFilename();
        String photo2Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFiles4()[0].getOriginalFilename();
        String photo3Path = System.getProperty("user.dir") + "/" + packageLink + "/" + variantForm.getTaskFiles4()[1].getOriginalFilename();

        File photo1 = new File(photo1Path);
        File audio = new File(audioPath);
        File photo2 = new File(photo2Path);
        File photo3 = new File(photo3Path);

        variantForm.getTaskFile2().transferTo(photo1);
        variantForm.getTaskFile3().transferTo(audio);
        variantForm.getTaskFiles4()[0].transferTo(photo2);
        variantForm.getTaskFiles4()[1].transferTo(photo3);

        Variant variant = new Variant(
                variantForm.getTaskText1(),
                variantForm.getTaskText2(),
                variantForm.getTaskText3(),
                variantForm.getTaskText4(),
                audioPath,
                photo1Path,
                photo2Path,
                photo3Path,
                variantForm.getVariantName(),
                userId,
                variantForm.getVariantLanguage());

        variantRepository.save(variant);
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