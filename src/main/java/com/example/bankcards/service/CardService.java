package com.example.bankcards.service;

import com.example.dto.CardCreationRq;
import com.example.dto.CardRs;
import com.example.dto.CardsPageRs;

public interface CardService {

  CardRs saveCard(CardCreationRq rq);

  void deleteCard(Long id);

  CardsPageRs findAllCards(Integer page, Integer size);

  CardsPageRs finaAllUserCards(String email, Integer page, Integer size);

  CardRs findCard(Long id);

}
