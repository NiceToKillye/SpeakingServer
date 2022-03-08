package loader.service;

import loader.repository.ExamRepository;
import org.springframework.stereotype.Service;

@Service
public class ExamService {
    ExamRepository examRepository;
    public ExamService(ExamRepository examRepository){
        this.examRepository = examRepository;
    }
}