package com.example.bankcards.mapper;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.dto.CardRs;
import com.example.dto.CardsPageRs;
import org.springframework.data.domain.Page;

public interface CardMapper {

  CardsPageRs toCardsPageRs(Page<Card> page);

  CardRs toCardRs(Card entity);

  Card toCardEntity(User user, String encryptedCardNumber, String cardNumberHash);

}
