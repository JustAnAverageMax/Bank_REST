package com.example.bankcards.service.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardAlreadyExistsException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardCryptoService;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.ExceptionMessage;
import com.example.dto.CardCreationRq;
import com.example.dto.CardRs;
import com.example.dto.CardsPageRs;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final UserRepository userRepository;

  private final CardMapper cardMapper;

  private final CardCryptoService cardCryptoService;

  @Override
  @Transactional
  public CardRs saveCard(@NonNull final CardCreationRq rq) {
    User user = userRepository.findById(rq.getUserId()).orElseThrow(() -> {
      log.error("User with id '{}' not found", rq.getUserId());
      return new EntityNotFoundException(
          ExceptionMessage.ENTITY_NOT_FOUND.format(User.class.getSimpleName()));
    });

    final String cardNumberHash = cardCryptoService.hash(rq.getNumber());
    final String maskedCardNumber = cardCryptoService.getMaskedCardNumber(rq.getNumber());

    if (cardRepository.existsByUserIdAndNumberHash(rq.getUserId(), cardNumberHash)) {
      log.error("User with id '{}' already has card with number '{}'", rq.getUserId(), maskedCardNumber);

      throw new CardAlreadyExistsException(ExceptionMessage.CARD_ALREADY_EXISTS.getMessage());
    }

    final String encryptedCardNumber = cardCryptoService.encrypt(rq.getNumber());
    Card card = cardMapper.toCardEntity(user, encryptedCardNumber, cardNumberHash);

    cardRepository.save(card);
    log.info("Card with number '{}' successfully saved", maskedCardNumber);

    return cardMapper.toCardRs(card);
  }

  @Override
  @Transactional
  public void deleteCard(@NonNull final Long id) {

    if (!cardRepository.existsById(id)) {
      log.error("No card found by id '{}'", id);
      throw new EntityNotFoundException();
    }

    cardRepository.deleteById(id);
    log.info("Card with id '{}' successfully deleted", id);
  }

  @Override
  public CardsPageRs findAllCards(@NonNull final Integer page, @NonNull final Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Card> cards = cardRepository.findAll(pageable);
    return cardMapper.toCardsPageRs(cards);
  }

  @Override
  public CardsPageRs finaAllUserCards(@NonNull final String email,
                                      @NonNull final Integer page,
                                      @NonNull final Integer size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Card> cards = cardRepository.findByUserEmail(email, pageable);

    return cardMapper.toCardsPageRs(cards);
  }

  @Override
  public CardRs findCard(Long id) {
    return null;
  }
}
