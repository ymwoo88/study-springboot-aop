package hello.aop.example;

import hello.aop.example.annotation.ExceptionIfNull;
import hello.aop.example.annotation.ReturnIfNull;
import hello.aop.example.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NullAnnotationService {

    private final NullAnnotationRepository nullAnnotationRepository;

    public String returnIfNullTest(@ReturnIfNull String itemId) {
        return nullAnnotationRepository.save(itemId);
    }
    public String exceptionIfNullTest(@ExceptionIfNull String itemId) {
        return nullAnnotationRepository.save(itemId);
    }
}
