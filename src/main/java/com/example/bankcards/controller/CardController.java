package com.example.bankcards.controller;

import com.example.api.CardApi;
import com.example.bankcards.service.CardService;
import com.example.dto.CardCreationRq;
import com.example.dto.CardRs;
import com.example.dto.CardsPageRs;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardController implements CardApi {

  private final CardService cardService;

  @Override
  public ResponseEntity<Void> deleteCard(final Long id) {
    cardService.deleteCard(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<CardsPageRs> findAllCards(final Integer page, final Integer size) {
    return ResponseEntity.ok(cardService.findAllCards(page, size));
  }

  @Override
  public ResponseEntity<CardsPageRs> findAllUserCards(final Integer page, final Integer size) {
    final String email = SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getName();

    return ResponseEntity.ok(cardService.finaAllUserCards(email, page, size));
  }

  @Override
  public ResponseEntity<CardRs> findCardById(final Long id) {
    return ResponseEntity.ok(cardService.findCard(id));
  }

  @Override
  public ResponseEntity<CardRs> saveCard(@NonNull @Valid final CardCreationRq cardCreationRq) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cardService.saveCard(cardCreationRq));
  }
}
