package hello.aop.example;

import org.springframework.stereotype.Repository;

@Repository
public class NullAnnotationRepository {

    public String save(String itemId) {
        return "[OK]" + itemId;
    }
}
