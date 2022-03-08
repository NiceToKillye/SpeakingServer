package loader.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeakingService {

    public void saveAudio(MultipartFile audio){
        /*
        File audioFileWav = new File(""); audio.getOriginalFilename() - to get name
        File audioFileMp3 = new File("");

        audio.transferTo(file);

        AudioAttributes audioAttributes = new AudioAttributes();
        EncodingAttributes attrs = new EncodingAttributes();
        Encoder encoder = new Encoder();

        audioAttributes.setCodec("libmp3lame");
        audioAttributes.setBitRate(128000);
        audioAttributes.setChannels(2);
        audioAttributes.setSamplingRate(44100);

        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audioAttributes);

        encoder.encode(file, file2, attrs);

        TODO: save mp3 to dir
        */

    }
}
