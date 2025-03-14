package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestGetDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestGetDto saveRequest(RequestCreateDto requestCreateDto, long userId) {
        User user = userRepository.getUserById(userId);
        Request request = requestMapper.createToRequestDto(requestCreateDto);
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);

        return requestMapper.requestToGetDto(requestRepository.save(request));
    }
}