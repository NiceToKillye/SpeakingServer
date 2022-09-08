package loader.service;

import loader.entity.Exam;
import loader.entity.AudioFile;
import loader.repository.ExamRepository;
import loader.repository.AudioFileRepository;

import java.io.File;
import java.util.Optional;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeakingService {

    ExamRepository examRepository;
    AudioFileRepository audioFileRepository;

    public SpeakingService(ExamRepository examRepository,
                           AudioFileRepository audioFileRepository)
    {
        this.examRepository = examRepository;
        this.audioFileRepository = audioFileRepository;
    }

    public void saveAudio(MultipartFile audio, String examName) throws IOException, EncoderException {

        Optional<Exam> examOptional = examRepository.findExamByExamName(examName);
        if(examOptional.isEmpty()){
            throw new NullPointerException("Exam with username: " + examName + " not found");
        }

        Exam exam = examOptional.get();

        String audioName = audio.getOriginalFilename();
        String audioFileWavPath = System.getProperty("user.dir") + "/" + exam.getPackageLink() + "/" + audioName + ".wav";
        String audioFileMp3Path = System.getProperty("user.dir") + "/" + exam.getPackageLink() + "/" + audioName + ".mp3";

        int counter = 1;
        while (Files.exists(Path.of(audioFileWavPath)) || Files.exists(Path.of(audioFileMp3Path))){
            assert audioName != null;
            audioFileWavPath = audioFileWavPath.replace(audioName, audioName + "_" + counter);
            audioFileMp3Path = audioFileMp3Path.replace(audioName, audioName + "_" + counter);
            counter++;
        }

        boolean wavCreated = new File(audioFileWavPath).createNewFile();
        boolean mp3Created = new File(audioFileMp3Path).createNewFile();
        if(!wavCreated || !mp3Created){
            throw new NullPointerException("No such file in saveAudio");
        }

        File audioFileWav = new File(audioFileWavPath);
        File audioFileMp3 = new File(audioFileMp3Path);
        audio.transferTo(audioFileWav);

        Encoder encoder = new Encoder();
        EncodingAttributes attrs = new EncodingAttributes();
        AudioAttributes audioAttributes = new AudioAttributes();

        audioAttributes.setChannels(2);
        audioAttributes.setBitRate(128000);
        audioAttributes.setSamplingRate(44100);
        audioAttributes.setCodec("libmp3lame");

        attrs.setAudioAttributes(audioAttributes);
        encoder.encode(new MultimediaObject(audioFileWav), audioFileMp3, attrs);

        AudioFile audioFile = new AudioFile(audioName, audioFileMp3Path, exam);

        audioFileRepository.save(audioFile);
        Files.delete(Path.of(audioFileWavPath));
    }
}