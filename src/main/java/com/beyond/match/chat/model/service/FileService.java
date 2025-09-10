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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final ChatMessageRepository chatMessageRepository;
    public DmFile saveFile(int messageId, MultipartFile file) throws IOException {
        Message message = chatMessageRepository.findById(messageId).orElseThrow(()->
                new EntityNotFoundException("메시지를 찾을 수 없습니다."));

        DmFile dmFile = DmFile.builder()
                .fileData(file.getBytes())
                .message(message)
                .fileType(file.getContentType())
                .fileName(file.getOriginalFilename())
                .build();
        return fileRepository.save(dmFile);
    }

    public DmFile getFile(int fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(()-> new EntityNotFoundException("파일을 찾을 수 없습니다."));
    }
}
