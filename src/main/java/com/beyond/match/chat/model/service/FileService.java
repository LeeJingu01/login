package com.beyond.match.chat.model.service;

import com.beyond.match.chat.model.repository.ChatMessageRepository;
import com.beyond.match.chat.model.repository.FileRepository;
import com.beyond.match.chat.model.vo.DmFile;
import com.beyond.match.chat.model.vo.Message;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final String uploadDir = "src/main/resources/static/uploads";
    public DmFile saveFile(int messageId, MultipartFile file) throws IOException {
        Message message = chatMessageRepository.findById(messageId).orElseThrow(()->
                new EntityNotFoundException("메시지를 찾을 수 없습니다."));

        String folderPath = uploadDir + "/" + LocalDate.now().getYear() + "/" +
                String.format("%02d", LocalDate.now().getMonthValue());
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String filePath = folderPath + "/" + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        DmFile dmFile = DmFile.builder()
                .fileUrl(filePath)
                .fileType(file.getContentType())
                .build();
        return fileRepository.save(dmFile);
    }
}
