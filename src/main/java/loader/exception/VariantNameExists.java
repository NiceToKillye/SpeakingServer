package loader.exception;

public class VariantNameExists extends Exception{
    public VariantNameExists(){
        super("Variant with this name is already exists");
    }
}