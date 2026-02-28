package br.com.fiap.venda.domain.exception;

import lombok.Getter;

@Getter
public class DomainValidationException extends DomainException {

    private final String field;

    public DomainValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

}
