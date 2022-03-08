package loader.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Counter {

    int value = 0;

    public int incrementAndGet(){
        return ++value;
    }

}
