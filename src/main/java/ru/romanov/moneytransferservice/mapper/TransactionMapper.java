package ru.romanov.moneytransferservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.romanov.moneytransferservice.model.entity.Transaction;
import ru.romanov.moneytransferservice.model.response.TransactionResponse;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "fromAccount.uid", target = "fromAccountUid")
    @Mapping(source = "toAccount.uid", target = "toAccountUid")
    TransactionResponse toResponse(Transaction transaction);
}

