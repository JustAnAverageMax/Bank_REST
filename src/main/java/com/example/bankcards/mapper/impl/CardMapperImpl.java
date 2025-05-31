package com.example.bankcards.mapper.impl;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.service.CardCryptoService;
import com.example.dto.CardRs;
import com.example.dto.CardsPageRs;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardMapperImpl implements CardMapper {

  private final CardCryptoService cardCryptoService;

  @Override
  public CardsPageRs toCardsPageRs(@NonNull final Page<Card> page) {
    CardsPageRs rs = new CardsPageRs();

    rs.setContent(page.getContent().stream()
        .map(this::toCardRs)
        .toList());

    rs.setTotalElements(page.getTotalElements());
    rs.setTotalPages(page.getTotalPages());
    rs.setPage(page.getNumber());
    rs.setSize(page.getSize());
    rs.setFirst(page.isFirst());
    rs.setLast(page.isLast());
    rs.setEmpty(page.isEmpty());

    return rs;
  }

  @Override
  public CardRs toCardRs(@NonNull final Card entity) {
    CardRs rs = new CardRs();
    User cardUser = entity.getUser();

    rs.setId(entity.getId());

    final String decryptedCardNumber = cardCryptoService.decrypt(entity.getNumberEncrypted());
    rs.setNumber(cardCryptoService.getMaskedCardNumber(decryptedCardNumber));

    rs.setOwner(cardUser.getFullName());
    rs.setBalance(entity.getBalance().doubleValue());
    rs.setExpirationDate(entity.getExpirationDate());
    rs.setStatus(entity.getStatus());

    return rs;
  }

  @Override
  public Card toCardEntity(@NonNull final User user,
                           @NonNull final String encryptedCardNumber,
                           @NonNull final String cardNumberHash) {

    return Card.builder()
        .numberEncrypted(encryptedCardNumber)
        .numberHash(cardNumberHash)
        .user(user)
        .build();
  }

}
