package loader.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class VariantForm {

    private String taskText1;
    private String taskText2;
    private String taskText3;
    private String taskText4;
    private String variantName;
    private MultipartFile taskFile2;
    private MultipartFile taskFile3;
    private MultipartFile[] taskFiles4;
}
